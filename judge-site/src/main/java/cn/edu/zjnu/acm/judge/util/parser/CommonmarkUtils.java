package cn.edu.zjnu.acm.judge.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.zjnu.acm.judge.domain.TestData;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import static cn.edu.zjnu.acm.judge.util.RegexUtils.matchCodeBlockStr;

@Slf4j
public class CommonmarkUtils {

    public static String mdToHtml(String mdString) {
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(mdString);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String htmlString = renderer.render(document);
        return htmlString;
    }

    public static String getInlineCodeStr (String mdStr) {
        List<String> codeBlockStrs = matchCodeBlockStr(mdStr);
        if (codeBlockStrs == null || codeBlockStrs.size() != 1){
            log.error("输入不符合规范");
            return null;
        }
        String codeBlockStr = codeBlockStrs.get(0);
        return codeBlockStr.substring(codeBlockStr.indexOf("\n") + 1, codeBlockStr.lastIndexOf("\n```"));
    }

    public static List<TestData> getTestCases (String mdInStr, String mdOutStr) {
        List<String> inCodeBlockStrs = matchCodeBlockStr(mdInStr);
        List<String> outCodeBlockStrs = matchCodeBlockStr(mdOutStr);
        if (inCodeBlockStrs == null || outCodeBlockStrs == null ||inCodeBlockStrs.size() != outCodeBlockStrs.size()){
            log.error("输入不符合规范");
            return null;
        }
        List<TestData> testCases = new ArrayList<>();
        int testCaseNum = inCodeBlockStrs.size();
        for(int i = 0; i < testCaseNum; i++){
            String inData = inCodeBlockStrs.get(i);
            String outData = outCodeBlockStrs.get(i);
            inData = inData.substring(inData.indexOf("\n") + 1, inData.lastIndexOf("\n```"));
            outData = outData.substring(outData.indexOf("\n") + 1, outData.lastIndexOf("\n```"));
            TestData testData = new TestData(inData,outData);
            testCases.add(testData);
        }
        return testCases;
    }

}
