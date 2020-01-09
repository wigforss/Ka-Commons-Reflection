package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters constructors which has a specific number of parameters.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NumberOfParametersFilter implements MethodFilter, ConstructorFilter {

    private int numberOfParameters;

    public NumberOfParametersFilter(int numberOfParameters) {
        this.numberOfParameters = numberOfParameters;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return constructor.getParameterTypes().length == numberOfParameters;
    }

    @Override
    public boolean apply(Method method) {
        return method.getParameterTypes().length == numberOfParameters;
    }


    @Override
    public String describe() {
        return "number of parameters is " + numberOfParameters;
    }

}
