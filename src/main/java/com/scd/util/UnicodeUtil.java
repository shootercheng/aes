package com.scd.util;

import javafx.beans.binding.When;

import java.text.Normalizer;
import java.util.StringTokenizer;

/**
 * @author chengdu
 * @date 2019/12/5
 */
public class UnicodeUtil {

    /*
     * 中文转unicode编码
     */
    public static String gbEncoding(String gbString) {
        char[] utfBytes = gbString.toCharArray();
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < utfBytes.length; i++) {
            String hexByte = Integer.toHexString(utfBytes[i]);
            if (hexByte.length() <= 2) {
                hexByte = "00" + hexByte;
            }
            stringBuilder.append("\\u").append(hexByte);
        }
        return stringBuilder.toString();
    }
    /*
     * unicode编码转中文
     */
    public static String decodeUnicode(String dataStr) {
        StringBuilder builder = new StringBuilder();
        StringTokenizer stringTokenizer = new StringTokenizer(dataStr, "\\u");
        while (stringTokenizer.hasMoreTokens()) {
            String hexStr = stringTokenizer.nextToken();
            char result = (char) Integer.parseInt(hexStr, 16);
            builder.append(result);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String msg = "你好";
        String unicode = gbEncoding(msg);
        String decode = decodeUnicode(unicode);
        System.out.println(decode);
    }
}
