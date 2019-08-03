package com.scd.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

public class CertificateUtil {
	
	public static final String keystore_password = "shootercheng";//秘钥库密码
	
	public static final String alias = "chengdu";
	public static final String ca_password = "chengdu";//证书密码
	
	public static final String KEY_STORE = "D:/Others/BOOK/certificate/sunchengdu.keystore";
	
	public static final String CER_PATH = "D:/Others/BOOK/certificate/chengdu.cer";
	
	public static byte[] encryptByCert(String input, String cerPath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		InputStream inputStream = new FileInputStream(cerPath);
		X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
		inputStream.close();
		Key pubKey = x509Certificate.getPublicKey();
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(input.getBytes("UTF-8"));
	}
	
	public static String decryptByPeivateKey(String storePath,String storePasswd,
			String alias,String cerpasswd, byte[] encryptInfo) throws Exception {
		InputStream inputStream = new FileInputStream(storePath);
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, storePasswd.toCharArray());
		
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cerpasswd.toCharArray());
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    byte[] decryByte = cipher.doFinal(encryptInfo);
		return new String(decryByte, "UTF-8");
	}

	public static void main(String[] args) throws Exception {		
		byte[] encrypt = encryptByCert("孙成都", CER_PATH);
		String decrypt = decryptByPeivateKey(KEY_STORE, keystore_password, alias, 
				ca_password, encrypt);
		System.out.println("decrypt str---"+decrypt);
	}

}
