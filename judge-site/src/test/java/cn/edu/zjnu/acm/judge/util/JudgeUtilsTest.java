package cn.edu.zjnu.acm.judge.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
@Transactional
public class JudgeUtilsTest {

    /**
     * Test of formatTime method, of class JudgeUtils.
     */
    @Test
    public void testFormatTime() {
        log.info("formatTime");
        JudgeUtils instance = JudgeUtils.INSTANCE;
        assertThat(instance.formatTime(0)).isEqualTo("00:00:00");
        assertThat(instance.formatTime(3675)).isEqualTo("01:01:15");
        assertThat(instance.formatTime(-3675)).isEqualTo("-01:01:15");
        assertThat(instance.formatTime(-3540)).isEqualTo("-00:59:00");
        assertThat(instance.formatTime(-3599)).isEqualTo("-00:59:59");
        assertThat(instance.formatTime(Long.MAX_VALUE)).isEqualTo("2562047788015215:30:07");
        assertThat(instance.formatTime(Long.MIN_VALUE)).isEqualTo("-2562047788015215:30:08");
        for (int i = 0; i < 3600; ++i) {
            String result = instance.formatTime(i);
            assertThat(result).isEqualTo("00:" + i / 600 + i / 60 % 10 + ':' + i % 60 / 10 + i % 10);
        }
    }

}
