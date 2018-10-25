package com.scd.util;

import com.scd.aes.AesUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @author chengdu
 */
public class ImgUtil {

    /**
     * 图片转化为字符串
     * @param imgpath
     * @return
     */
    public static String getImageStr(String imgpath)
    {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgpath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }


    //base64字符串转化成图片
    public static void generateImage(String imgstr, String imgPath) throws IOException
    {
        if (imgstr == null) {
            throw new RuntimeException("----img string is null----");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgstr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            out = new FileOutputStream(imgPath);
            out.write(b);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(out != null){
                out.close();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        String userdir = System.getProperty("user.dir");
        String sourceimg = userdir + File.separator + "sourcefile" + File.separator + "1.jpg";
        String img = getImageStr(sourceimg);
        String key = "1234567891234567";
        //图像字符串加密
        byte[] encryimg = AesUtil.aesEncrypt(img, key);
        //保存加密字符串至文件
        String outputpath = userdir + File.separator + "test" + File.separator + "img.jpg";
        FileUtil.writeByteArraytoFile(outputpath, encryimg, false);
        //读取加密串
        byte[] fileimg = FileUtil.readByteArrayByfpath(outputpath);
        String decrystr = AesUtil.aesDecrypt(fileimg, key);
        String imgpath = userdir + File.separator + "test" + File.separator + "decrypt.jpg";
        generateImage(decrystr,imgpath );
//        String imgdecry = AesUtil.aesDecrypt(encryimg, key);
//        System.out.println(img.length());
//        String outputpath = userdir + File.separator + "test" + File.separator + "2.jpg";
//        generateImage(imgdecry, outputpath);
    }
}
