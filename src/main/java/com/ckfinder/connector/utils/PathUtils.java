/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.ckfinder.connector.utils;

import com.ckfinder.connector.configuration.Constants;
import java.util.regex.Pattern;

/**
 * Utility class used to change paths in connector.
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class PathUtils {

    /**
     * Escapes double slashes (//) and replaces backslashes characters (\) with
     * slashes (/). <br>
     * <strong>NOTE:</strong> This method preserves UNC paths.
     *
     * @param string string to escape
     * @return Escaped string, {@code null} or empty string.
     */
    public static String escape(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        final int prefixIndex = string.indexOf("://");
        String prefix = "";
        String suffix = string;
        if (prefixIndex > -1) {
            prefix = suffix.substring(0, prefixIndex + 3);
            suffix = suffix.substring(prefixIndex + 3);
        }
        suffix = suffix.replace('\\', '/');

        // preserve // at the beginning for UNC paths
        if (suffix.startsWith("//")) {
            suffix = "/" + suffix.replaceAll("/+", "/");
        } else {
            suffix = suffix.replaceAll("/+", "/");
        }
        return prefix.concat(suffix);
    }

    /**
     * Adds slash character at the end of String provided as parameter. The
     * slash character will not be added if parameter is empty string or ends
     * with slash.
     *
     * @param string string to add slash character to
     * @return String with slash character at the end, {@code null} or empty
     * string.
     */
    public static String addSlashToEnd(String string) {
        if (string != null && !string.endsWith("/")) {
            return string.concat("/");
        }
        return string;
    }

    /**
     * Adds slash character at the start of String provided as parameter. The
     * slash character will not be added if parameter is ULR or starts with
     * slash.
     *
     * @param string string to add slash character to
     * @return String with slash character at the beginning, {@code null} or
     * full URL.
     */
    public static String addSlashToBeginning(String string) {
        if (string == null || string.charAt(0) == '/'
                || Pattern.matches(Constants.URL_REGEX, string)) {
            return string;
        }
        return "/".concat(string);
    }

    /**
     * Removes slash from the start of string, provided as parameter if
     * necessary.
     *
     * @param string string to remove slash character from
     * @return String without slash character at the beginning, {@code null} or
     * empty string.
     */
    public static String removeSlashFromBeginning(String string) {
        if (string != null && string.startsWith("/")) {
            return string.substring(1, string.length());
        }
        return string;
    }

    /**
     * Removes slash character from the end of string, provided as parameter if
     * necessary.
     *
     * @param string string to remove slash character from
     * @return String without slash at the end, {@code null} or empty string.
     */
    public static String removeSlashFromEnd(String string) {
        if (string != null && string.endsWith("/")) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

}
