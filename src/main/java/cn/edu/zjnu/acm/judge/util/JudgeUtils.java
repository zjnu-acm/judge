package cn.edu.zjnu.acm.judge.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.annotation.Nullable;
import org.springframework.util.StringUtils;
import org.unbescape.html.HtmlEscape;

@SpecialCall({"fragment/standing", "users/list"})
public enum JudgeUtils {
    INSTANCE;

    /**
     * required in fragment/standing
     *
     * @param seconds the time, in seconds
     * @return A string represents the specified seconds
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    @SpecialCall("fragment/standing")
    public String formatTime(long seconds) {
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

    public String getReplyString(@Nullable String string) {
        if (!StringUtils.hasText(string)) {
            return "";
        }
        return HtmlEscape.escapeHtml4Xml(new BufferedReader(new StringReader(string)).lines()
                .filter(line -> !line.startsWith("> "))
                .collect(Collectors.joining("\n> ", "> ", "\n")));
    }

    public String formatTime(Instant a, Instant b) {
        return formatTime(ChronoUnit.SECONDS.between(a, b));
    }

    @SpecialCall("users/list")
    public long[] sequence(long total, long current) {
        if (total <= 0) {
            if (total == 0) {
                return new long[0];
            }
            throw new IllegalArgumentException();
        }
        LongStream stream;
        int max = 15;
        if (total <= max) {
            stream = LongStream.range(0, total);
        } else {
            LongStream a = LongStream.of(0, total - 1);
            LongStream b;
            long left = max / 2, right = max - left - 1;

            if (current > total - right) {
                b = LongStream.range(total - max + 1, total);
            } else if (current < left) {
                b = LongStream.range(0, max - 1);
            } else {
                b = LongStream.range(Math.max(0, current - left + 1), Math.min(total, current + right));
            }
            stream = LongStream.concat(a, b);
        }
        return stream.sorted().distinct().toArray();
    }

}
