package cn.edu.zjnu.acm.judge.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.util.StringUtils;
import org.unbescape.html.HtmlEscape;

@SpecialCall({"fragment/standing", "users/list"})
public interface JudgeUtils {

    /**
     * required in fragment/standing
     *
     * @param seconds the time, in seconds
     * @return A string represents the specified seconds
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    @SpecialCall("fragment/standing")
    static String formatTime(long seconds) {
        boolean neg = false;
        if (seconds < 0) {
            neg = true;
            seconds = -seconds;
        }
        long h = (seconds >>> 4) / 225;      // h = seconds/3600, unsigned
        int ms = (int) (seconds - h * 3600);
        int m = ms / 60;
        int s = ms - m * 60;

        StringBuilder buf = new StringBuilder(8);
        if (neg) {
            buf.append('-');
        }
        if (h < 10) {
            buf.append('0');
        }
        buf.append(h).append(':');
        if (m < 10) {
            buf.append('0');
        }
        buf.append(m).append(':');
        if (s < 10) {
            buf.append('0');
        }
        return buf.append(s).toString();
    }

    static String getReplyString(String string) {
        if (!StringUtils.hasText(string)) {
            return "";
        }
        return HtmlEscape.escapeHtml4Xml(new BufferedReader(new StringReader(string)).lines()
                .filter(line -> !line.startsWith("> "))
                .collect(Collectors.joining("\n> ", "> ", "\n")));
    }

    static String formatTime(Instant a, Instant b) {
        return formatTime(ChronoUnit.SECONDS.between(a, b));
    }

    @SpecialCall("users/list")
    static long[] sequence(long total, long current) {
        if (total <= 0) {
            if (total == 0) {
                return new long[0];
            }
            throw new IllegalArgumentException();
        }
        LongStream stream;
        if (total < 20) {
            stream = LongStream.range(0, total);
        } else {
            LongStream a = LongStream.of(0, total - 1);
            LongStream b = LongStream.rangeClosed(Math.max(0, current - 9), Math.min(total - 1, current + 9));
            stream = LongStream.concat(a, b);
        }
        return stream.sorted().distinct().toArray();
    }

}
