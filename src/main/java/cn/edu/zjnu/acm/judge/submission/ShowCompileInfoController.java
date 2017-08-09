package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShowCompileInfoController {

    @Autowired
    private SubmissionMapper submissionMapper;

    @GetMapping("/showcompileinfo")
    public String showCompileInfo(@RequestParam("solution_id") long submissionId, Model model) {
        String compileInfo = submissionMapper.findCompileInfoById(submissionId);

        if (compileInfo == null) {
            throw new MessageException("No such solution", HttpStatus.NOT_FOUND);
        }

        model.addAttribute("compileInfo", JudgeUtils.escapeCompileInfo(compileInfo));
        return "showcompileinfo";
    }
}
