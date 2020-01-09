package org.kasource.commons.reflection.collection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Wraps a map and resolves super packages of the key.
 *
 * @param <T> Content type of the map
 * @author rikardwi
 **/
public class PackageMap<T> {

    private Map<String, T> map = new HashMap<>();

    public PackageMap() {
    }

    public PackageMap(final Map<String, T> map) {
        this.map.putAll(map);
    }


    public T get(Class clazz) {
        return get(clazz.getPackage().getName());
    }

    public T get(final String packagePath) {
        String packageName = packagePath;
        T object = map.get(packageName);
        String subPackageName = StringUtils.substringBeforeLast(packageName, ".");
        while (object == null && !subPackageName.equals(packageName)) {
            packageName = subPackageName;
            object = map.get(packageName);
            subPackageName = StringUtils.substringBeforeLast(packageName, ".");
        }
        return object;
    }

}
