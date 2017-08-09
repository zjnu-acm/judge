package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProblemListController {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @GetMapping({"/problemlist", "/problems"})
    public String problemList(Model model,
            @RequestParam(value = "orderby", defaultValue = "") final String orderby,
            @RequestParam(value = "volume", required = false) Long volumeOptional,
            Locale locale,
            Authentication authentication) {
        String currentUserId = authentication != null ? authentication.getName() : null;
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
        Map<Long, Contest> hashMap = new HashMap<>(4);
        problems = problems.stream()
                .filter(problem -> Optional.ofNullable(problem.getContest())
                .map(contestId -> hashMap.computeIfAbsent(contestId, contestMapper::findOne))
                .map(contest -> {
                    if (contest.isEnded()) {
                        problemMapper.setContest(problem.getId(), null);
                    }
                    return contest.isStarted();
                }).orElse(true))
                .sorted(comparator)
                .collect(Collectors.toList());

        model.addAttribute("volume", volume);
        model.addAttribute("totalVolume", totalVolume);
        model.addAttribute("problems", problems);

        return "problems/list";
    }

}
