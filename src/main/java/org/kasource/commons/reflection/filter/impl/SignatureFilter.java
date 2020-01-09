package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters methods which parameters matches a specific signature.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class SignatureFilter implements MethodFilter, ConstructorFilter {

    private Class<?>[] params;
    private Class<?> returnType;

    public SignatureFilter(final Class<?>... params) {
        this.params = new Class<?>[params.length];
        System.arraycopy(params, 0, this.params, 0, params.length);
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean apply(Method method) {
        boolean returnTypeCheck = true;
        if (returnType != null) {
            returnTypeCheck = method.getReturnType().equals(returnType);
        }
        return returnTypeCheck && Arrays.equals(method.getParameterTypes(), params);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return Arrays.equals(constructor.getParameterTypes(), params);
    }


    @Override
    public String describe() {
        return "with the parameters of type(s) (" + StringUtils.join(params, ", ") + ")";
    }
}
