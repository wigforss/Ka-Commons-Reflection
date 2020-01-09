package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.ConstructorFilter;


/**
 * List of constructor Filters.
 * <p>
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 * @author rikardwi
 **/
public class AndConstructorFilter implements ConstructorFilter {
    private List<ConstructorFilter> filters = new ArrayList<>();

    public AndConstructorFilter(final ConstructorFilter... filters) {
        if (filters != null) {
            this.filters = Arrays.stream(filters).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return !filters.stream().filter(f -> !f.apply(constructor)).findFirst().isPresent();
    }

    @Override
    public String describe() {
        return filters.stream().map(f -> f.describe()).collect(Collectors.joining(" and "));
    }
}
