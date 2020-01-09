package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.FieldFilter;


/**
 * Filters fields by negating the result of another field filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NegationFieldFilter implements FieldFilter {

    private FieldFilter filter;

    public NegationFieldFilter(final FieldFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean apply(Field field) {
        return !filter.apply(field);
    }

    @Override
    public String describe() {
        return "not " + filter.describe();
    }
}
