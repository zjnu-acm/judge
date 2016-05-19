package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPerferenceMapper;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ShowSourceController {

    private static final ConcurrentHashMap<String, String> SHJS_CACHE = new ConcurrentHashMap<>(10);
    private static final String[] STYLES = {
        "acid", "berries-dark", "berries-light", "bipolar", "blacknblue",
        "bright", "contrast", "darkblue", "darkness", "desert",
        "dull", "easter", "emacs", "golden", "greenlcd",
        "ide-anjuta", "ide-codewarrior", "ide-devcpp", "ide-eclipse", "ide-kdev",
        "ide-msvcpp", "kwrite", "matlab", "navy", "nedit",
        "neon", "night", "pablo", "peachpuff", "print",
        "rand01", "style", "the", "typical", "vampire",
        "vim-dark", "vim", "whatis", "whitengrey", "zellner"
    };

    public static String findClass4SHJS(String lang) {
        return SHJS_CACHE.computeIfAbsent(lang, ShowSourceController::calc);
    }

    private static String calc(String lang) {
        String srcLang = " " + lang.toLowerCase() + " ";
        if ((srcLang.contains("c++")) || (srcLang.contains("cpp")) || (srcLang.contains("g++"))) {
            return "sh_cpp";
        } else if ((srcLang.contains(" c ")) || (srcLang.contains("gcc"))) {
            return "sh_c";
        } else if (srcLang.contains("c#")) {
            return "sh_csharp";
        } else if (srcLang.contains("java ") || srcLang.contains("jdk")) {
            return "sh_java";
        } else if ((srcLang.contains("pascal")) || (srcLang.contains("fpc"))) {
            return "sh_pascal";
        } else if (srcLang.contains("tcl")) {
            return "sh_tcl";
        } else if (srcLang.contains("scala")) {
            return "sh_scala";
        } else if (srcLang.contains("perl")) {
            return "sh_perl";
        } else if (srcLang.contains("python")) {
            return "sh_python";
        } else if (srcLang.contains("ruby")) {
            return "sh_ruby";
        } else if (srcLang.contains("php")) {
            return "sh_php";
        } else if (srcLang.contains("prolog")) {
            return "sh_prolog";
        } else if (srcLang.contains("javascript")) {
            return "sh_javascript";
        } else {
            return "sh_c";
        }
    }

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserPerferenceMapper userPerferenceMapper;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private LanguageFactory languageFactory;

    @RequestMapping(value = "/showsource", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showsource(HttpServletRequest request,
            @RequestParam("solution_id") long submissionId,
            @RequestParam(value = "style", required = false) Integer style) {
        UserDetailService.requireLoginned(request);
        Submission submission = submissionMapper.findOne(submissionId);

        if (submission == null) {
            throw new MessageException("No such solution");
        }
        String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        if (!submissionService.canView(request, submission)) {
            throw new MessageException("You have no permission to view the source.");
        }
        String language = languageFactory.getLanguage(submission.getLanguage()).getName();
        String sh = findClass4SHJS(language);
        if (style == null) {
            style = userPerferenceMapper.getStyle(userId);
        }
        int defaultStyle = 18;
        if ((style < 0) || (style >= STYLES.length)) {
            style = defaultStyle;
        }
        String source = submissionMapper.findSourceById(submissionId);

        request.setAttribute("submission", submission);
        if (submission.getContest() != null) {
            request.setAttribute("contestId", submission.getContest());
        }
        request.setAttribute("language", language);
        request.setAttribute("result", ResultType.getShowsourceString(submission.getScore()));
        request.setAttribute("styles", STYLES);
        request.setAttribute("style", style);
        request.setAttribute("source", source);
        request.setAttribute("sh", sh);
        request.setAttribute("styleName", STYLES[style]);
        return "submissions/source";
    }

}
