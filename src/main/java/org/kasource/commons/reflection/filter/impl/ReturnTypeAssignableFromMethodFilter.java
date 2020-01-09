package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filter methods which return type extends/implements the assignableFromClass.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class ReturnTypeAssignableFromMethodFilter implements MethodFilter {

    private Class<?> assignableFromClass;

    public ReturnTypeAssignableFromMethodFilter(final Class<?> assignableFromClass) {
        this.assignableFromClass = assignableFromClass;
    }

    @Override
    public boolean apply(Method method) {
        return assignableFromClass.isAssignableFrom(method.getReturnType());
    }

    @Override
    public String describe() {
        return "return type extends " + assignableFromClass.getName();
    }

}
