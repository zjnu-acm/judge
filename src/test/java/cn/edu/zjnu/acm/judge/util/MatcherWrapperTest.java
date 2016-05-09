/*
 * Copyright (C) 2014 zhanhb
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.edu.zjnu.acm.judge.util;

import java.util.function.Function;
import java.util.regex.Pattern;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
public class MatcherWrapperTest {

    private void test(String pattern, String input, Function<MatcherWrapper, String> replaceFunction, String expectedResult) {
        String result = new MatcherWrapper(Pattern.compile(pattern), input).replaceAll(replaceFunction);
        assertEquals(expectedResult, result);
    }

    private void test2(String pattern, String input, Function<MatcherWrapper, String> replaceFunction, String expectedResult) {
        String result = new MatcherWrapper(Pattern.compile(pattern), input).replaceFirst(replaceFunction);
        assertEquals(expectedResult, result);
    }

    /**
     * Test of replaceAll method, of class MatcherWrapper.
     */
    @Test
    public void testReplaceAll_Function() {
        test("((?:he))", "(hezzhe)", matcher -> matcher.group(1) + "$1zhb", "(he$1zhbzzhe$1zhb)");
    }

    /**
     * Test of replaceFirst method, of class MatcherWrapper.
     */
    @Test
    public void testReplaceFirst_Function() {
        test2("((?:he))", "(hezzhe)", matcher -> matcher.group(1) + "$1zhb", "(he$1zhbzzhe)");
    }
}
