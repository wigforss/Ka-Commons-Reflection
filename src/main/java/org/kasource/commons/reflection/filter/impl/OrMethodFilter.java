package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.MethodFilter;

/**
 * Filters methods by evaluating two other method filters by OR:ing their result.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class OrMethodFilter implements MethodFilter {

    private MethodFilter left;
    private MethodFilter right;
    private MethodFilter[] rest;

    /**
     * Constructs an Or filter based on at least two filters left and right, but may include
     * additional number of filters as the rest.
     *
     * @param left  Left side filter
     * @param right Right side filters
     * @param rest  Additional filters, may be empty.
     **/
    public OrMethodFilter(final MethodFilter left, final MethodFilter right, final MethodFilter... rest) {
        this.left = left;
        this.right = right;
        this.rest = new MethodFilter[rest.length];
        System.arraycopy(rest, 0, this.rest, 0, rest.length);
    }

    @Override
    public boolean apply(Method method) {
        boolean result = left.apply(method) || right.apply(method);
        for (MethodFilter filter : rest) {
            result |= filter.apply(method);
        }
        return result;
    }

    @Override
    public String describe() {
        return left.describe() + " or " + right.describe();
    }

}
