package cn.edu.zjnu.acm.judge.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Utils {

    /**
     * 字符串转图片
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str){
        return Base64.getDecoder().decode(replaceEnter(base64Str));
    }

    /**
     * 图片转字符串
     * @param image
     * @return
     */
    public static String encode(byte[] image){
        return Base64.getEncoder().encodeToString(image);
    }

    public static String encode(String uri){
        return Base64.getEncoder().encodeToString(uri.getBytes(StandardCharsets.UTF_8));
    }

    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

}
