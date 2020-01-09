package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.FieldFilter;


/**
 * FIlter fields which type passes a class filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class TypeClassFilter implements ClassFilter, FieldFilter {

    private ClassFilter classFilter;

    public TypeClassFilter(final ClassFilter filter) {
        this.classFilter = filter;
    }

    @Override
    public boolean apply(Field field) {
        return classFilter.apply(field.getType());
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return classFilter.apply(clazz);
    }

    @Override
    public String describe() {
        return "type matches class filter: " + classFilter.describe();
    }
}
