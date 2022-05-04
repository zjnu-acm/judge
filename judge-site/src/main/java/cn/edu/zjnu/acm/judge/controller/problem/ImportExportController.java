package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.service.ProblemExportService;
import cn.edu.zjnu.acm.judge.service.ProblemImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/problems", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
@Secured("ROLE_ADMIN")
@Slf4j
public class ImportExportController {

    private final ProblemImportService problemImportService;
    private final ProblemExportService problemExportService;

    @RequestMapping("/importProb")
    public boolean importProb(MultipartFile file) throws IOException, SAXException, ParserConfigurationException {
        return problemImportService.importProblem(file);
    }

    @RequestMapping("/exportProb")
    public void exportProb(HttpServletResponse response, Long[] ids) throws IOException {
        List<Problem> problemList = problemExportService.generateProblemEntity(ids);
        String xmlStr = problemExportService.generateXmlContent(problemList);
        problemExportService.exportXml(response, xmlStr);
    }

}
