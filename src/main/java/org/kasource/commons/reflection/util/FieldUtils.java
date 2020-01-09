package org.kasource.commons.reflection.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.FieldFilter;

public final class FieldUtils {

    private FieldUtils() {
    }


    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the clazz class.
     *
     * @param clazz  The class to inspect.
     * @param filter Filter to use.
     * @return All matching fields declared by the clazz class.
     **/
    public static Set<Field> getDeclaredFields(Class<?> clazz, FieldFilter filter) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> filter.apply(f))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class or any of its super classes.
     *
     * @param target Class to inspect.
     * @param filter Filter to use.
     *
     * @return All matching fields declared by the target class.
     **/
    @SuppressWarnings("PMD.AssignmentInOperand")
    public static Set<Field> getFields(Class<?> target, FieldFilter filter) {
        Class<?> clazz = target;
        Set<Field> fields = getDeclaredFields(clazz, filter);
        while ((clazz = clazz.getSuperclass()) != null) {
            fields.addAll(getDeclaredFields(clazz, filter));
        }
        return fields;
    }

}
