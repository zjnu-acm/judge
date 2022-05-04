package cn.edu.zjnu.acm.judge.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImgUtils {

    /**
     * 通过图片的url获取图片的base64字符串
     * @param imgUrl    图片url
     * @return    返回图片base64的字符串
     */
    public static String imageToBase64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try{
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=is.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            return Base64Utils.encode(outStream.toByteArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

    /**
     * imgFile 图片本地存储路径
     */
    public static String getImgFileToBase64(String imgFile) throws IOException {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] buffer = Files.readAllBytes(Paths.get(imgFile));
        return Base64.getEncoder().encodeToString(buffer);
    }

}
