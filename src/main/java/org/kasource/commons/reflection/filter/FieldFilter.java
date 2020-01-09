package org.kasource.commons.reflection.filter;

import java.lang.reflect.Field;

/**
 * Field Filter.
 * <p>
 * Implement this interface to create a field filter.
 *
 * @author rikardwi
 **/
@FunctionalInterface
public interface FieldFilter {
    boolean apply(Field field);

    default String describe() {
        return toString();
    }
}
