package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


public class AnnotatedFilter implements ClassFilter, MethodFilter, FieldFilter, ConstructorFilter {
    private Class<? extends Annotation> annotation;


    public AnnotatedFilter(final Class<? extends Annotation> annotation) {
        this.annotation = annotation;

    }

    @Override
    public boolean apply(Class<?> clazz) {
        return clazz.isAnnotationPresent(annotation);
    }

    @Override
    public boolean apply(Method method) {
        return method.isAnnotationPresent(annotation);
    }

    @Override
    public boolean apply(Field field) {
        return field.isAnnotationPresent(annotation);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return constructor.isAnnotationPresent(annotation);
    }


    @Override
    public String describe() {
        return "annotated with @" + annotation.getName();
    }
}
