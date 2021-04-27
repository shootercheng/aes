package com.scd.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Base64;

import com.scd.constant.Constant;
import com.scd.exception.DecryptException;
import com.scd.exception.EncryptException;

/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class Base64Util {

    public static byte[] decryptBASE64(String key) {
        return Base64.getDecoder().decode(key);
    }                 

    public static String encryptBASE64(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }

    public static String encryptImgBASE64(String picPath) {
        try (InputStream inputStream = new FileInputStream(picPath)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int readLen;
            byte[] buffer = new byte[Constant.BUFFER_SIZE];
            while ((readLen = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLen);
            }
            return encryptBASE64(outputStream.toByteArray());
        } catch (IOException e) {
            throw new EncryptException("encode img error ", e);
        }
    }  


    public static void decryptBASE64Img(String str, String picPath) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(picPath, "rw")) {
            byte[] result = decryptBASE64(str);
            randomAccessFile.write(result);
        } catch (IOException e) {
            throw new DecryptException("decrypt img error ", e);
        }
    }
}
