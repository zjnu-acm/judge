package cn.edu.zjnu.acm.judge.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.thymeleaf.util.StringUtils;

@SpecialCall({
    "WEB-INF/templates/fragment/standing.html",
    "WEB-INF/templates/users/list.html"
})
public interface JudgeUtils {

    /**
     * required in WEB-INF/templates/fragment/standing.html
     *
     * @param seconds the time, in seconds
     * @return A string represents the specified seconds
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    @SpecialCall("WEB-INF/templates/fragment/standing.html")
    public static String formatTime(long seconds) {
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

    public static String escapeCompileInfo(String string) {
        if (StringUtils.isEmptyOrWhitespace(string)) {
            return "";
        }
        return string
                .replaceAll("\\w:[/\\\\](?:\\w+[/\\\\])+?(?i)(?=Main\\.)(?-i)", "");
    }

    public static String getReplyString(String string) {
        if (StringUtils.isEmptyOrWhitespace(string)) {
            return "";
        }
        return StringUtils.escapeXml(new BufferedReader(new StringReader(string)).lines()
                .filter(line -> !line.startsWith("> "))
                .collect(Collectors.joining("\n> ", "> ", "\n")));
    }

    public static String getHtmlFormattedString(String str) {
        if (StringUtils.isEmptyOrWhitespace(str)) {
            return "";
        }
        for (int i = 0, len = str.length(); i < len; ++i) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            }
            if (ch == '<') {
                return str;
            }
            break;
        }
        return str.replaceAll("(?:(?:\r\n)|\n|\r|<br\\s*/?>)++(?!\\s*(?:<|&lt;)p)", "<br />")
                .replaceAll("<(?=\\s|\\d)", "&lt;")
                .replaceAll("(?:<br />)+$", "")
                .replace("<br />", "<br />\n");
    }

    public static String formatTime(Instant a, Instant b) {
        return formatTime(ChronoUnit.SECONDS.between(a, b));
    }

    @SpecialCall("users/list.html")
    public static long[] sequence(long total, long current) {
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
