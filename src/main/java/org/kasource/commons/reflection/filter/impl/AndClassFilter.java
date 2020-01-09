package org.kasource.commons.reflection.filter.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.ClassFilter;



/**
 * Composite filter that runs the filter list supplied in the constructor.
 * <p>
 * Use this class to combine filters.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 * @author rikard
 **/
public class AndClassFilter implements ClassFilter {

    private List<ClassFilter> filters = new ArrayList<>();

    public AndClassFilter(final ClassFilter... filters) {
        if (filters != null) {
           this.filters = Arrays.stream(filters).collect(Collectors.toList());
        }
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return !filters.stream().filter(f -> !f.apply(clazz)).findFirst().isPresent();
    }

    @Override
    public String describe() {
        return filters.stream().map(f -> f.describe()).collect(Collectors.joining(" and "));
    }
}
