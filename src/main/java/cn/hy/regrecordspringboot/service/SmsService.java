package cn.hy.regrecordspringboot.service;

public interface SmsService {
    String sendMail (String target);

    String sendPhone (String target);

    String verifyMail (String rKey, String code);

    String verifyPhone (String rKey, String code);
}
