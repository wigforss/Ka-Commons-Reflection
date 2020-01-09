package org.kasource.commons.reflection.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;


/**
 * Class for scanning classpath after annotated classes.
 *
 * @author rikardwi
 **/
public class AnnotationScanner {


    /**
     * Scan classpath for packages matching scanPath for classes annotated with the supplied annotationClass that
     * extends or implements the supplied ofType.
     *
     * @param <T>             Type of class to find
     * @param scanPath        Comma separated list of packages to scan for classes.
     * @param annotationClass The annotation to find.
     * @param ofType          The type the found classes should extend or implement, set to Object if no validation is needed.
     * @return All classes found by scanning the classapth that extends or implements the ofType type.
     * @throws IOException           If exception occurred while accessing class path.
     * @throws IllegalStateException if class found does not extends of implement the ofType parameter or
     *                               the class found could not be loaded.
     **/
    public <T> Set<Class<? extends T>> findAnnotatedClasses(String scanPath,
                                                            Class<? extends Annotation> annotationClass,
                                                            Class<T> ofType) throws IOException {
        Set<Class<? extends T>> classes = new HashSet<>();
        String packagesToScan = scanPath;
        if (packagesToScan.contains(".")) {
            packagesToScan = packagesToScan.replace('.', '/');
        }
        String[] scanPaths = packagesToScan.split(",");

        String includeRegExp = buildIncludeRegExp(scanPaths);
        URL[] urls = resolverUrls(scanPaths);

        Map<String, Set<String>> annotationIndex = scanForAnnotatedClasses(urls);
        Set<String> classNames = annotationIndex.get(annotationClass.getName());
        if (classNames != null) {
            for (String className : classNames) {
                addClass(classes, includeRegExp, className, ofType, annotationClass);
            }
        }

        return classes;

    }


    /**
     * Adds matching classes to the resulting set of classes.
     *
     * @param classes          The resulting set of classes
     * @param includeRegExp    Regular Expression of classes to include
     * @param className        Name of the class to evaluate
     * @param ofType           The type the class should extend
     * @param annotationClass  The annotation to scan for
     * @param <T>   The base type of classes to scan for.
     */
    @SuppressWarnings("unchecked")
    private <T> void addClass(Set<Class<? extends T>> classes,
                              String includeRegExp,
                              String className,
                              Class<T> ofType,
                              Class<? extends Annotation> annotationClass) {
        if (className.matches(includeRegExp)) {
            try {
                Class<?> matchingClass = Class.forName(className);
                matchingClass.asSubclass(ofType);

                classes.add((Class<T>) matchingClass);
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalStateException("Scannotation found a class that does not exist " + className + " !", cnfe);
            } catch (ClassCastException cce) {
                throw new IllegalStateException("Class " + className
                        + " is annoted with @" + annotationClass + " but does not extend or implement  "
                        + ofType, cce);
            }
        }
    }


    /**
     * Returns the URLs which has classes from scanPaths.
     *
     * @param scanPaths Comma separated list of package names.
     * @return URLs matching classes in scanPaths.
     **/
    private URL[] resolverUrls(String... scanPaths) {
        return Arrays.stream(scanPaths)
                .map(s -> ClasspathUrlFinder.findResourceBases(s.trim()))
                .flatMap(a -> Arrays.stream(a))
                .toArray(URL[]::new);
    }

    /**
     * Returns a regular expression of acceptable package names.
     *
     * @param scanPaths Comma separated list of package names.
     * @return a regular expression of acceptable package names.
     **/
    private String buildIncludeRegExp(String... scanPaths) {
        return Arrays.stream(scanPaths)
                .map(p -> p.trim()
                        .replace("/", "\\.") + ".*|")
                .collect(Collectors.joining(","));

    }

    /**
     * Scans for classes in URLs.
     *
     * @param urls URLs to scan for annotated classes.
     * @return Map of annotated classes found.
     * @throws IOException If exception occurs.
     **/
    private Map<String, Set<String>> scanForAnnotatedClasses(URL... urls) throws IOException {
        AnnotationDB db = new AnnotationDB();
        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);

        db.scanArchives(urls);
        return db.getAnnotationIndex();

    }


}
