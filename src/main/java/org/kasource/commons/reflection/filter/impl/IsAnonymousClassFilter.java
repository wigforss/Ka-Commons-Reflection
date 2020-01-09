package org.kasource.commons.reflection.filter.impl;


import org.kasource.commons.reflection.filter.ClassFilter;


/**
 * Filter that keeps only anonymous classes.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author Rikard Wigforss
 **/
public class IsAnonymousClassFilter implements ClassFilter {
    @Override
    public boolean apply(Class<?> clazz) {
        return clazz.isAnonymousClass();
    }

    @Override
    public String describe() {
        return "is an anonymous class";
    }
}
