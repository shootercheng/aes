package com.scd.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author chengdu
 * @date 2019/8/3.
 */
public class KeyUtil {

    private static String[][] ENCRYPT_ELEMENTS = {
            new String[]{"~","!","@","#","$","%","^","&","*","(",")","_","+","{","}",":",",",".","?"},
            new String[]{"`","1","2","3","4","5","6","7","8","9","0","-","=","[","]",";","<",">","/"},
            new String[]{"A","B","C","D","E","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U",
                    "V","W","X","Y","Z"},
            new String[]{"a","b","c","d","e","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u",
                    "v","w","x","y","z"}
    };

    private static class SecureRandomHolder {
        static final SecureRandom random = new SecureRandom();
    }

	public static String genRandomKey(int keyLength) {
		StringBuilder key = new StringBuilder();
        String strKey = null;
        int indexTwoDim = 0;
		for (int i = 0; i < keyLength; i++) {
			// 产生随机二位数组索引 0-3
			int index = SecureRandomHolder.random.nextInt(4);
			switch (index){
				case 0:
				    // 第一个二位数组索引
                    indexTwoDim = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[0].length);
                    strKey = ENCRYPT_ELEMENTS[0][indexTwoDim];
                    key.append(strKey);
					break;
				case 1:
				    // 第二个二位数组索引
                    indexTwoDim = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[1].length);
					strKey = ENCRYPT_ELEMENTS[1][indexTwoDim];
					key.append(strKey);
					break;
				case 2:
					// 第三个二位数组索引
                    indexTwoDim = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[2].length);
					strKey = ENCRYPT_ELEMENTS[2][indexTwoDim];
					key.append(strKey);
					break;
                case 3:
                    indexTwoDim = SecureRandomHolder.random.nextInt(ENCRYPT_ELEMENTS[3].length);
                    strKey = ENCRYPT_ELEMENTS[3][indexTwoDim];
                    key.append(strKey);
				default:
					break;
			}
		}
		return key.toString();
	}

	public static String genUidKey(int keyLength){
	    if (keyLength > 36){
	        throw new IllegalArgumentException("key length is too Long");
        }
	    String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, keyLength);
    }

    private static void convertToLower(){
        String[] en = ENCRYPT_ELEMENTS[2];
        StringBuilder stringBuilder = new StringBuilder("");
        for (String s : en){
            stringBuilder.append("\"").append(s.toLowerCase()).append("\"")
                    .append(",");
        }
        String str = stringBuilder.toString();
        System.out.println(str.substring(0, str.length() - 1));
    }

    public static void main(String[] args){
        convertToLower();
    }
}
