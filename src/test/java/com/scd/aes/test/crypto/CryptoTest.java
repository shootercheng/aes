package com.scd.aes.test.crypto;

import com.scd.aes.CryptoUtil;
import com.scd.model.CryptoKey;
import com.scd.util.Base64Util;
import com.scd.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
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

    @Test
    public void testEncryptFile() {
        String filePath = "file/PDF_Software.pdf";
        String encryptPath = "file/Encrypt_PDF_Software.pdf";
        String decryptPath = "file/Decrypt_PDF_Software.pdf";
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        CryptoUtil.encrypt(cryptoKey, filePath, encryptPath);
        CryptoUtil.decrypt(cryptoKey, encryptPath, decryptPath);
    }

    @Test
    public void testEncryptImg() {
        String filePath = "file/java-exception.jpg";
        String encryptPath = "file/Encrypt_java-exception.jpg";
        String decryptPath = "file/Decrypt_java-exception.jpg";
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        CryptoUtil.encrypt(cryptoKey, filePath, encryptPath);
        CryptoUtil.decrypt(cryptoKey, encryptPath, decryptPath);
    }

    /**
     * size 1.39 GB (1,501,102,080 字节)
     * time 1m 54s 762 ms
     */
    @Test
    public void testEncryptLargeFile() {
        // size
        String filePath = "E:\\ISO\\Linux\\ubuntu-17-10.iso";
        File file = new File(filePath);
        String parentPath = file.getParent();
        String fileName = file.getName();
        String encryptPath = parentPath + File.separator + "Encrypt_" + fileName;
        String decryptPath = parentPath + File.separator + "Decrypt_" + fileName;
        CryptoKey cryptoKey = CryptoUtil.generateRandomKey();
        CryptoUtil.encrypt(cryptoKey, filePath, encryptPath);
        CryptoUtil.decrypt(cryptoKey, encryptPath, decryptPath);
    }
}
