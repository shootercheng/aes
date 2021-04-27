package com.scd.aes.test.randomkey;

import com.scd.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class RandomKeyTest {

    @Test
    public void testRandomKey(){
        String key = KeyUtil.genRandomKey(32);
        System.out.println(key);
        Assert.assertEquals(32, key.length());
    }
}
