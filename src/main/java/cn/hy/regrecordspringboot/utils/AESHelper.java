package cn.hy.regrecordspringboot.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hy.regrecordspringboot.exception.StringHandlerException;
import cn.hy.regrecordspringboot.exception.StringHandlerException;

import java.io.UnsupportedEncodingException;

public class AESHelper {
    public static String encode(String srcText) throws StringHandlerException {
        return encode(srcText, "template", "gbk");
    }
    public static String encode(String srcText, String key) throws StringHandlerException {
        return encode(srcText, key, "gbk");
    }
    public static String encode(String srcText, String key, String charset) throws StringHandlerException {
        if (srcText.equals("") || srcText == null) {
            throw new StringHandlerException("srcText不能为空字符串");
        }
        while (key.length() < 16) {
            key += "x";
        }

        SymmetricCrypto aes = null;
        try {
            aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //加密
        return aes.encryptBase64(srcText);
    }

    public static String decode(String cipher) throws StringHandlerException {
        return decode(cipher, "template", "gbk");
    }
    public static String decode(String cipher, String key) throws StringHandlerException {
        return decode(cipher, key, "gbk");
    }
    public static String decode(String cipher, String key, String charset) throws StringHandlerException {
        if (cipher.equals("") || cipher == null) {
            throw new StringHandlerException("cipher不能为空字符串");
        }
        while (key.length() < 16) {
            key += "x";
        }
        //构建
        SymmetricCrypto aes = null;
        try {
            aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return aes.decryptStr(cipher, CharsetUtil.CHARSET_UTF_8);
    }
}
