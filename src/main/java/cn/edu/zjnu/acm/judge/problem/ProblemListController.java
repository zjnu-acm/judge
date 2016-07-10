package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPerferenceMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ProblemListController {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserPerferenceMapper userPerferenceMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @RequestMapping(value = {"/problemlist", "/problems"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String problemlist(HttpServletRequest request, @RequestParam(value = "orderby", defaultValue = "") final String orderby,
            @RequestParam(value = "volume", required = false) Long volumeOptional,
            Locale locale) {
        String currentUserId = UserDetailService.getCurrentUserId(request).orElse(null);
        boolean logined = currentUserId != null;
        long volume;
        if (logined) {
            if (volumeOptional != null) {
                userPerferenceMapper.setVolumn(currentUserId, volumeOptional);
                volume = volumeOptional;
            } else {
                volume = userPerferenceMapper.getVolume(currentUserId);
            }
        } else if (volumeOptional != null) {
            volume = volumeOptional;
        } else {
            volume = 1;
        }
        Comparator<Problem> comparator;

        switch (orderby) {
            case "ratio":
                comparator = Comparator.comparingInt(Problem::getRatio);
                break;
            case "accepted":
                comparator = Comparator.comparingLong(Problem::getAccepted);
                break;
            case "submit":
                comparator = Comparator.comparingLong(Problem::getSubmit);
                break;
            case "difficulty":
                comparator = Comparator.comparingInt(Problem::getDifficulty);
                break;
            case "title":
                comparator = Comparator.comparing(Problem::getTitle, Collator.getInstance(locale));
                break;
            default:
                comparator = Comparator.comparingLong(Problem::getId);
                break;
        }
        long start = 900 + volume * 100L;
        long end = 999 + volume * 100;
        long totalVolume = (problemMapper.nextId() - 1001) / 100 + 1;

        List<Problem> problems = problemMapper.findAllByDisabledFalse(currentUserId, start, end, locale.getLanguage());
        problems = problems.stream()
                .filter(problem -> Optional.ofNullable(problem.getContest())
                        .map(contestMapper::findOne)
                        .map(contest -> {
                            if (contest.isEnded()) {
                                problemMapper.setContest(problem.getId(), null);
                            }
                            return contest.isStarted();
                        }).orElse(true))
                .sorted(comparator)
                .collect(Collectors.toList());

        request.setAttribute("volume", volume);
        request.setAttribute("totalVolume", totalVolume);
        request.setAttribute("problems", problems);

        return "problems/list";
    }

}
