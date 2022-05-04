package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.Problem;
import com.sun.istack.internal.NotNull;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ProblemImportService {

    boolean importProblem(MultipartFile file) throws ParserConfigurationException, SAXException, IOException;

    List<Problem> identifyProblems(MultipartFile file) throws IOException, ParserConfigurationException, SAXException;

    void saveProblem(Problem problem) throws IOException ;

    List<Problem> identifyMd(List<String> mdStringList);

    Problem identifyMdProblem(List<String> mdStringList);

    Problem mapToProblem(@NotNull HashMap<String, String> problemMap);

}
