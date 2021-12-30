package org.logfx;

import org.apache.tools.ant.util.StringUtils;

import java.util.List;

public class Filters {
    private List<String> includeKeywords;
    private List<String> excludeKeywords;

    private boolean caseSensitiveInInclude;
    private boolean caseSensitiveInExclude;

    private String fromTime; //format:  10:53:22.163
    private String toTime;

    public Filters(List<String> includeKeywords, List<String> excludeKeywords,
                   boolean caseSensitiveInInclude, boolean caseSensitiveInExclude,
                   String fromTime, String toTime) {
        this.includeKeywords = includeKeywords;
        this.excludeKeywords = excludeKeywords;
        this.caseSensitiveInInclude = caseSensitiveInInclude;
        this.caseSensitiveInExclude = caseSensitiveInExclude;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public List<String> getIncludeKeywords() {
        return includeKeywords;
    }

    public List<String> getExcludeKeywords() {
        return excludeKeywords;
    }

    public boolean isCaseSensitiveInInclude() {
        return caseSensitiveInInclude;
    }

    public boolean isCaseSensitiveInExclude() {
        return caseSensitiveInExclude;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }
}
