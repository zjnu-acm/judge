package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ShowCompileInfoController {

    @Autowired
    private SubmissionMapper submissionMapper;

    @RequestMapping(value = "/showcompileinfo", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showcompileinfo(@RequestParam("solution_id") long submissionId, Model model) {
        String compileInfo = submissionMapper.findCompileInfoById(submissionId);

        if (compileInfo == null) {
            throw new MessageException("No such solution");
        }

        model.addAttribute("compileInfo", JudgeUtils.escapeCompileInfo(compileInfo));
        return "showcompileinfo";
    }
}
