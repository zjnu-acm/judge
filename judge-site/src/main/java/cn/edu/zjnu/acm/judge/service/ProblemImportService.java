package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.Problem;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProblemImportService {

    boolean importProblem(MultipartFile file) throws ParserConfigurationException, SAXException, IOException;

    List<Problem> identifyProblems(MultipartFile file) throws IOException, ParserConfigurationException, SAXException;

    void saveProblem(Problem problem) throws IOException;

    List<Problem> identifyMd(List<String> mdStringList);

    Problem identifyMdProblem(List<String> mdStringList);

    Problem mapToProblem(@NonNull Map<String, String> problemMap);

}
