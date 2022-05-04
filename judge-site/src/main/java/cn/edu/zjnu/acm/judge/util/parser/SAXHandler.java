package cn.edu.zjnu.acm.judge.util.parser;

import cn.edu.zjnu.acm.judge.domain.Image;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.TestData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXHandler extends DefaultHandler {

    private String flag = "";
    private String unit = "";
    private String testInStr = "";
    private Boolean isTestCase = false;
    private String imgStr = "";
    private Boolean isImg = false;
    private List<Problem> list = new ArrayList<>();
    private Problem problem = null;

    public List getList(){
        return this.list;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("item")) {
            problem = new Problem();
        } else if (qName.equals("img")) {
            isImg = true;
        }
        if (attributes.getLength() != 0){
            for (int i = 0; i < attributes.getLength(); i++){
                String attributesQName = attributes.getQName(i);
                String attributesValue = attributes.getValue(i);
                if (attributesQName.equals("unit")){
                    unit = attributesValue;
                }
            }
        }
        flag = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("item")){
            problem.setDisabled(false);
            list.add(problem);
        }
        flag = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String value = new String(ch, start, length).trim();
        if(flag.equals("title")) {
            problem.setTitle(value);
        } else if (flag.equals("time_limit")) {
            long timeLimit = Long.parseLong(value);
            // 时间默认以ms为单位
            if (unit != null && !unit.isEmpty()) {
                if (unit.equals("s")) {
                    timeLimit *= 1000;
                }
                unit = "";
            } else {
                timeLimit *= 1000;
            }
            problem.setTimeLimit(timeLimit);
        } else if (flag.equals("memory_limit")) {
            long memoryLimit = Long.parseLong(value);
            // 空间默认以K为单位
            if (unit != null && !unit.isEmpty()) {
                if (unit.equals("kb")) {
                    memoryLimit /= 8;
                }
                else if (unit.equals("mb")) {
                    memoryLimit *= 128;
                }
                unit = "";
            } else {
                memoryLimit *= 128;
            }
            problem.setMemoryLimit(memoryLimit);
        } else if (flag.equals("description")) {
            problem.setDescription(value);
        } else if (flag.equals("input")) {
            problem.setInput(value);
        } else if (flag.equals("output")) {
            problem.setOutput(value);
        } else if (flag.equals("sample_input")) {
            problem.setSampleInput(value);
        } else if (flag.equals("sample_output")) {
            problem.setSampleOutput(value);
        } else if (flag.equals("hint")) {
            problem.setHint(value);
        } else if (flag.equals("test_input")) {
            testInStr = value;
            isTestCase = true;
        } else if (flag.equals("test_output") && isTestCase) {
            List<TestData> testData = problem.getTestData();
            if (testData == null) {
                testData = new ArrayList<>();
            }
            testData.add(new TestData(testInStr, value));
            problem.setTestData(testData);
            isTestCase = false;
        } else if (flag.equals("src") && isImg) {
            imgStr = value;
        } else if (flag.equals("base64") && isImg) {
            List<Image> images = problem.getImages();
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(new Image(imgStr, value));
            problem.setImages(images);
            isImg = false;
        }
    }
}
