package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * A filter which negates the result form another method filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NegationMethodFilter implements MethodFilter {

    private MethodFilter filter;

    public NegationMethodFilter(final MethodFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean apply(Method method) {
        return !filter.apply(method);
    }

    @Override
    public String describe() {
        return "not " + filter.describe();
    }
}
