package com.scd.util;

import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RsaUtil {
	
	 //非对称密钥算法
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    
    public static final String UTF8_ENCODE = "UTF-8";
    
    private static final String BASE_PATH = "C:/Users/chengdu/Desktop/encrypt/RSA";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println(keyPair.getPublic());
        System.out.println(keyPair.getPrivate());
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }



    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }
    
    /**
     * 初始化客户端秘钥
     * @throws Exception
     */
    public static void initRsaKey() throws Exception {
		init("client");
		init("server");
    }
    
    public static void init(String type) throws Exception {
    	Map<String, Object> map = initKey();
		byte[] publicKey = getPublicKey(map);
		byte[] privateKey = getPrivateKey(map);
		// 客户端公钥、私钥匙文件，公钥可以给其它人
		String pbpath = BASE_PATH + File.separator + type + File.separator + "public.conf";
		String pripath = BASE_PATH + File.separator + type + File.separator + "private.conf";
		// 保存公钥、私钥
		FileUtil.createNewDirFile(pbpath);
		FileUtil.createNewDirFile(pripath);
		FileUtil.writeByteArraytoFile(pbpath, publicKey, false);
		FileUtil.writeByteArraytoFile(pripath, privateKey, false);
    }
    
    public static void test() throws Exception {
    	String pbpath = BASE_PATH + File.separator + "server" + File.separator + "public.conf";
		String pripath = BASE_PATH + File.separator + "server"+ File.separator + "private.conf";
		// 客户端使用服务器公钥加密信息
		byte[] pbkeybyte = FileUtil.readByteArrayByfpath(pbpath);
        String msg = "孙成都";
        // 使用公钥加密之后， 再使用 Base64 编码之后发送
        byte[] encryptstr = encryptByPublicKey(msg.getBytes("UTF-8"), pbkeybyte);
        String trsmsg = Base64Util.encryptBASE64(encryptstr);
        
        // 服务器接收到之后，使用Base64解码解密信息，再使用服务器私钥解密
        byte[] encrpByte = Base64Util.decryptBASE64(trsmsg);
        byte[] prikey = FileUtil.readByteArrayByfpath(pripath);
        String decrystr = new String(decryptByPrivateKey(encrpByte, prikey));
        System.out.println("decrypt str--------"+decrystr);
    }

	public static void main(String[] args) throws Exception {
//		initRsaKey();
		test();
//		System.out.println(Charset.defaultCharset());
//		String aa = "qq";
//		aa.getBytes(Charset.defaultCharset());
	}

}
