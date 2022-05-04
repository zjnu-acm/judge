package cn.edu.zjnu.acm.judge.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Utils {

    /**
     * 字符串转图片
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str){
        byte[] b = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            b = decoder.decodeBuffer(replaceEnter(base64Str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 图片转字符串
     * @param image
     * @return
     */
    public static String encode(byte[] image){
        BASE64Encoder decoder = new BASE64Encoder();
        return replaceEnter(decoder.encode(image));
    }

    public static String encode(String uri){
        BASE64Encoder encoder = new BASE64Encoder();
        return replaceEnter(encoder.encode(uri.getBytes()));
    }

    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

}
