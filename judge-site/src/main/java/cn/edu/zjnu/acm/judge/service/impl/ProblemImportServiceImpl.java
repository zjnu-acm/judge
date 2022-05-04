package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.domain.Image;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.TestData;
import cn.edu.zjnu.acm.judge.service.ProblemImportService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.parser.SAXUtils;
import com.github.zhanhb.ckfinder.connector.api.BasePathBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.zjnu.acm.judge.util.FileUtils.*;
import static cn.edu.zjnu.acm.judge.util.ImgUtils.imageToBase64;
import static cn.edu.zjnu.acm.judge.util.RegexUtils.*;
import static cn.edu.zjnu.acm.judge.util.parser.CommonmarkUtils.*;

@RequiredArgsConstructor
@Service("ProblemImportService")
@Slf4j
public class ProblemImportServiceImpl implements ProblemImportService {

    private final SystemService systemService;
    private final ProblemService problemService;
    private final BasePathBuilder basePathBuilder;

    public boolean importProblem(MultipartFile file) throws ParserConfigurationException, SAXException, IOException {
        List<Problem> problems = identifyProblems(file);
        if (problems != null) {
            for (Problem problem: problems) {
                saveProblem(problem);
            }
        }
        return true;
    }

    public List<Problem> identifyProblems(MultipartFile file) throws IOException, ParserConfigurationException, SAXException {
        String contentType = file.getContentType();
        if (contentType.contains("markdown")) {
            List<String> strings = readMultipartFile(file);
            return identifyMd(strings);
        } else if (contentType.contains("xml")) {
            Path tmpDirPath = systemService.getUploadDirectory();
            String tmpDirSrc = tmpDirPath.toAbsolutePath().toString();
            String tmpSrc = writeTmpFile(tmpDirSrc, file);
            List<Problem> problems = SAXUtils.parse(tmpSrc);
            if (!tmpSrc.isEmpty() && tmpSrc != null) {
                deleteTmpFile(tmpSrc);
            }
            return problems;
        }
        return null;
    }

    public void saveProblem(Problem problem) throws IOException {
        problemService.save(problem);
        Long id = problem.getId();
        List<TestData> testDataList = problem.getTestData();
        if(testDataList != null && testDataList.size() != 0){
            for (int idx = 0; idx < testDataList.size(); idx++) {
                Path dataDir = systemService.getDataDirectory(id);
                String dataDirSrc = dataDir.toAbsolutePath().toString();
                TestData testData = testDataList.get(idx);
                writeIOFile(dataDirSrc, idx, testData.getTestIn(), true);
                writeIOFile(dataDirSrc, idx, testData.getTestOut(), false);
            }
        }
        List<Image> images = problem.getImages();
        if (images != null && images.size() != 0) {
            for (int idx = 0; idx < images.size(); idx++) {
                Image image = images.get(idx);
                String imgSrc = image.getImgSrc();
                String base64Src = image.getBase64Src();
                String imgType = imgSrc.substring(imgSrc.lastIndexOf("."), imgSrc.length());
                String imgName = id + "_" + idx + imgType;
                Path uploadDir = systemService.getUploadDirectory();
                String uploadDirStr = uploadDir.toAbsolutePath().toString() + "/Images";
                writeImgFile(uploadDirStr, base64Src, imgName);
                String newImgSrc = basePathBuilder.getBaseUrl() + "Images/" + imgName;
                System.out.println(id + ", " + imgSrc + ", " + newImgSrc);
                problemService.updateImgUrl(id, imgSrc, newImgSrc);
            }
        }
    }

    public List<Problem> identifyMd(List<String> mdStringList) {
        List<String> stringList = new ArrayList<>();
        List<Problem> problems = new ArrayList<>();
        for (String tmpMd : mdStringList){
            String tmpHtml = mdToHtml(tmpMd);
            if (tmpHtml.equals("<hr />") || tmpHtml.contains("<hr")) {
                Problem problem = identifyMdProblem(stringList);
                if (problem != null) {
                    problems.add(problem);
                }
                stringList.clear();
                continue;
            }
            stringList.add(tmpMd);
        }
        Problem problem = identifyMdProblem(stringList);
        if (problem != null) {
            problems.add(problem);
        }
        return problems;
    }

    public Problem identifyMdProblem(List<String> mdStringList) {
        HashMap<String, String> problemMap = new HashMap<>();
        String[] problemAttributes = {"title", "timelimit", "memorylimit", "description", "sampleinput",
                "sampleoutput", "input", "output", "hint", "source", "testin", "testout"};
        boolean flag = false; // 当前是否为字段块内信息
        String curAttribute = "";
        String curValue = "";
        String mdImgStr = "";
        for (String str : mdStringList) {
            if (!flag && str.trim().length() > 0 && str.charAt(0) == '#') {
                boolean flagg = false;
                String curString = str.replaceAll("[^A-Za-z]","").toLowerCase();
                for (String attribute : problemAttributes) {
                    if (curString.contains(attribute)) {
                        flag = true;
                        flagg = true;
                        if (curAttribute != null && !curAttribute.isEmpty()) {
                            mdImgStr = mdImgStr + matchMdImgStr(curValue);
                            problemMap.put(curAttribute,curValue);
                            curValue = "";
                        }
                        curAttribute = attribute;
                        break;
                    }
                }
                if (flagg) continue;
            }
            if (!flag && (curAttribute == null || curAttribute.isEmpty())) {
                continue;
            }
            flag = false;
            if (curValue == "") {
                curValue = curValue + str;
            } else {
                curValue = curValue + "\n" + str;
            }
        }
        if(curAttribute != null && !curAttribute.isEmpty()){
            mdImgStr += matchMdImgStr(curValue);
            problemMap.put(curAttribute, curValue);
        }
        if (problemMap.isEmpty()) {
            return null;
        }
        List<String> imgUrls = matchImgUrls(mdImgStr);
        List<Image> httpImages = new ArrayList<>();
        for (String imgUrl : imgUrls) {
            if (imgUrl.startsWith("http")) {
                String base64Str = imageToBase64(imgUrl);
                httpImages.add(new Image(imgUrl, base64Str));
            }
        }
        Problem problem = mapToProblem(problemMap);
        problem.setImages(httpImages);
        return problem;
    }

    public Problem mapToProblem(@NonNull Map<String, String> problemMap){
        Problem newProblem = new Problem();
        newProblem.setTitle(problemMap.get("title").trim());
        Long timeLimit = Long.parseLong(problemMap.get("timelimit").replaceAll("[^0-9]",""));
        newProblem.setTimeLimit(timeLimit);
        Long memoryLimit = Long.parseLong(problemMap.get("memorylimit").replaceAll("[^0-9]",""));
        newProblem.setMemoryLimit(memoryLimit);
        newProblem.setDescription(mdToHtml(problemMap.get("description")));
        newProblem.setInput(mdToHtml(problemMap.get("input")));
        newProblem.setOutput(mdToHtml(problemMap.get("output")));
        newProblem.setSampleInput(getInlineCodeStr(problemMap.get("sampleinput")));
        newProblem.setSampleOutput(getInlineCodeStr(problemMap.get("sampleoutput")));
        newProblem.setHint(mdToHtml(problemMap.get("hint")));
        newProblem.setSource(mdToHtml(problemMap.get("source")));
        newProblem.setTestData(getTestCases(problemMap.get("testin"),problemMap.get("testout")));
        newProblem.setDisabled(false);

        System.out.println("******** begin *********");
        System.out.println(newProblem.getTitle());
        System.out.println(newProblem.getTestData());
        System.out.println("******** end *********");

        return newProblem;
    };

}
