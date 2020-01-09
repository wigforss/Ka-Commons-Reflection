package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.FieldFilter;



/**
 * List of Field Filters.
 * <p>
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class AndFieldFilter implements FieldFilter {

    private List<FieldFilter> filters = new ArrayList<>();


    public AndFieldFilter(final FieldFilter... filters) {
        if (filters != null) {
            this.filters = Arrays.stream(filters).collect(Collectors.toList());
        }
    }


    @Override
    public boolean apply(Field field) {
        return !filters.stream().filter(f -> !f.apply(field)).findFirst().isPresent();
    }

    @Override
    public String describe() {
        return filters.stream().map(f -> f.describe()).collect(Collectors.joining(" and "));
    }
}
