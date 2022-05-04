package cn.edu.zjnu.acm.judge.util.parser;

import cn.edu.zjnu.acm.judge.domain.Problem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class SAXUtils {

    public static List<Problem> parse(String path) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXHandler handler = new SAXHandler();
        saxParser.parse(path, handler);
        List<Problem> problemList = handler.getList();

        return problemList;
    }
}
