package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.FieldFilter;

/**
 * Filters fields by evaluating two other field filters and OR:ing their result.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class OrFieldFilter implements FieldFilter {

    private FieldFilter left;
    private FieldFilter right;

    public OrFieldFilter(final FieldFilter left, final FieldFilter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean apply(Field field) {
        return left.apply(field) || right.apply(field);
    }

    @Override
    public String describe() {
        return left.describe() + " or " + right.describe();
    }
}
