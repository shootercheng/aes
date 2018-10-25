package com.scd.aes;

import com.scd.util.FileUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengdu
 * @date 2018/10/22.
 */
public class AesUtil {

    private static final String UTF8ENCODE = "utf-8";

    /**使用AES对字符串加密
     * @param str utf8编码的字符串
     * @param key 密钥（16字节）
     * @return 加密结果
     * @throws Exception
     */
    public static byte[] aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes(UTF8ENCODE));
        return  bytes;
    }
    /**使用AES对数据解密
     * @param bytes utf8编码的二进制数据
     * @param key 密钥（16字节）
     * @return 解密结果
     * @throws Exception
     */
    public static String aesDecrypt(byte[] bytes, String key) throws Exception {
        if (bytes == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(UTF8ENCODE), "AES"));
        bytes = cipher.doFinal(bytes);
        return new String(bytes, UTF8ENCODE);
    }

    /**
     * 加密多个文件输出到指定路径
     * @param configpath  配置文件路径
     * @param key   秘钥
     * @param encryOutPath  加密输出路径
     * @param originPath    需要截取路径, 文件前缀需一样，不一样需要处理 截取路径 D:/Code/IdeaProject/
     * @throws Exception
     */
    public static void encryPtByfilePath(String configpath, String key, String encryOutPath, String originPath) throws Exception{
        List<String> filepaths = FileUtil.getListPathByfile(configpath);
//        String encryOutPath = "C:/Users/chengdu/Desktop/encryptout/";
//        //需要截取的路径
        for(String filepath : filepaths) {
            //读取源文件，加密成Byte[] 数组
            String filecontent = FileUtil.readFileAll(filepath, UTF8ENCODE);
            byte[] aesByteArray = aesEncrypt(filecontent, key);
            //1.创建加密文件输出文件夹
            filepath = filepath.replace("\\", "/");
            filepath = filepath.substring(originPath.length());
            System.out.println(filepath);
            int lastIndex = filepath.lastIndexOf("/");
            String filedir = filepath.substring(0, lastIndex + 1);
            String filename = filepath.substring(lastIndex + 1);
            String outencrydir = encryOutPath + filedir;
            File fileencrydir = new File(outencrydir);
            if (!fileencrydir.exists()) {
                fileencrydir.mkdirs();
            }
            //2.创建加密输出文件
            String outencrypath = outencrydir + filename;
            File fileencryf = new File(outencrypath);
            if (!fileencryf.exists()) {
                fileencryf.createNewFile();
            }
            FileUtil.writeByteArraytoFile(outencrypath, aesByteArray, false);
        }
    }

    public static void decryPtFile(String encryOutPath, String decryptPath, String key) throws Exception{
//         String encryOutPath = "C:/Users/chengdu/Desktop/encryptout/";
//         String decryptPath = "C:/Users/chengdu/Desktop/decryptout/";
         List<String> filepaths = new ArrayList<String>();
         FileUtil.getFilePath(encryOutPath, filepaths);
//         System.out.println(filepaths);
//         System.out.println(filepaths.size());
         for(String encryptpath : filepaths){
             byte[] bytes = FileUtil.readByteArrayByfpath(encryptpath);
             String decyptrs = aesDecrypt(bytes, key);
             String filepath = encryptpath.substring(encryOutPath.length());
             filepath = filepath.replace("\\","/");
             int lastindex = filepath.lastIndexOf("/");
             String filedir = filepath.substring(0, lastindex);
             String filename = filepath.substring(lastindex);
             //创建解密输出文件夹
             String decryptOut = decryptPath + filedir;
             File dfile = new File(decryptOut);
             if(!dfile.exists()){
                 dfile.mkdirs();
             }
             //创建解密文件
             String dfilepath = decryptOut + filename;
             File dptFile = new File(dfilepath);
             if(!dptFile.exists()){
                 dptFile.createNewFile();
             }
             FileUtil.writeStrtoFile(dfilepath, decyptrs, false);
         }
    }

    public static void encryFileBydir(String sourcedir, String key, String encryOutPath) throws Exception{
        List<String> filelist = new ArrayList<String>();
        FileUtil.getFilePath(sourcedir, filelist);
        for(String filepath : filelist){
            //读取源文件，加密成Byte[] 数组
            String filecontent = FileUtil.readFileAll(filepath, UTF8ENCODE);
            byte[] aesByteArray = aesEncrypt(filecontent, key);
            filepath = filepath.substring(sourcedir.length());
            String outpath = encryOutPath + filepath;
            FileUtil.createNewDirFile(outpath);
            FileUtil.writeByteArraytoFile(outpath, aesByteArray, false);
        }
    }

    public static void decryFileByoutdir(String encryOutPath, String key, String decryOutPath) throws Exception{
        List<String> ecryfilelist = new ArrayList<String>();
        FileUtil.getFilePath(encryOutPath, ecryfilelist);
        for(String filepath : ecryfilelist){
            //解密
            byte[] encryArray = FileUtil.readByteArrayByfpath(filepath);
            String decryptcontent = aesDecrypt(encryArray, key);
            filepath = filepath.substring(encryOutPath.length());
            String outpath = decryOutPath + filepath;
            FileUtil.createNewDirFile(outpath);
            FileUtil.writeStrtoFile(outpath, decryptcontent, false);
        }
    }



    public static void main(String[] args) throws Exception {
        String userdir = System.getProperty("user.dir");
        String encrypth = userdir + File.separator + "encry"+ File.separator +"encrypath.txt";
        //注意 中文密钥受JDK版本影响，也不知道为啥？？
        String key = "中文啊XX";
//        File file = new File(encrypth);
//        if(!file.exists()){
//            throw new RuntimeException("----file not exists----" + encrypth);
//        }
        String encryOutPath = "C:/Users/chengdu/Desktop/encryptout/";
        String decryptPath = "C:/Users/chengdu/Desktop/decryptout/";
//        //decryPtFile(encryOutPath, decryptPath, key);
//        encryPtByfilePath(encrypth, key, encryOutPath, originPath);
          String dir = "E:/Java/Blog/";
          encryFileBydir(dir, key, encryOutPath);
//        decryFileByoutdir(encryOutPath, key,decryptPath);
    }
}
