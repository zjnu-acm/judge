package cn.edu.zjnu.acm.judge.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Matcher.quoteReplacement;

@SuppressWarnings("FinalClass")
public final class MatcherWrapper implements MatchResult {

    public static MatcherWrapper matcher(String pattern, String input) {
        return new MatcherWrapper(Pattern.compile(pattern), input);
    }

    private final Matcher matcher;
    private final CharSequence text;
    private boolean locked;

    public MatcherWrapper(Pattern pattern, CharSequence input) {
        this.matcher = pattern.matcher(input);
        this.text = input;
    }

    public String replaceAll(String replacement) {
        checkLocked();
        return matcher.replaceAll(replacement);
    }

    public String replaceAll(Function<MatcherWrapper, String> replaceFunction) {
        checkLocked();
        Objects.requireNonNull(replaceFunction);
        reset();
        boolean result = find();
        if (result) {
            @SuppressWarnings("StringBufferWithoutInitialCapacity")
            StringBuffer sb = new StringBuffer();
            do {
                String replacement;
                lock();
                try {
                    replacement = Objects.requireNonNull(replaceFunction.apply(this));
                } finally {
                    unlock();
                }
                result = appendReplacement(sb, quoteReplacement(replacement)).find();
            } while (result);
            return appendTail(sb).toString();
        }
        return text.toString();
    }

    public String replaceFirst(Function<MatcherWrapper, String> replaceFunction) {
        Objects.requireNonNull(replaceFunction, "replaceFunction");
        checkLocked();
        reset();
        if (!find()) {
            return text.toString();
        }
        String replacement;
        lock();
        try {
            replacement = Objects.requireNonNull(replaceFunction.apply(this));
        } finally {
            unlock();
        }
        @SuppressWarnings("StringBufferWithoutInitialCapacity")
        StringBuffer sb = new StringBuffer();
        return appendReplacement(sb, quoteReplacement(replacement)).appendTail(sb).toString();
    }

    public String replaceFirst(String replacement) {
        checkLocked();
        return matcher.replaceFirst(replacement);
    }

    public Pattern pattern() {
        return matcher.pattern();
    }

    public MatcherWrapper reset() {
        checkLocked();
        matcher.reset();
        return this;
    }

    @Override
    public int start() {
        return matcher.start();
    }

    @Override
    public int start(int group) {
        return matcher.start(group);
    }

    public int start(String name) {
        return matcher.start(name);
    }

    @Override
    public int end() {
        return matcher.end();
    }

    @Override
    public int end(int group) {
        return matcher.end(group);
    }

    public int end(String name) {
        return matcher.end(name);
    }

    @Override
    public String group() {
        return matcher.group();
    }

    @Override
    public String group(int group) {
        return matcher.group(group);
    }

    public String group(String name) {
        return matcher.group(name);
    }

    @Override
    public int groupCount() {
        return matcher.groupCount();
    }

    public boolean matches() {
        checkLocked();
        return matcher.matches();
    }

    public boolean find() {
        checkLocked();
        return matcher.find();
    }

    public boolean find(int start) {
        checkLocked();
        return matcher.find(start);
    }

    public MatcherWrapper appendReplacement(StringBuffer sb, String replacement) {
        checkLocked();
        matcher.appendReplacement(sb, replacement);
        return this;
    }

    public StringBuffer appendTail(StringBuffer sb) {
        checkLocked();
        return matcher.appendTail(sb);
    }

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    private void checkLocked() {
        if (locked) {
            throw new IllegalStateException("Matcher Locked");
        }
    }

}
