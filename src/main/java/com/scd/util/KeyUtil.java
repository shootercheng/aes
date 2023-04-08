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
        int lene = ENCRYPT_ELEMENTS.length;
        for (int i = 0; i < keyLength; i++) {
            int arrIndex = SecureRandomHolder.random.nextInt(lene);
            key.append(generateOneElement(arrIndex));
        }
        return key.toString();
    }

    private static char generateOneElement(int arrIndex) {
        int arrLen = ENCRYPT_ELEMENTS[arrIndex].length;
        return ENCRYPT_ELEMENTS[arrIndex][SecureRandomHolder.random.nextInt(arrLen)];
    }

    public static String genUidKey(int keyLength) {
        if (keyLength > 36) {
            throw new IllegalArgumentException("key length is too Long");
        }
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, keyLength);
    }
}
