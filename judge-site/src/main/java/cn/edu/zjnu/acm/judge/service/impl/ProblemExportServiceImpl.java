package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.domain.Image;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.TestData;
import cn.edu.zjnu.acm.judge.service.ProblemExportService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import com.github.zhanhb.ckfinder.connector.api.BasePathBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.zjnu.acm.judge.util.FileUtils.readFile;
import static cn.edu.zjnu.acm.judge.util.ImgUtils.getImgFileToBase64;

@RequiredArgsConstructor
@Service("ProblemExportService")
@Slf4j
public class ProblemExportServiceImpl implements ProblemExportService {

    private final ProblemService problemService;
    private final SystemService systemService;
    private final BasePathBuilder basePathBuilder;

    @Override
    public List<Problem> generateProblemEntity(Long[] ids) throws IOException {
        List<Problem> problemList = new ArrayList<>();
        for (Long id: ids) {
            Problem problem = problemService.findOne(id);
            problem.setTestData(findTestData(id));
            problem.setImages(findImages(id));
            problemList.add(problem);
        }
        return problemList;
    }

    @Override
    public List<TestData> findTestData(Long id) throws IOException {
        Path dirPath = systemService.getDataDirectory(id);
        String dirUrl = dirPath.toAbsolutePath().toString();
        File dir = new File(dirUrl);
        if (!dir.exists()) {
            return null;
        }
        File[] fileArray = dir.listFiles();
        List<TestData> testDataList = new ArrayList<>();
        for(int i = 0; i < fileArray.length; i++) {
            if (!fileArray[i].isFile()) {
                continue;
            }
            String fileName = fileArray[i].getName();
            if (!fileName.toLowerCase().endsWith(".in")) {
                continue;
            }
            String inPath = fileArray[i].getAbsolutePath();
            String outPath = dirUrl + "/" + fileName.substring(0, fileName.length() - 3) + ".out";
            File out = new File(outPath);
            if (!out.exists()) {
                continue;
            }
            TestData testData = new TestData(readFile(inPath),readFile(outPath));
            testDataList.add(testData);
        }
        if (testDataList.size() == 0) {
            return null;
        }
        return testDataList;
    }

    @Override
    public List<Image> findImages(Long id) throws IOException {
        Path dirPath = systemService.getUploadDirectory();
        String dirUrl = dirPath.toAbsolutePath().toString() + "/Images";
        File dir = new File(dirUrl);
        File[] fileArray = dir.listFiles();
        List<Image> imageList = new ArrayList<>();
        for(int i = 0; i < fileArray.length; i++) {
            if (!fileArray[i].isFile()) {
                continue;
            }
            String fileName = fileArray[i].getName();
            String filePath = fileArray[i].getAbsolutePath();
            if (fileName.contains(Long.toString(id))) {
                String base64 = getImgFileToBase64(filePath);
                String imgSrc = basePathBuilder.getBaseUrl() + "Images/" + fileName;
                Image image = new Image(imgSrc, base64);
                imageList.add(image);
            }
        }
        if (imageList.size() == 0) {
            return null;
        }
        return imageList;
    }

    @Override
    public void exportXml(HttpServletResponse response, String text){
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/xml");
        response.addHeader("Content-Disposition","attachment;filename="
                + "fps-data.xml");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            log.error("导出文件文件出错 e:{}", e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                log.error("关闭流对象出错 e:{}", e);
            }
        }
    }

    @Override
    public String generateXmlContent(List<Problem> problemList) {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
                "<fps version=\"1.1\" url=\"http://code.google.com/p/freeproblemset/\">\n";
        for (Problem problem: problemList) {
            xmlStr = xmlStr + "<item>\n";
            xmlStr = addXmlTag(xmlStr, "title", problem.getTitle());
            xmlStr = addXmlTag(xmlStr, "time_limit", problem.getTimeLimit().toString());
            xmlStr = addXmlTag(xmlStr, "memory_limit", problem.getMemoryLimit().toString());
            xmlStr = addXmlImgTag(xmlStr, problem.getImages());
            xmlStr = addXmlTag(xmlStr, "description", problem.getDescription());
            xmlStr = addXmlTag(xmlStr, "input", problem.getInput());
            xmlStr = addXmlTag(xmlStr, "output", problem.getOutput());
            xmlStr = addXmlTag(xmlStr, "sample_input", problem.getSampleInput());
            xmlStr = addXmlTag(xmlStr, "sample_output", problem.getSampleOutput());
            List<TestData> testDataList = problem.getTestData();
            if (testDataList != null && testDataList.size()>0) {
                for (TestData testData: testDataList) {
                    xmlStr = addXmlTag(xmlStr, "test_input", testData.getTestIn());
                    xmlStr = addXmlTag(xmlStr, "test_output", testData.getTestOut());
                }
            }
            xmlStr = addXmlTag(xmlStr, "hint", problem.getHint());
            xmlStr = addXmlTag(xmlStr, "source", problem.getSource());
            xmlStr = xmlStr + "</item>\n";
        }
        xmlStr = xmlStr + "</fps>";
        return xmlStr;
    }


    @Override
    public String addXmlTag(String xmlStr, String tagName, String tagValue) {
        String tagStr = "<" + tagName;
        if (tagName.equals("time_limit")) {
            tagStr = tagStr + " unit=\"ms\"";
        } else if (tagName.equals("memory_limit")) {
            tagStr = tagStr + " unit=\"kb\"";
        }
        if (tagValue == null) {
            tagValue = "";
        }
        tagStr = tagStr + "><![CDATA["+ tagValue +"]]></" + tagName + ">\n";
        return xmlStr + tagStr;
    }

    @Override
    public String addXmlImgTag(String xmlStr, List<Image> imageList) {
        if (imageList == null || imageList.size() == 0) {
            return  xmlStr;
        }
        String tagStr = "";
        for (Image image: imageList) {
            tagStr = tagStr + "<img><src><![CDATA[" + image.getImgSrc() + "]]></src>" +
                    "<base64><![CDATA[" + image.getBase64Src() + "]]></base64></img>\n";
        }
        return xmlStr + tagStr;
    }

}
