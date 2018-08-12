package com.newland.jxh.store.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

/**
 * @Author qyw
 * @Description MD5加密工具
 * @Date Created in 23:07 2018/8/12
 */
@Slf4j
public enum Md5UtilEnum {

    INSTANCE;

    /**
     * MD5方法
     * @param clearText 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String clearText, String key){
        //加密后的字符串
        String encodeStr=DigestUtils.md5DigestAsHex((clearText + key).getBytes());
        //System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        log.info("MD5加密后的字符串为:encodeStr:{}",encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param clearText 明文
     * @param key 密钥
     * @param cipherText 密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String clearText, String key, String cipherText) {
        //根据传入的密钥进行验证
        String encodeStr = md5(clearText, key);
        if(encodeStr.equalsIgnoreCase(cipherText))
        {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        String encodeStr = Md5UtilEnum.md5("qyw", "salt");
        System.out.println(Md5UtilEnum.verify("qyw", "salt","9fd10b2e96673285d08ba5f230fa16a8"));
    }
}
