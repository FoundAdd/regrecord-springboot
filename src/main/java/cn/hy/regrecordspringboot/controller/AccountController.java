package cn.hy.regrecordspringboot.controller;

import cn.hutool.core.util.URLUtil;
import cn.hy.regrecordspringboot.bean.entity.Account;
import cn.hy.regrecordspringboot.exception.SelectDataBaseException;
import cn.hy.regrecordspringboot.exception.StringHandlerException;
import cn.hy.regrecordspringboot.service.AccountService;
import cn.hy.regrecordspringboot.utils.Constants;
import cn.hy.regrecordspringboot.utils.RedisUtils;
import cn.hy.regrecordspringboot.utils.TokenHelper;
import cn.hy.regrecordspringboot.service.AccountService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    RedisTemplate redisTemplate;
    RedisUtils redisUtils;
    Account account = null;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord, @RequestParam("userType") String logNameType) {
        switch (logNameType) {
            case "-1":
                return "{\"status_code\": " + Constants.LOGIN_FAILD + "}";
            case "0":
                account = accountService.selectPasswordByName(userName);break;
            case "1":
                account = accountService.selectPasswordByMail(userName);break;
            case "2":
                account = accountService.selectPasswordByPhone(userName);break;
        }

        String db_password = account==null ? null : account.getAccountPassword();
        String db_account_status = account==null ? null : account.getAccountStatu();

        if (passWord.equals(db_password) && db_account_status.equals("100000") ) {  // 通过了校验，返回登录成功状态码
            return "{\"status_code\": " + Constants.LOGIN_SUCCESS +
                    ", \"uid\": " + account.getAccountId() +
                    ", \"token\": \"" + TokenHelper.generateToken(account.getAccountId().toString() + account.getAccountName() + account.getAccountPassword()) + "\"}";
        } else if (db_password == null || !passWord.equals(db_password)) { //虽然判断出了账户不存在，但是为了用户的账户安全，不应将此信息返回给用户，所以返回密码错误状态码
            return "{\"status_code\": " + Constants.LOGIN_PASSWORD_ERROR + "}";
        } else if (passWord.equals(db_password) && (db_account_status.equals("100001") || db_account_status.equals("100002"))) {    // 虽然通过了信息校验，但是账户被封禁了，所以返回账户封禁状态码
            return "{\"status_code\": " + Constants.LOGIN_ACCOUNT_BANED + "}";
        }
        return "{\"status_code\": " + Constants.LOGIN_FAILD + "}";  // 有未知的判断类型没有处理到，返回默认的其他失败状态码，将状态码反馈给管理员就能通过日志查出问题所在
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {
        if (!userName.matches("^[a-zA-Z][a-zA-Z0-9_ ]{5,15}$")) {
            return Constants.REGISTER_ACCOUNT_ERROR;    // 用户格式错误，防止被抓包攻击
        }
        if (accountService.selectCountByAccountName(userName) > 0) {    // 用户已存在
            return Constants.REGISTER_ACCOUNT_EXIST;
        }
        Account account = new Account();
        account.setAccountName(userName);
        account.setAccountPassword(passWord);
        account.setAccountCreateTime(new Date());

        Integer result = accountService.insert(account);

        if (result == 1){
            return Constants.REGISTER_SUCCESS;
        }
        return Constants.REGISTER_FAILD;
    }

    @RequestMapping(value = {"/accountInfo", "/accountInfo/"}, produces = "text/html;charset=gbk")
    public ModelAndView accountInfo(){
        return new ModelAndView("accountInfo");
    }

    @RequestMapping(value = "/accountInfo/queryAll", produces="application/json;charset=gbk")
    public String accountInfo(@RequestParam("uid") String userId, @RequestParam("token") String token) throws StringHandlerException {
        Account account = accountService.selectByPrimaryKey(userId);
        if (account != null && TokenHelper.verifyToken((account.getAccountId().toString() + account.getAccountName() + account.getAccountPassword()), token)){
            Map<String, String> map = new HashMap();
            map.put("uid", account.getAccountId().toString());
            map.put("uLevel", account.getAccountLevel().toString());
            map.put("uNickName", account.getAccountNickName());
            map.put("uName", account.getAccountName());
            map.put("uPhone", account.getAccountPhone());
            map.put("uMail", account.getAccountMail());
            map.put("uStatus", account.getAccountStatu());
            return JSON.toJSONString(map);
        } else{
            throw new SelectDataBaseException("查询帐号信息失败, 错误码: " + Constants.SELECT_FAILD_EMPTY);
        }
    }

    @RequestMapping(value = "/accountInfo/authPwd", produces = "application/json;charset=gbk")
    public String verifyPassWord(@RequestBody String data) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonData = JSON.parseObject(URLUtil.decode(data, "GBK"));
        String token = jsonData.getString("token");
        String requestPwd = jsonData.getString("password");

        if (jsonData.getString("uid") != null) {
            account = accountService.selectForTokenByPrimaryKey(jsonData.getString("uid"));
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            if (requestPwd.equals(account.getAccountPassword())) { // 密码正确
                redisUtils = new RedisUtils(redisTemplate);
                String authID = TokenHelper.generateToken(requestPwd + token);
                try {
                    redisUtils.set(token + jsonData.getString("uid"), authID, 900);
                    map.put("authID", authID);
                    map.put("status", Constants.PASSWORD_VERIFY_SUCCESS);
                    map.put("msg", "密码正确");
                }catch (Exception e) {
                    map.put("authID", "-1");
                    map.put("status", Constants.PASSWORD_VERIFY_ERROR);
                    map.put("msg", "其他错误");
                }

            } else {
                map.put("authID", "-1");
                map.put("status", Constants.PASSWORD_VERIFY_FAILED);
                map.put("msg", "密码有误");
            }
        } else {
            map.put("authID", "-1");
            map.put("status", Constants.PASSWORD_VERIFY_FAILED);
            map.put("msg", "uid或token不正确");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 前端发起请求的部分在accountInfoModal.js文件的322行，修改 - 密码 | 手机 | 邮箱 | 昵称
     * @param data 请求参数（token、uid、password | phone | mail | nickName）
     * @return 回调参数（status）
     */
    @RequestMapping(value = "/accountInfo/modify/password", method = RequestMethod.PUT, produces = "application/json;charset=gbk")
    public String editPassWord(@RequestBody String data) {
        return JSON.toJSONString(updateAccountInfo(JSON.parseObject(URLUtil.decode(data)), "password"));
    }

    @RequestMapping(value = "/accountInfo/modify/phone", method = RequestMethod.PUT,  produces = "application/json;charset=gbk")
    public String editPhone(@RequestBody String data) {
        return JSON.toJSONString(updateAccountInfo(JSON.parseObject(URLUtil.decode(data)), "phone"));
    }

    @RequestMapping(value = "/accountInfo/modify/mail", method = RequestMethod.PUT,  produces = "application/json;charset=gbk")
    public String editMail(@RequestBody String data) {
        return JSON.toJSONString(updateAccountInfo(JSON.parseObject(URLUtil.decode(data)), "mail"));
    }

    @RequestMapping(value = "/accountInfo/modify/nickname", method = RequestMethod.PUT,  produces = "application/json;charset=gbk")
    public String editNick(@RequestBody String data) {
        return JSON.toJSONString(updateAccountInfo(JSON.parseObject(URLUtil.decode(data)), "nickName"));
    }

    /**
     * 验证昵称可用性（accountInfoModal.js第467行）
     * @param uid 请求参数（uid、token、nick）
     * @return 回调参数（JSON对象，key: status）
     */
    @RequestMapping(value = "/accountInfo/nickAvailable", method = RequestMethod.GET)
    public String nickAvailable(@RequestParam String uid, @RequestParam String token, @RequestParam String nick) {
        Map<String, String> map = new HashMap<>();
        if (uid != null) {
            account = accountService.selectForTokenByPrimaryKey(uid);
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            // 先检测文本中是否含有敏感词（敏感词检测api太贵了，免费的效果不理想，先空着）

            // 控制nickname的格式：字母或中文开头，10个以下字符，允许字母数字中文以及下划线
            if (!nick.matches("^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9_\\u4e00-\\u9fa5]{1,10}$")) {
                map.put("status", Constants.NICK_ILLEGAL);
            }
            // 通过敏感词检测后再验证是否重复
            if (map.size() < 1 && accountService.selectByNick(nick) < 1) {
                map.put("nickAuthID", TokenHelper.generateToken(nick));
                map.put("status", Constants.NICK_PASS);
            }
        } else {
            map.put("status", Constants.NICK_REPEAT);
        }
        return JSON.toJSONString(map);
    }

    /**
     * 抽取的更新操作公用方法
     * @param data 由前端传来的字符串处理得来的JSON对象
     * @param updateStr 修改词条，由此字符串取得JSON对象中对应的待修改的数据
     * @return 一个Map对象，存放有一个key值为status的状态码
     */
    public Map<String, Object> updateAccountInfo(JSONObject data, String updateStr) {
        String token = data.getString("token");
        Map<String, Object> map = new HashMap<>();
        if (data.getString("uid") != null) {
            account = accountService.selectForTokenByPrimaryKey(data.getString("uid"));
        }
        if (account != null && TokenHelper.verifyToken((account.getAccountId() + account.getAccountName() + account.getAccountPassword()), token)) {
            Integer updateColumns = 0;
            switch (updateStr){
                case "password":
                    if (data.getString("authID").equals(TokenHelper.generateToken(account.getAccountPassword() + data.getString("token")))){
                        updateColumns = accountService.updatePasswordByPrimaryKey(data.getString(updateStr), data.getString("uid"));
                    } else {
                        map.put("msg", "刷新页面后尝试重新校验密码");
                    }
                    break;
                case "phone":
                    updateColumns = accountService.updatePhoneByPrimaryKey(data.getString(updateStr), data.getString("uid"));
                    break;
                case "mail":
                    updateColumns = accountService.updateMailByPrimaryKey(data.getString(updateStr), data.getString("uid"));
                    break;
                case "nickName":
                    if (data.getString("nickAuthID").equals(TokenHelper.generateToken(data.getString("nickName")))) {
                        updateColumns = accountService.updateNickNameByPrimaryKey(data.getString(updateStr), data.getString("uid"));
                    } else {
                        map.put("msg", "刷新页面后尝试重新校验昵称" +
                                "");
                    }
                    break;
            }
            if (updateColumns == 1) {
                map.put("status", Constants.UPDATE_SUCCESS);
                return map;
            }
        }
        map.put("status", Constants.UPDATE_FAILED);
        return map;
    }
}
