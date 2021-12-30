package org.logfx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class KeyWordParseUtil {
    private static final Pattern pattern = Pattern.compile("\"+?(.+?)\"+?|(\\S+)");

    public static List<String> parse(String line) {
        Matcher matcher = pattern.matcher(line);

        List<String> keywords = new ArrayList<>();
        while (matcher.find()) {
            keywords.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }

        return keywords;
    }
}
