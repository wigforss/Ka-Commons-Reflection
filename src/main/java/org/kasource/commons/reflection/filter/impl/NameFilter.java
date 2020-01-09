package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;

/**
 * Filters classes which name matches a Regular Expression.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class NameFilter implements ClassFilter, MethodFilter, FieldFilter {

    private String nameRegExp;

    public NameFilter(final String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return clazz.getName().matches(nameRegExp);
    }

    @Override
    public boolean apply(Field field) {
        return field.getName().matches(nameRegExp);
    }

    @Override
    public boolean apply(Method method) {
        return method.getName().matches(nameRegExp);
    }

    @Override
    public String describe() {
        return "name matches regular expression " + nameRegExp;
    }

}
