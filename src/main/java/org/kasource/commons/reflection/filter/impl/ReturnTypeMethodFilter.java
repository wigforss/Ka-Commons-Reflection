package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters methods which return type is of a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class ReturnTypeMethodFilter implements MethodFilter {

    private Class<?> returnType;

    public ReturnTypeMethodFilter(final Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean apply(Method method) {
        return method.getReturnType().equals(returnType);
    }

    @Override
    public String describe() {
        return "return type is " + returnType.getName();
    }
}
