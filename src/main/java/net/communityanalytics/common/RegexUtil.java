package net.communityanalytics.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    /**
     * Check if a string is a valid domain
     *
     * @param domain boolean
     * @return boolean
     */
    public static boolean isDomain(String domain) {
        String domain_regex = "^(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]$";
        if (domain.matches(domain_regex))
            return true;
        if (domain.contains(":"))
            return isDomain(domain.split(":")[0]);
        return false;
    }

    /**
     * @param message String
     * @return String
     */
    public static String extractIp(String message) {
        // Pattern 1
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group();
        }

        // Pattern 2
        pattern = Pattern.compile("\\b([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})\\b");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
