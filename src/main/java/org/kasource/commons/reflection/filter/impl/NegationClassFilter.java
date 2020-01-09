package org.kasource.commons.reflection.filter.impl;

import org.kasource.commons.reflection.filter.ClassFilter;


/**
 * Filters classes by negating the result of another class filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NegationClassFilter implements ClassFilter {

    private ClassFilter filter;

    public NegationClassFilter(final ClassFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return !filter.apply(clazz);
    }

    @Override
    public String describe() {
        return "not " + filter.describe();
    }

}
