package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;

import org.kasource.commons.reflection.filter.ConstructorFilter;


/**
 * A filter which negates the result form another constructor filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NegationConstructorFilter implements ConstructorFilter {

    private ConstructorFilter filter;

    public NegationConstructorFilter(final ConstructorFilter filter) {
        this.filter = filter;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return !filter.apply(constructor);
    }

    @Override
    public String describe() {
        return "not " + filter.describe();
    }
}
