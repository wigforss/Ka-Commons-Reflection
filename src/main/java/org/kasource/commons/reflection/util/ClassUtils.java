package org.kasource.commons.reflection.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.filter.ClassFilter;


/**
 * Class loading Utilities.
 *
 * @author rikardwi
 **/
public final class ClassUtils {

    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<>();

    static {
        PRIMITIVE_CLASSES.put("byte", byte.class);
        PRIMITIVE_CLASSES.put("short", short.class);
        PRIMITIVE_CLASSES.put("char", char.class);
        PRIMITIVE_CLASSES.put("int", int.class);
        PRIMITIVE_CLASSES.put("long", long.class);
        PRIMITIVE_CLASSES.put("float", float.class);
        PRIMITIVE_CLASSES.put("double", double.class);
        PRIMITIVE_CLASSES.put("boolean", boolean.class);
        PRIMITIVE_CLASSES.put("[B", byte[].class);
        PRIMITIVE_CLASSES.put("[Lbyte", byte[].class);
        PRIMITIVE_CLASSES.put("[S", short[].class);
        PRIMITIVE_CLASSES.put("[Lshort", short[].class);
        PRIMITIVE_CLASSES.put("[C", char[].class);
        PRIMITIVE_CLASSES.put("[Lchar", char[].class);
        PRIMITIVE_CLASSES.put("[I", int[].class);
        PRIMITIVE_CLASSES.put("[Lint", int[].class);
        PRIMITIVE_CLASSES.put("[L", long[].class);
        PRIMITIVE_CLASSES.put("[Llong", long[].class);
        PRIMITIVE_CLASSES.put("[F", float[].class);
        PRIMITIVE_CLASSES.put("[Lfloat", float[].class);
        PRIMITIVE_CLASSES.put("[D", double[].class);
        PRIMITIVE_CLASSES.put("[Ldouble", double[].class);
        PRIMITIVE_CLASSES.put("[Z", boolean[].class);
        PRIMITIVE_CLASSES.put("[Lboolean", boolean[].class);

    }

    private ClassUtils() {
    }

    public static Class<?> getPrimitiveClass(String primitiveClassName) {
        return PRIMITIVE_CLASSES.get(primitiveClassName);
    }


    /**
     * Returns a set of interfaces from the target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     *
     * @param target The class to inspect.
     * @param filter The class filter to use.
     *
     * @return a set of interfaces from the target class that passes the supplied filter.
     */
    @SuppressWarnings("PMD.AssignmentInOperand")
    public static Set<Class<?>> getInterfaces(Class<?> target, ClassFilter filter) {
        Class<?> clazz = target;
        Set<Class<?>> interfacesFound = getDeclaredInterfaces(clazz, filter);

        while ((clazz = clazz.getSuperclass()) != null) {
            interfacesFound.addAll(getDeclaredInterfaces(clazz, filter));
        }
        return interfacesFound;

    }

    /**
     * Returns the interface from target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     * <p>
     * If no interface is found null is returned.
     *
     * @param target The class to inspect.
     * @param filter The class filter to use.
     *
     * @return the interface from target class that passes the supplied filter, may
     * be null if no match is found.
     */
    public static Class<?> getInterface(Class<?> target, ClassFilter filter) {
        Set<Class<?>> interfaces = getInterfaces(target, filter);
        if (!interfaces.isEmpty()) {
            return interfaces.iterator().next();
        }
        return null;
    }


    /**
     * Returns a set of interfaces from the clazz that passes the supplied filter.
     *
     * @param clazz  The class to inspect
     * @param filter The class filter to use.
     *
     * @return all Interface classes from clazz that passes the filter.
     */
    public static Set<Class<?>> getDeclaredInterfaces(Class<?> clazz, ClassFilter filter) {
        return Arrays.stream(clazz.getInterfaces()).filter(i -> filter.apply(i)).collect(Collectors.toSet());
    }


    /**
     * Loads and returns the class named className of type superClass.
     *
     * @param <T>       Type of the class
     * @param className Name of the class to load
     * @param ofType    Type of class
     *
     * @return The loaded class of type superClass.
     * @throws IllegalArgumentException if the class with className could not be loaded or
     *                                  if the that class does not extend the class supplied in the superClass parameter.
     **/
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> loadClass(String className, Class<T> ofType) {
        try {
            Class<?> clazz = Class.forName(className);

            if (ofType == null || !ofType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Class " + className + " must extend or implement " + ofType + "!");
            }
            return (Class<? extends T>) clazz;
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Class " + className + " could not be found!", cnfe);
        }
    }

}
