package cn.hy.regrecordspringboot.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hy.regrecordspringboot.exception.StringHandlerException;

public class TokenHelper {
    public static String generateToken(String token_key) {
        String token = null;
        try {
            token = AESHelper.encode(DigestUtil.md5Hex(token_key));
        } catch (StringHandlerException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static boolean verifyToken(String token_key, String token) {
        return token.equals(generateToken(token_key));
    }
}
