package cn.edu.zjnu.acm.judge.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@Slf4j
@Transactional
public class JudgeUtilsTest {

    /**
     * Test of formatTime method, of class JudgeUtils.
     */
    @Test
    public void testFormatTime() {
        log.info("formatTime");
        assertEquals("00:00:00", JudgeUtils.formatTime(0));
        assertEquals("01:01:15", JudgeUtils.formatTime(3675));
        assertEquals("-01:01:15", JudgeUtils.formatTime(-3675));
        assertEquals("-00:59:00", JudgeUtils.formatTime(-3540));
        assertEquals("-00:59:59", JudgeUtils.formatTime(-3599));
        assertEquals("2562047788015215:30:07", JudgeUtils.formatTime(Long.MAX_VALUE));
        assertEquals("-2562047788015215:30:08", JudgeUtils.formatTime(Long.MIN_VALUE));
        for (int i = 0; i < 3600; ++i) {
            String result = JudgeUtils.formatTime(i);
            assertEquals("00:" + i / 600 + i / 60 % 10 + ':' + i % 60 / 10 + i % 10, result);
        }
    }

}
