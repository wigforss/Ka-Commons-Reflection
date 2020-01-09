package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;

import org.kasource.commons.reflection.filter.ConstructorFilter;

/**
 * Filters constructors by evaluating two other constructor filters by OR:ing their result.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class OrConstructorFilter implements ConstructorFilter {

    private ConstructorFilter left;
    private ConstructorFilter right;

    public OrConstructorFilter(final ConstructorFilter left, final ConstructorFilter right) {
        this.left = left;
        this.right = right;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return left.apply(constructor) || right.apply(constructor);
    }

    @Override
    public String describe() {
        return left.describe() + " or " + right.describe();
    }

}
