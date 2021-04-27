package com.scd.aes.test.crypto;

import com.scd.aes.CryptoUtil;
import com.scd.model.CryptoKey;
import com.scd.util.Base64Util;
import com.scd.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author James
 */
public class CryptoTest {

    @Test
    public void testGenKey() {
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        Assert.assertEquals(32, Base64Util.decryptBASE64(cryptoKey.getSecretKeySpec()).length);
        Assert.assertEquals(16, Base64Util.decryptBASE64(cryptoKey.getIvParameterSpec()).length);
    }

    @Test
    public void testEncryptCn() {
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        String text = "孙成都";
        String encrypt = CryptoUtil.encrypt(cryptoKey.getSecretKeySpec(), cryptoKey.getIvParameterSpec(), text);
        System.out.println(encrypt);
        String decrypt = CryptoUtil.decrypt(cryptoKey.getSecretKeySpec(), cryptoKey.getIvParameterSpec(), encrypt);
        Assert.assertEquals(text, decrypt);
    }

    @Test
    public void testEncryptCnEn() {
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        String text = "孙成都 sunchengdu 123456";
        String encrypt = CryptoUtil.encrypt(cryptoKey.getSecretKeySpec(), cryptoKey.getIvParameterSpec(), text);
        System.out.println(encrypt);
        String decrypt = CryptoUtil.decrypt(cryptoKey.getSecretKeySpec(), cryptoKey.getIvParameterSpec(), encrypt);
        Assert.assertEquals(text, decrypt);
    }

    @Test
    public void testKey() {
        String key = KeyUtil.genRandomKey(32);
        String iv = KeyUtil.genRandomKey(16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        String text = "孙成都 sunchengdu 123456";
        String encrypt = CryptoUtil.encrypt(secretKeySpec, ivParameterSpec, text);
        System.out.println(encrypt);
        String decrypt = CryptoUtil.decrypt(secretKeySpec, ivParameterSpec, encrypt);
        Assert.assertEquals(text, decrypt);
    }

}
