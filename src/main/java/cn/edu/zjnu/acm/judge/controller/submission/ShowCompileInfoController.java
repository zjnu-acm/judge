package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, submissionId);
        }

        model.addAttribute("compileInfo", JudgeUtils.escapeCompileInfo(compileInfo));
        return "showcompileinfo";
    }
}
