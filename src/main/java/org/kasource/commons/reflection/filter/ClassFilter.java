package org.kasource.commons.reflection.filter;

/**
 * Filters classes.
 *
 * @author Rikard Wigforss
 **/
@FunctionalInterface
public interface ClassFilter {
    boolean apply(Class<?> clazz);

    default String describe() {
        return toString();
    }
}
