package cn.edu.zjnu.acm.judge.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    private final static String MARKDOWN_IMG_LINK_REGEXP = "!\\[[^\\]]*\\]\\((.*?)\\s*(\"(?:.*[^\"])\")?\\s*\\)";
    private final static String IMG_LINK_REGEXP = "(?<=\\().+?(?=\\))";
    private final static String MARKDOWN_CODE_BLOCK_REGEXP = "```([\\s\\S]*?)```";

    public static List<String> matchContents(String regexp, String str) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String matchMdImgStr(String mdStr) {
        Pattern pattern = Pattern.compile(MARKDOWN_IMG_LINK_REGEXP);
        Matcher matcher = pattern.matcher(mdStr);
        String mdImgStr = "";
        while (matcher.find()) {
            mdImgStr += matcher.group();
        }
        return mdImgStr;
    }

    public static List<String> matchImgUrls(String mdImgStr) {
        return matchContents(IMG_LINK_REGEXP, mdImgStr);
    }

    public static List<String> matchCodeBlockStr (String mdStr) {
        return matchContents(MARKDOWN_CODE_BLOCK_REGEXP, mdStr);
    }

}
