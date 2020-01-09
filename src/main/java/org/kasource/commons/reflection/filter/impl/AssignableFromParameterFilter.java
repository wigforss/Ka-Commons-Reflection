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
 * Filters constructors which parameter types is assignable from a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class AssignableFromParameterFilter implements MethodFilter, ConstructorFilter {

    private int paramterIndex;
    private Class<?> assignableFromClass;
    private List<Class<?>> assignableFrom = new ArrayList<>();

    public AssignableFromParameterFilter(int paramterIndex, final Class<?> assignableFromClass) {
        this.paramterIndex = paramterIndex;
        this.assignableFromClass = assignableFromClass;
    }

    public AssignableFromParameterFilter(final Class<?>... assignableFrom) {
        if (assignableFrom != null) {
            this.assignableFrom = Arrays.stream(assignableFrom).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        if (assignableFromClass != null) {
            if ((constructor.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return assignableFromClass.isAssignableFrom(constructor.getParameterTypes()[paramterIndex]);
        } else if (assignableFrom.size() == constructor.getParameterTypes().length) {
            for (int i = 0; i < assignableFrom.size(); i++) {
                if (!assignableFrom.get(i).isAssignableFrom(constructor.getParameterTypes()[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean apply(Method method) {
        if (assignableFromClass != null) {
            if ((method.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return assignableFromClass.isAssignableFrom(method.getParameterTypes()[paramterIndex]);
        } else if (assignableFrom.size() == method.getParameterTypes().length) {
            for (int i = 0; i < assignableFrom.size(); i++) {
                if (!assignableFrom.get(i).isAssignableFrom(method.getParameterTypes()[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String describe() {
        if (assignableFromClass != null) {
            return "paremeter at index " + paramterIndex + " extends " + assignableFromClass.getName();
        } else {
            return "parameters extends" + StringUtils.join(assignableFrom, ", ");
        }
    }

}
