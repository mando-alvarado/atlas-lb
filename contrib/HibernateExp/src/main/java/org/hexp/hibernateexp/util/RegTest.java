package org.hexp.hibernateexp.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegTest {

    public static final Pattern backReferencePattern = Pattern.compile(".*([^\\\\]\\\\[0-9]+).*");

    // If you see 1 slash in front of a number then this is a back reference but more then one slash before
    // a number doesn't count since its being escaped.
    public static boolean isBackReference(String testStr) {
        Matcher m = backReferencePattern.matcher(testStr);
        if (m.find()) {
            return m.groupCount() >= 1;
        } else {
            return false;
        }
    }
}
