package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters constructors which parameter types is assignable to a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 * @author rikardwi
 **/
public class AssignableToParameterFilter implements MethodFilter, ConstructorFilter {

    private int paramterIndex;
    private Class<?> assignableToClass;
    private List<Class<?>> assignableTo = new ArrayList<>();

    public AssignableToParameterFilter(int paramterIndex, final Class<?> assignableToClass) {
        this.paramterIndex = paramterIndex;
        this.assignableToClass = assignableToClass;
    }

    public AssignableToParameterFilter(final Class<?>... assignableTo) {
        if (assignableTo != null) {
            this.assignableTo = Arrays.stream(assignableTo).collect(Collectors.toList());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean apply(Constructor constructor) {
        if (assignableToClass != null) {
            if ((constructor.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return constructor.getParameterTypes()[paramterIndex].isAssignableFrom(assignableToClass);
        } else if (assignableTo.size() == constructor.getParameterTypes().length) {
            for (int i = 0; i < assignableTo.size(); i++) {
                if (!constructor.getParameterTypes()[i].isAssignableFrom(assignableTo.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean apply(Method method) {
        if (assignableToClass != null) {
            if ((method.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return method.getParameterTypes()[paramterIndex].isAssignableFrom(assignableToClass);
        } else if (assignableTo.size() == method.getParameterTypes().length) {
            for (int i = 0; i < assignableTo.size(); i++) {
                if (!method.getParameterTypes()[i].isAssignableFrom(assignableTo.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public String describe() {
        if (assignableToClass != null) {
            return "parameter at index" + paramterIndex + " is super class of  " + assignableToClass.getName();
        } else {
            return "parameters is super classes of" + StringUtils.join(assignableTo, ", ");
        }
    }

}
