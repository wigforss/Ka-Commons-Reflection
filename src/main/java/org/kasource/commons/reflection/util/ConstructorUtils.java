package org.kasource.commons.reflection.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.kasource.commons.reflection.filter.ConstructorFilter;

/**
 * Utility class for creating object instances using Reflection Based Construction.
 *
 * @author rikardwi
 **/
public final class ConstructorUtils {

    private ConstructorUtils() {
    }

    /**
     * Returns set of constructors that matches the filter parameter.
     *
     * @param target Class to get constructor for.
     * @param filter Filter to apply.
     * @param <T>    Type of constructor.
     *
     * @return constructors that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     **/
    @SuppressWarnings("unchecked")
    public static <T> Set<Constructor<T>> findConstructors(Class<T> target, ConstructorFilter filter) {
        return Arrays.stream((Constructor<T>[]) target.getDeclaredConstructors()).filter(c -> filter.apply(c)).collect(Collectors.toSet());
    }


    /**
     * Returns the first constructor found that matches the filter parameter.
     *
     * @param target Class to get constructor for.
     * @param filter Filter to apply.
     * @param <T>    Type of constructor.
     *
     * @return the first constructor found that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     *                                  or no constructor is found matching the filter.
     **/
    public static <T> Constructor<T> findFirstConstructor(Class<T> target, ConstructorFilter filter) {

        Set<Constructor<T>> cons = findConstructors(target, filter);
        if (cons.isEmpty()) {
            throw new IllegalArgumentException("No constructor found for " + target.getName() + " matching filter: " + filter.describe());
        }
        return cons.iterator().next();
    }




    /**
     * Creates and returns a new instance of class with name className, loading the class and using the default constructor.
     *
     * @param <T>       Type of instance
     * @param className Name of class
     * @param ofType    Type of class
     *
     * @return a new instance of class loaded from className.
     * @throws IllegalStateException if className could not be loaded or if that class does not have a default constructor
     *                               or if the loaded class is not of the supplied type (ofType).
     */
    public static <T> T newInstance(String className, Class<T> ofType) throws IllegalStateException {
        return newInstance(className, ofType, new Class<?>[]{}, new Object[]{});
    }

    /**
     * Returns a new object of <i>className</i>. The objected is casted to the <i>ofType</i>,
     * which is either super class or interface of the className class.
     *
     * @param className         Name of the class to instanciate an object of
     * @param ofType            An super class or interface of the className class.
     * @param constructorParams Constructor parameter types
     * @param constructorArgs   Constructor arguments to use when creating a new instance
     * @param <T>               Type of instance.
     *
     * @return A new instance of class with name className casted to the ofType class.
     * @throws IllegalStateException if className could not be loaded or if that class does not have a matching constructor
     *                               to the constructorParam or if the loaded class is not of the supplied type (ofType).
     **/
    @SuppressWarnings({"unchecked", "PMD.UseStringBufferForStringAppends"})
    public static <T> T newInstance(String className, Class<T> ofType, Class<?>[] constructorParams, Object... constructorArgs) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(constructorParams);
            return ofType.cast(constructor.newInstance(constructorArgs));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not create new instance of class " + className, e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Could not create new instance of class " + className + " using ";
            if (constructorParams.length == 0) {
                errorMessage += "default constructor.";
            } else {
                errorMessage += "constructor with parameters " + StringUtils.join(constructorArgs, ", ") + ".";
            }
            throw new IllegalStateException(errorMessage, e);
        }
    }

}
