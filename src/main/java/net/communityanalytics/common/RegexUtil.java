package net.communityanalytics.common;

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
}
