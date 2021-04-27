package com.scd.clientserver;

import java.io.File;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scd.aes.Aes256Util;
import com.scd.util.Base64Util;
import com.scd.util.FileUtil;
import com.scd.util.KeyUtil;
import com.scd.util.RsaUtil;
import com.scd.model.ClientInfo;

/**
 * 客户端、服务端一次信息传输过程
 * 客户端已知服务端公钥，或者向
 * 服务端请求公钥
 * @author chengdu
 *
 */
public class ClientServerTest {
	
	public static final String KEY_PATH = "C:/Users/chengdu/Desktop/encrypt/RSA";
	
	public static final String SERVER_PATH = KEY_PATH + File.separator + "server";
	
	public static final String CLIENT_PATH = KEY_PATH + File.separator + "client";
	
	public static final String UTF8_ENCODE = "UTF-8";

	/**
	 * ##### 客户端机密数据发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String createClientData() throws Exception {
		ClientInfo clientInfo = new ClientInfo();
		
		// aeskey 使用 RSA加密	使用服务器端公钥
		// 产生随机 AES key
		String aesKey = KeyUtil.genRandomKey(32);
		clientInfo.setAesKey(encryptStr(aesKey));
		// 图片信息通过 AES 加密
		String imgPath = "C:/Users/chengdu/Desktop/encrypt/" + "1.JPG";
		clientInfo.setPicData(aesImg(imgPath, aesKey));
		clientInfo.setFileType(aesStr("JPG", aesKey));
		// 用户信息 AES 加密
		String input = "name:s成都";
		clientInfo.setUserInfo(aesStr(input, aesKey));
		// 获取客户端公钥,Base64编码之后发给服务器
		clientInfo.setClientPub(getClientPub());
		return String.valueOf(JSON.toJSON(clientInfo));
	}
	
	/**
	 * 服务器端公钥加密 aes key
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String encryptStr(String input) throws Exception {
		
		byte[] spubkey = FileUtil.readByteArrayByfpath(SERVER_PATH + File.separator + "public.conf");
		byte[] inputData = input.getBytes(UTF8_ENCODE);
		byte[] rsaEData = RsaUtil.encryptByPublicKey(inputData, spubkey);
		return Base64Util.encryptBASE64(rsaEData);
	}
	
	public static String aesImg(String imgPath, String aesKey) throws Exception {
    	String picBase64 = Base64Util.encryptImgBASE64(imgPath);
    	// 对 picBase64图片加密
    	byte[] aesPic = Aes256Util.aes256Encode(picBase64, aesKey);
    	// 转成Base64字符串传输
    	return  Base64Util.encryptBASE64(aesPic);
	}
	
	public static String aesStr(String input, String aesKey) throws Exception {
		byte[] aesStr = Aes256Util.aes256Encode(input, aesKey);
		return Base64Util.encryptBASE64(aesStr);
	}
	
	public static String getClientPub() throws Exception {
		byte[] cpubkey = FileUtil.readByteArrayByfpath(CLIENT_PATH + File.separator + "public.conf");
		return Base64Util.encryptBASE64(cpubkey);
	}
	
	// ######################################################
	/**
	 * 
	 * ####### 服务器解密部分
	 * 
	 * 
	 */
	
	
	public static void decryptServer(String clientData) throws Exception {
		JSON clientJson = (JSON) JSONObject.parse(clientData);
		ClientInfo clientInfo = JSON.toJavaObject(clientJson, ClientInfo.class);
		
		// 使用服务器私钥 RSA 解密  AES 的 key 值
		String key = getClientAesKey(clientInfo.getAesKey());
		System.out.println(key);
		
		// AES 解密还原图像, 先 Base64 解码， 然后 AES 解密
		String clientPic = Aes256Util.aes256Decode(Base64Util.decryptBASE64(clientInfo.getPicData()), key);
		String fileType = Aes256Util.aes256Decode(Base64Util.decryptBASE64(clientInfo.getFileType()), key);
		// 存储文件路径
		String path = "C:/Users/chengdu/Desktop/encrypt/" + System.currentTimeMillis() + "ClientSend" + "." + fileType;
		Base64Util.decryptBASE64Img(clientPic, path);
		
		// AES 解密用户信息   先Base64 解码， 然后 AES 解密
		String userInfo = Aes256Util.aes256Decode(Base64Util.decryptBASE64(clientInfo.getUserInfo()), key);
		System.out.println(userInfo);
		
		// 客户端公钥
		System.out.println(clientInfo.getClientPub());
		
	}
	
	public static String getClientAesKey(String encryAesKey) throws Exception {
		// 先使用 Base64 解码
		byte[] byAeskey = Base64Util.decryptBASE64(encryAesKey);
		// 服务器私钥
		byte[] spriKey = FileUtil.readByteArrayByfpath(SERVER_PATH + File.separator + "private.conf");
		byte[] byAeKey = RsaUtil.decryptByPrivateKey(byAeskey, spriKey);
		// 保持与客户端字符集编码格式一致
		return new String(byAeKey, UTF8_ENCODE);
	}
	
	
	public static void main(String[] args) throws Exception {
        String clientInfo = createClientData();
        decryptServer(clientInfo);
	}

}

