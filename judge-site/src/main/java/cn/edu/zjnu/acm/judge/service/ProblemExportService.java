package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.Image;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.TestData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProblemExportService {

    List<Problem> generateProblemEntity(Long[] ids) throws IOException;

    List<TestData> findTestData(Long id) throws IOException;

    List<Image> findImages(Long id) throws IOException;

    void exportXml(HttpServletResponse response, String text);

    String generateXmlContent(List<Problem> problemList);

    String addXmlTag(String xmlStr, String tagName, String tagValue);

    String addXmlImgTag(String xmlStr, List<Image> imageList);

}
