package cn.hy.regrecordspringboot.utils;

import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

@Data
@Slf4j(topic = "SMSLogger")
public class SmsHelper {
    public String sendMail(String target, String code) {
        try {
            String mailId = MailUtil.send(target, "身份校验", createContent(target, code), true);
            log.info("邮箱验证码发送成功, 邮件id: " + mailId + ", 目标邮箱: " + target + ", 验证码为: " + code);
            return Constants.SMS_MAIL_SUCCESS;
        }catch (Exception e) {
            log.warn("邮箱验证码发送失败, 目标邮箱: " + target + ", 验证码为: " + code + ", \n错误栈: " + e.getMessage());
            return Constants.SMS_MAIL_FAILD;
        }
    }

    public String sendPhone(String target, String code) {
        if (upyunRemain() > 3) {    // 判断又拍云短信余量
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;
            try {
                URLConnection conn = new URL("https://sms-api.upyun.com/api/messages").openConnection();
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setRequestProperty("Authorization", "upyun user token");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // 获取URLConnection对象对应的输出流并发送参数
                printWriter = new PrintWriter(conn.getOutputStream());
                printWriter.print("mobile=" + target +
                        "&template_id=3809" +
                        "&vars=" + code);
                // 刷新输出流的缓冲
                printWriter.flush();

                // 定义BufferedReader输入流来读取URL的响应
                String result = "";
                bufferedReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonResult = JSON.parseObject(result);
                if (jsonResult.getString("code").equals("0")) {
                    JSONObject msg_ids = jsonResult.getJSONArray("message_ids").getJSONObject(0);

                    if (msg_ids.getString("report_code").equals("0")) {
                        log.info("又拍云验证短信发送成功, 短信编号: " + msg_ids.getString("msg_id") + ", 目标手机号: " + msg_ids.getString("mobile") + ", 验证码为: " + code);
                        return Constants.SMS_PHONE_SUCCESS;
                    }else {
                        log.warn("又拍云验证短信发送失败, 返回码(report_code): " + msg_ids.getString("report_code") +
                                ", 报错信息(error_code): " + msg_ids.getString("error_code") +
                                ", 目标手机号: " + msg_ids.getString("mobile") +
                                ", 待发送的验证码: " + code);
                        return Constants.SMS_PHONE_FAILD;
                    }
                }
                return Constants.SMS_PHONE_FAILD;
            } catch (Exception e) {
                log.error("向手机号 " + target + " 发送又拍云验证短信时发生了错误, 错误栈:\n" + e);
                return Constants.SMS_PHONE_FAILD;
            } finally {
                try{
                    if(printWriter != null){
                        printWriter.close();
                    }
                    if(bufferedReader != null){
                        bufferedReader.close();
                    }
                } catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        } else {    // 又拍云短信余量不足，转到阿里云
            Config config = new Config().setAccessKeyId("Aliyun AccessKeyId").setAccessKeySecret("Aliyun AccessKeySecret");
            config.endpoint = "dysmsapi.aliyuncs.com";
            try {
                Client client = new Client(config);
                SendSmsRequest sendSmsRequest = new SendSmsRequest();
                sendSmsRequest.setPhoneNumbers(target);
                sendSmsRequest.setSignName("");
                sendSmsRequest.setTemplateCode("");
                sendSmsRequest.setTemplateParam(code);

                SendSmsResponseBody resBody = client.sendSms(sendSmsRequest).getBody();

                if (resBody.getCode().equals("OK")) { // 判断短信是否发送成功，成功则写入一条info到日志，否则写入一条warn到日志
                    log.info("阿里云向手机号 " + target + " 发送的验证短信成功完成, 请求id: " + resBody.getRequestId() +
                            ", 回执id: " + resBody.getBizId() +
                            ", 验证码: " + code);
                    return Constants.SMS_PHONE_SUCCESS;
                } else {
                    log.warn("阿里云向手机号 " + target + " 发送的验证短信未能完成, 请求id: " + resBody.getRequestId() +
                            ", 错误码: " + resBody.getCode() +
                            ", 相关信息: " + resBody.getMessage() +
                            ", 验证码: " + code);
                    return Constants.SMS_PHONE_FAILD;
                }
            } catch (Exception e) {
                log.error("向手机号 " + target + " 发送阿里云验证短信时发生了错误, 错误栈:\n" + e);
                return Constants.SMS_PHONE_FAILD;
            }
        }
    }

    /**
     *
     * @param target 目标邮箱
     * @param code 验证码
     * @return 通过模板生成的html文本
     */
    private String createContent(String target, String code) {
        return "<div class=\"content\" style=\"width: 500px; background: #FFE; margin: auto; padding:50px; display: flex; flex-direction: column;outline: 1px solid #DDD;\">\n" +
                "  <div class=\"header\" style=\"width: 100%; margin: 5px 0; padding-left: 20px; align-self: center;outline: 1px solid #DDD;\">\n" +
                "    <p style=\"font-size: 30px; font-weight: bolder; margin: 0px; padding: 10px 0;\">钥匙串 - 验证码</p>\n" +
                "  </div>\n" +
                "  <div class=\"container\" style=\"width: 100%; margin: 5px 0; padding-left: 20px; align-self: center; outline: 1px solid #DDD;\">\n" +
                "    <p>您好, <a href=\"mailto:" + target + "\">" + target + "</a></p>\n" +
                "    <p>感谢使用钥匙串！</p>\n" +
                "    <p>您本次的验证码是: <strong>" + code + "</strong></p>\n" +
                "    <p>请妥善保管并尽快填写到相应页面中，验证码在5分钟之后失效。</p>\n" +
                "  </div>\n" +
                "  <div class=\"footer\" style=\"width: 100%; margin: 5px 0; padding-left: 20px; align-self: center; outline: 1px solid #DDD;\">\n" +
                "  \t<p><a href=\"http://www.example.com\" target=\"_blank\">@example.com</a></p>\n" +
                "  </div>\n" +
                "</div>";
    }

    /**
     * 查询又拍云短信余量
     */
    private Integer upyunRemain() {
        BufferedReader in = null;
        try {
            URLConnection conn = new URL("https://sms-api.upyun.com/api/users/remain").openConnection();  // 打开和URL之间的连接

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Authorization", "upyun user token");

            // 建立实际的连接
            conn.connect();

            // 获取所有响应头字段
            String result = "";
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return JSON.parseObject(result).getInteger("industry");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return 0;
    }
}
