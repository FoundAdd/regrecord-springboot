package cn.hy.regrecordspringboot.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hy.regrecordspringboot.service.SmsService;
import cn.hy.regrecordspringboot.utils.Constants;
import cn.hy.regrecordspringboot.utils.RedisUtils;
import cn.hy.regrecordspringboot.utils.SmsHelper;
import cn.hy.regrecordspringboot.utils.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    RedisTemplate redisTemplate;
    RedisUtils redisUtils;
    String redisKey;

    public String sendMail (String target) {
        redisKey = TokenHelper.generateToken(target);
        redisUtils = new RedisUtils(redisTemplate);
        Long lastSend = 60001L;

        if (redisUtils.hasKey(redisKey)) { // 判断是否存在这个键
           lastSend = new Date().getTime() - ((Date) redisUtils.hmget(redisKey).get("getTime")).getTime();
        }

        Map<String, Object> map = new HashMap();

        map.put("code", RandomUtil.randomNumbers(6));
        map.put("getTime", new Date());

        // 第一个判断距上一次发送数据是否过去了60秒，第二个判断数据是否成功写入redis服务器
        if (lastSend > 60000 && redisUtils.hmset(redisKey, map, 300)) {
            return new SmsHelper().sendMail(target, map.get("code").toString());
        }
        return Constants.SMS_MAIL_FAILD;
    }

    public String sendPhone (String target) {
        redisKey = TokenHelper.generateToken(target);
        redisUtils = new RedisUtils(redisTemplate);
        Long lastSend = 60001L;

        if (redisUtils.hasKey(redisKey)) { // 判断是否存在这个键
            lastSend = new Date().getTime() - ((Date) redisUtils.hmget(redisKey).get("getTime")).getTime();
        }

        Map<String, Object> map = new HashMap();

        map.put("code", RandomUtil.randomNumbers(6));
        map.put("getTime", new Date());

        // 第一个判断距上一次发送数据是否过去了300秒，第二个判断数据是否成功写入redis服务器
        if (lastSend > 60000 && redisUtils.hmset(redisKey, map, 300)) {
            return new SmsHelper().sendPhone(target, map.get("code").toString());
        }
        return Constants.SMS_PHONE_FAILD;
    }

    public String verifyMail (String rKey, String code) {
        redisKey = TokenHelper.generateToken(rKey);
        redisUtils = new RedisUtils(redisTemplate);
        if (code.equals(redisUtils.hmget(redisKey).get("code"))) {
            redisUtils.hdel(redisKey);
            return Constants.SMS_MAIL_VERIFY_SUCCESS;
        }
        return Constants.SMS_MAIL_VERIFY_FAILD;
    }

    public String verifyPhone (String rKey, String code) {
        redisKey = TokenHelper.generateToken(rKey);
        redisUtils = new RedisUtils(redisTemplate);
        if (code.equals(redisUtils.hmget(redisKey).get("code"))) {
            redisUtils.hdel(redisKey);
            return Constants.SMS_PHONE_VERIFY_SUCCESS;
        }
        return Constants.SMS_PHONE_VERIFY_FAILD;
    }
}
