package com.scd.aes.test.aes256test;

import com.scd.aes.Aes256Util;
import com.scd.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class Aes256Test {

    @Test
    public void testAes256(){
        StringBuilder stringBuilder = new StringBuilder("");
        // Key length not 128/192/256 bits
        int keylength = 256 / 8;
        for (int i = 0; i < keylength; i++){
            stringBuilder.append("1");
        }
        String key = stringBuilder.toString();
        String str = "成都";
        byte[] encryptByte = Aes256Util.aes256Encode(str, key);
        String decryptStr = Aes256Util.aes256Decode(encryptByte, key);
        Assert.assertEquals(str, decryptStr);
    }

    @Test
    public void testAes256WithKey(){
        String key = KeyUtil.genRandomKey(256 / 8);
        String str = "s成都";
        byte[] encryptByte = Aes256Util.aes256Encode(str, key);
        String decryptStr = Aes256Util.aes256Decode(encryptByte, key);
        Assert.assertEquals(str, decryptStr);
    }
}
