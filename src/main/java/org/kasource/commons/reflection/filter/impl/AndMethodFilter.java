package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.MethodFilter;

/**
 * List of Method Filters.
 * <p>
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class AndMethodFilter implements MethodFilter {
    private List<MethodFilter> filters = new ArrayList<>();

    public AndMethodFilter(final MethodFilter... filters) {
        if (filters != null) {
            this.filters = Arrays.stream(filters).collect(Collectors.toList());
        }
    }

    @Override
    public boolean apply(Method method) {
        return !filters.stream()
                .filter(f -> !f.apply(method))
                .findFirst()
                .isPresent();

    }

    @Override
    public String describe() {
        return filters.stream().map(f -> f.describe()).collect(Collectors.joining(" and "));
    }
}
