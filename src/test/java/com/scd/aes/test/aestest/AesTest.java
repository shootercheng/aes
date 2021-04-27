package com.scd.aes.test.aestest;

import com.scd.aes.AesUtil;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class AesTest {

    @Test
    public void testEnKey() throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 16; i++){
            stringBuilder.append("1");
        }
        String key = stringBuilder.toString();
        String str = "成都";
        byte[] encryptBytes = AesUtil.aesEncrypt(str, key);
        String decryptStr = AesUtil.aesDecrypt(encryptBytes, key);
        Assert.assertEquals(decryptStr, str);
    }
}
