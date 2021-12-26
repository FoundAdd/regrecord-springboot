package cn.hy.regrecordspringboot.controller;

import cn.hutool.core.util.URLUtil;
import cn.hy.regrecordspringboot.bean.dto.SmsDTO;
import cn.hy.regrecordspringboot.bean.entity.Account;
import cn.hy.regrecordspringboot.service.AccountService;
import cn.hy.regrecordspringboot.service.SmsService;
import cn.hy.regrecordspringboot.utils.Constants;
import cn.hy.regrecordspringboot.utils.TokenHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    @Autowired
    SmsService smsService;
    @Autowired
    AccountService accountService;
    SmsDTO smsDTO;
    @RequestMapping(value = "/sms/send/mail",  produces = "application/json;charset=gbk")
    public String sendMail (@RequestBody String data) {
        Account account = null;
        JSONObject jsonObject = JSON.parseObject(URLUtil.decode(data, "GBK"));
        smsDTO = new SmsDTO();

        if (jsonObject.getString("uid") != null) {
            account = accountService.selectForTokenByPrimaryKey(jsonObject.getString("uid"));
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), jsonObject.getString("token"))) {
            // 调用发送邮件的方法 传递参数
            String smsStatus = smsService.sendMail(jsonObject.getString("to"));
            smsDTO.setStatus(smsStatus);
            if (smsStatus.equals(Constants.SMS_MAIL_SUCCESS)) {
                smsDTO.setMsg("发送成功");
            } else {
                smsDTO.setMsg("发送失败");
            }
            return JSON.toJSONString(smsDTO);
        } else {
            smsDTO.setStatus(Constants.TOKEN_VERIFY_FAILD);
            smsDTO.setMsg("token或uid 非法");
            return JSON.toJSONString(smsDTO);
        }
    }

    @RequestMapping(value = "/sms/send/phone", produces = "application/json;charset=gbk")
    public String sendPhone(@RequestBody String data) {
        Account account = null;
        JSONObject jsonObject = JSON.parseObject(URLUtil.decode(data, "GBK"));
        smsDTO = new SmsDTO();

        if (jsonObject.getString("uid") != null) {
            account = accountService.selectForTokenByPrimaryKey(jsonObject.getString("uid"));
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), jsonObject.getString("token"))) {
            // 调用发送邮件的方法 传递参数
            String smsStatus = smsService.sendPhone(jsonObject.getString("to"));
            smsDTO.setStatus(smsStatus);
            if (smsStatus.equals(Constants.SMS_MAIL_SUCCESS)) {
                smsDTO.setMsg("发送成功");
            } else {
                smsDTO.setMsg("发送失败");
            }
            return JSON.toJSONString(smsDTO);
        } else {
            smsDTO.setStatus(Constants.TOKEN_VERIFY_FAILD);
            smsDTO.setMsg("token或uid 非法");
            return JSON.toJSONString(smsDTO);
        }
    }

    @RequestMapping(value = "/sms/verify")
    public String verifyMail(@RequestBody String data) {
        JSONObject jsonObject = JSON.parseObject(URLUtil.decode(data, "GBK"));
        String token = jsonObject.getString("token");   // 注：这里的的token是待修改的手机、邮箱或昵称
        String code = jsonObject.getString("code");
        String verifyType = jsonObject.getString("type");
        String verifyResult = "";
        smsDTO = new SmsDTO();

        if (verifyType.equals("uMail")) {
            verifyResult = smsService.verifyMail(token, code);
        } else if (verifyType.equals("uPhone")){
            verifyResult = smsService.verifyPhone(token, code);
        }

        if (verifyResult.equals(Constants.SMS_MAIL_VERIFY_SUCCESS) || verifyResult.equals(Constants.SMS_PHONE_VERIFY_SUCCESS)) {
            if (verifyType.equals("uMail")) {
                smsDTO.setStatus(Constants.SMS_MAIL_VERIFY_SUCCESS);
            }else if (verifyType.equals("uPhone")) {
                smsDTO.setStatus(Constants.SMS_PHONE_VERIFY_SUCCESS);
            }
        }else {
            if (verifyType.equals("uMail")) {
                smsDTO.setStatus(Constants.SMS_MAIL_VERIFY_FAILD);
            }else if (verifyType.equals("uPhone")) {
                smsDTO.setStatus(Constants.SMS_PHONE_VERIFY_FAILD);
            }
        }
        return JSON.toJSONString(smsDTO);
    }
}
