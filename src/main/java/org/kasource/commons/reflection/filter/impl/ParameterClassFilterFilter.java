package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters methods which parameters types passes a ClassFilter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class ParameterClassFilterFilter implements MethodFilter, ConstructorFilter {

    private int parameterIndex;
    private ClassFilter classFilter;
    private ClassFilter[] classFilters;

    public ParameterClassFilterFilter(final ClassFilter... filters) {
        this.classFilters = new ClassFilter[filters.length];
        System.arraycopy(filters, 0, this.classFilters, 0, filters.length);
    }

    public ParameterClassFilterFilter(int parameterIndex, final ClassFilter filter) {
        this.parameterIndex = parameterIndex;
        this.classFilter = filter;
    }

    @Override
    public boolean apply(Method method) {

        return passFilter(method.getParameterTypes());
    }

    @SuppressWarnings("rawTypes")
    @Override
    public boolean apply(Constructor constructor) {
        return passFilter(constructor.getParameterTypes());
    }

    private boolean passFilter(Class<?>... parameters) {
        if (classFilter != null) {
            if ((parameters.length - 1) < parameterIndex) {
                return false;
            }
            return classFilter.apply(parameters[parameterIndex]);
        } else if (classFilters != null) {
            if (classFilters.length != parameters.length) {
                return false;
            }
            for (int i = 0; i < classFilters.length; ++i) {
                Class<?> paramterType = parameters[i];
                if (!classFilters[i].apply(paramterType)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String describe() {
        if (classFilter != null) {
            return "parameter type at index " + parameterIndex + " matches class filter: " + classFilter.describe();
        } else {
            List<String> filterDescriptions = new ArrayList<>();
            for (ClassFilter filter : classFilters) {
                filterDescriptions.add(filter.describe());
            }
            return "parameter types matches filter: " + StringUtils.join(filterDescriptions.toArray(), ", ");
        }
    }
}
