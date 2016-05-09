package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.mapper.UserPerferenceMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubmitPageController {

    @Autowired
    private UserPerferenceMapper userPerferenceMapper;

    @RequestMapping(value = "/submitpage", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String submitpage(HttpServletRequest request,
            @RequestParam(value = "problem_id", defaultValue = "0") long problemId,
            @RequestParam(value = "contest_id", required = false) Long contestId) {
        UserDetailService.requireLoginned(request);
        request.setAttribute("contestId", contestId);
        request.setAttribute("problemId", problemId);
        request.setAttribute("languages", LanguageFactory.getLanguages().values());
        String user = UserDetailService.getCurrentUserId(request).orElse(null);
        int languageId = userPerferenceMapper.getLanguage(user);
        request.setAttribute("languageId", languageId);
        return "submit";
    }
}
