package org.kasource.commons.reflection.filter;

import java.lang.reflect.Constructor;

/**
 * Filters Constructors.
 * <p>
 * Implement this interface to create a Constructor filter.
 *
 * @author rikardwi
 **/
@FunctionalInterface
public interface ConstructorFilter {
    @SuppressWarnings("rawtypes")
    boolean apply(Constructor constructor);

    default String describe() {
        return toString();
    }
}
