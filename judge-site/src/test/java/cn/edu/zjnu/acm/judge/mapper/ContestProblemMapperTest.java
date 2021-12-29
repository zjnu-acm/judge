package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.dto.Standing;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestProblemMapperTest {

    @Autowired
    private ContestProblemMapper instance;
    @Autowired
    private LocaleService localeService;
    private final Locale locale = Locale.SIMPLIFIED_CHINESE;

    /**
     * Test of getProblems method, of class ContestProblemMapper.
     */
    @Test
    public void testGetProblems() {
        log.info("getProblems");
        long contestId = 0L;
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, null, localeService.resolve(locale));
        assertThat(result).isEqualTo(expResult);
    }

    /**
     * Test of getProblems method, of class ContestProblemMapper.
     */
    @Test
    public void testGetProblemsNullLocale() {
        log.info("getProblems");
        long contestId = 0L;
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, null, null);
        assertThat(result).isEqualTo(expResult);
    }

    /**
     * Test of getProblems method, of class ContestProblemMapper.
     */
    @Test
    public void testGetUserProblems() {
        log.info("getProblems");
        long contestId = 0L;
        String userId = "'";
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, userId, localeService.resolve(locale));
        assertThat(result).isEqualTo(expResult);
    }

    /**
     * Test of standing method, of class ContestProblemMapper.
     */
    @Test
    public void testStanding() {
        log.info("standing");
        long contest = 1058L;
        List<Standing> result = instance.standing(contest);
        log.info("{}", result);
    }

    /**
     * Test of getProblem method, of class ContestProblemMapper.
     */
    @Test
    public void testGetProblem() {
        log.info("getProblem");
        long contestId = 0L;
        long problemOrder = 0L;
        Problem expResult = null;
        Problem result = instance.getProblem(contestId, problemOrder, locale.toLanguageTag());
        assertThat(result).isEqualTo(expResult);
    }

}
