package org.kasource.commons.reflection.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.filter.fields.FieldFilter;

public final class FieldUtils {
    
    private FieldUtils() { }
    
    
    
    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the clazz class.
     * 
     * @param clazz     The class to inspect.
     * @param filter    Filter to use.
     * 
     * @return All matching fields declared by the clazz class.
     **/
    public static Set<Field> getDeclaredFields(Class<?> clazz, FieldFilter filter) {
        Set<Field> fields = new HashSet<Field>();
        Field[] allFields = clazz.getDeclaredFields();
        for(Field field : allFields) {
            if(filter.passFilter(field)) {
                fields.add(field);
            }
        }
        return fields;
    }
    
    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class or any of its super classes.
     * 
     * @param filter    Filter to use.
     * 
     * @return All matching fields declared by the target class.
     **/
    public static Set<Field> getFields(Class<?> target, FieldFilter filter) {
        Class<?> clazz = target;
        Set<Field> fields = getDeclaredFields(clazz, filter);
        while((clazz = clazz.getSuperclass()) != null) {    
            fields.addAll(getDeclaredFields(clazz, filter));
        }
        return fields;
    }
    
}
