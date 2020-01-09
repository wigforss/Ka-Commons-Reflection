package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;


/**
 * Filters all classes which is annotated with an annotation which is annotated with a specific meta annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class MetaAnnotatedFilter implements ClassFilter, MethodFilter, FieldFilter, ConstructorFilter {

    private Class<? extends Annotation> inheritedAnnotation;

    public MetaAnnotatedFilter(final Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return passFilter(clazz.getDeclaredAnnotations());
    }

    @Override
    public boolean apply(Method method) {
        return passFilter(method.getDeclaredAnnotations());
    }

    @Override
    public boolean apply(Field field) {
        return passFilter(field.getDeclaredAnnotations());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return passFilter(constructor.getDeclaredAnnotations());

    }

    private boolean passFilter(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String describe() {
        return "is meta-annotated with annotation @" + inheritedAnnotation.getName();
    }

}
