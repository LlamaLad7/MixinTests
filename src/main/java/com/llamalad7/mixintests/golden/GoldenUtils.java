package com.llamalad7.mixintests.golden;

import org.apache.commons.lang3.StringUtils;

class GoldenUtils {
    public static String normalize(String text) {
        if (text == null) return "\n";

        int start = 0, end = text.length();
        while (start < end && text.charAt(start) <= ' ') start++;
        while (end > start && text.charAt(end - 1) <= ' ') end--;
        String trimmed = text.substring(start, end);

        String normalized = trimmed.replace("\r\n", "\n").replace('\r', '\n');

        String[] lines = StringUtils.splitPreserveAllTokens(normalized, '\n');
        for (int i = 0; i < lines.length; i++) {
            lines[i] = StringUtils.stripEnd(lines[i], null);
        }

        return String.join("\n", lines) + "\n";
    }
}
