package com.scd.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class KeyUtil {

    private static char[][] ENCRYPT_ELEMENTS = {
            new char[]{'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', ':', ',', '.', '?','|','"'},
            new char[]{'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '[', ']', ';', '<', '>', '/', '\\','\''},
            new char[]{'A', 'B', 'C', 'D', 'E', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'},
            new char[]{'a', 'b', 'c', 'd', 'e', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                    'v', 'w', 'x', 'y', 'z'}
    };

    private static class SecureRandomHolder {
        static final SecureRandom random = new SecureRandom();
    }

    public static String genRandomKey(int keyLength) {
        StringBuilder key = new StringBuilder(keyLength);
        char strKey;
        int index;
        for (int i = 0; i < keyLength; i++) {
            // 产生随机二位数组索引 0-3
            index = SecureRandomHolder.random.nextInt(4);
            switch (index) {
                case 0:
                    // 第一个二位数组索引
                    index = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[0].length);
                    strKey = ENCRYPT_ELEMENTS[0][index];
                    key.append(strKey);
                    break;
                case 1:
                    // 第二个二位数组索引
                    index = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[1].length);
                    strKey = ENCRYPT_ELEMENTS[1][index];
                    key.append(strKey);
                    break;
                case 2:
                    // 第三个二位数组索引
                    index = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[2].length);
                    strKey = ENCRYPT_ELEMENTS[2][index];
                    key.append(strKey);
                    break;
                case 3:
                    index = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[3].length);
                    strKey = ENCRYPT_ELEMENTS[3][index];
                    key.append(strKey);
                default:
                    break;
            }
        }
        return key.toString();
    }

    public static String genUidKey(int keyLength) {
        if (keyLength > 36) {
            throw new IllegalArgumentException("key length is too Long");
        }
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, keyLength);
    }
}
