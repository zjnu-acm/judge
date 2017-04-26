package cn.edu.zjnu.acm.judge.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.thymeleaf.util.StringUtils;

@SpecialCall("WEB-INF/templates/fragment/standing.html")
public class JudgeUtils {

    private static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * required in WEB-INF/templates/fragment/standing.html
     *
     * @param seconds the time, in seconds
     * @return A string represents the specified seconds
     */
    @SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "AssignmentToMethodParameter"})
    @SpecialCall("WEB-INF/templates/fragment/standing.html")
    public static String formatTime(long seconds) {
        boolean neg = false;
        if (seconds < 0) {
            neg = true;
            seconds = -seconds;
        }
        long h = (seconds >>> 4) / 225;      // h = seconds/3600, unsigned
        long h1 = h / 10;
        int h2 = (int) (h - h1 * 10);
        int ms = (int) (seconds - h * 3600);
        int m = ms / 60;
        int m1 = m / 10;
        int m2 = m - m1 * 10;
        int s = ms - m * 60;
        int s1 = s / 10;
        int s2 = s - s1 * 10;
        String tmp = Long.toString(h1);
        int tmpLen = tmp.length();

        int len = 0;
        char[] buf = new char[neg ? tmpLen + 8 : tmpLen + 7];
        if (neg) {
            buf[len++] = '-';
        }
        tmp.getChars(0, tmpLen, buf, len);
        len += tmpLen;
        char[] ds = digits;
        buf[len++] = ds[h2];
        buf[len++] = ':';
        buf[len++] = ds[m1];
        buf[len++] = ds[m2];
        buf[len++] = ':';
        buf[len++] = ds[s1];
        buf[len++] = ds[s2];
        return new String(buf);
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
    public static Collection<Number> sequence(long total, long current) {
        if (total <= 0) {
            if (total == 0) {
                return Collections.emptyList();
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
        return stream.boxed().collect(Collectors.toCollection(TreeSet::new));
    }

    private JudgeUtils() {
        throw new AssertionError();
    }

}
