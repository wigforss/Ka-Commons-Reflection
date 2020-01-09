package org.kasource.commons.reflection.filter;

import java.lang.reflect.Method;

/**
 * Method Filter.
 * <p>
 * Implement this interface to create a MethodFilter.
 *
 * @author rikardwi
 **/
@FunctionalInterface
public interface MethodFilter {

    boolean apply(Method method);

    default String describe() {
        return toString();
    }
}
