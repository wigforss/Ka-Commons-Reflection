package org.kasource.commons.reflection.collection;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Wraps a map and resolves super classes and / or interfaces of the key.
 * <p>
 * {@code
 * Map<Class<?>, String> map = new HashMap<Class<?>, String>();
 * map.put(java.lang.Object.class, "Object");
 * map.put(java.lang.Integer.class, "Integer")
 * ClassMap<String> classMap = new ClassMap<String>();
 * classMap.setMap(map);
 * String doubleString = classMap.get(java.lang.Double);
 * assert(doubleString.equals("Object"))
 * String numberString = classMap.get(java.lang.Number);
 * assert(numberString.equals("Object"))
 * String integerString = classMap.get(java.lang.Integer);
 * assert(numberString.equals("Integer"))
 * }
 *
 * @param <T> Content type of the map
 * @author rikardwi
 **/
public class ClassMap<T> {

    private Map<Class<?>, T> map = new HashMap<>();

    public ClassMap() {
    }

    public ClassMap(final Map<Class<?>, T> map) {
        this.map.putAll(map);
    }

    /**
     * Returns the value of a map entry which key matches the supplied class
     * or any of its super classes or any of its interfaces.
     * <p>
     * Values are resolved using the most specific classes and interfaces first
     * and then more general (base classes).
     *
     * @param clazz Class to resolve value for by inspecting the class META data.
     * @return value found for the supplied class or null if no value could be resolved.
     **/
    public T get(final Class<?> clazz) {

        Class<?> classToInspect = clazz;
        T object = map.get(classToInspect);
        if (object == null) {
            object = getByInterfaces(classToInspect);
        }
        while (object == null && classToInspect.getSuperclass() != null) {
            classToInspect = classToInspect.getSuperclass();
            object = map.get(classToInspect);
            if (object == null) {
                object = getByInterfaces(classToInspect);
            }
        }
        return object;
    }


    /**
     * Returns the value associated with the any
     * interface implemented by the supplied class.
     * <p>
     * This method will return the first match only, if
     * more than one value can be resolved the first is
     * returned.
     *
     * @param clazz Class to inspect interfaces of.
     * @return The resolved value if found, else null.
     **/
    public T getByInterfaces(final Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces())
                .map(i -> map.get(i)).filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }



}
