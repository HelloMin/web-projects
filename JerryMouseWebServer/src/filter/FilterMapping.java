package filter;

import jakarta.servlet.Filter;

import java.util.regex.Pattern;

public class FilterMapping {
    final Pattern pattern;
    final Filter filter;

    public FilterMapping(String urlPattern, Filter filter) {
        this.pattern = buildPattern(urlPattern);
        this.filter = filter;
    }
}
