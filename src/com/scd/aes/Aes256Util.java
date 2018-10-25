package com.scd.aes;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * @author chengdu
 * 256位 32字节 key
 * 秘钥key 长度支持 128/192/256 位  16、24、32字节
 */
public class Aes256Util {

    public static boolean initialized = false;

    public static final String UTF8ENCODE = "utf-8";

    public static final String ALGORITHM_PKCS7Padding = "AES/ECB/PKCS7Padding";

    private static final String ALGORITHM_PKCS5Padding = "AES/ECB/PKCS5Padding";

    public static byte[] Aes256Encode(String str, String key){
        initialize();
        byte[] result = null;
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_PKCS7Padding, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"); //生成加密解密需要的Key
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            result = cipher.doFinal(str.getBytes(UTF8ENCODE));
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String Aes256Decode(byte[] bytes, String key){
        initialize();
        String result = null;
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_PKCS7Padding, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"); //生成加密解密需要的Key
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(bytes);
            result = new String(decoded, UTF8ENCODE);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] aes256EncrptyDefault(String str, String key) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM_PKCS5Padding);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"); //生成加密解密需要的Key
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] result = cipher.doFinal(str.getBytes(UTF8ENCODE));
        return result;
    }

    public static String aes256dcrptyDefault(byte[] bytes, String key) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM_PKCS5Padding);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"));
        bytes = cipher.doFinal(bytes);
        return new String(bytes, UTF8ENCODE);
    }


    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    public static String generateKey(int size){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < size; i++){
            sb.append("1");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        String key = generateKey(256/8);
        System.out.println(key.length());
        byte[] encrptyArr = aes256EncrptyDefault("成都", key);
        System.out.println(encrptyArr);
        String originStr = aes256dcrptyDefault(encrptyArr, key);
        System.out.println(originStr);
        byte[] pkcs7Attr = Aes256Encode("成都",key);
        System.out.println(pkcs7Attr);
        String orginstr7 = Aes256Decode(pkcs7Attr, key);
        System.out.println(orginstr7);
    }
}
