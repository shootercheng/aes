package com.scd.aes.test.ascii;

import org.junit.Test;

/**
 * @author James
 * @date 2023/4/2 21:14
 */
public class PrintAsciiTest {

    @Test
    public void testPrintChar() {
        printStr(32, 47);
        printStr(48, 57);
        printStr(58, 64);
        printStr(65, 90);
        printStr(91, 96);
        printStr(97, 122);
    }

    private void printStr(int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            char curChar = (char) i;
            stringBuilder.append(curChar);
        }
        System.out.println(stringBuilder);
    }
}
