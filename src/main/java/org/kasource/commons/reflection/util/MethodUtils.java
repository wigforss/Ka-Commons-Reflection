package org.kasource.commons.reflection.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.methods.MethodFilter;
import org.kasource.commons.reflection.filter.methods.MethodFilterList;

/**
 * Utility class for Method based introspection.
 * 
 * @author rikardwi
 **/
public class MethodUtils {
    
    private MethodUtils() {}
    
    
    /**
     * Returns the named method from class <i>clazz</i>, does not throw checked exceptions.
     * 
     * @param clazz
     *            The class to inspect
     * @param name
     *            The name of the method to get
     * @param params
     *            Parameter types for the method
     * 
     * 
     * @return Returns the named method from class <i>clazz</i>.
     * 
     * @throws IllegalArgumentException if method could not be found or security 
     * issues occurred, when trying to retrieve the method.
     */
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(name, params);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not access method: " + name + " on " + clazz, e);
        }
    }
    
    /**
     * Returns the named public method from class <i>clazz</i> or any of its super classes, does not throw checked exceptions.
     * 
     * @param clazz
     *            The class to inspect
     * @param name
     *            The name of the method to get
     * @param params
     *            Parameter types for the method
     * 
     * 
     * @return Returns the named method from class <i>clazz</i>.
     * 
     * @throws IllegalArgumentException if method could not be found or security 
     * issues occurred when trying to retrieve the method.
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            return clazz.getMethod(name, params);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not access method: " + name + " on " + clazz, e);
        }
    }

    
    
    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match which matches the supplied
     * method filter.
     * 
     **/
    public static Set<Method> getDeclaredMethods(Class<?> clazz, MethodFilter methodFilter) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {       
            if (methodFilter.passFilter(method)) {
                matches.add(method);
            }
        }
        return matches;
    }
    
    /**
     * Returns the methods declared by the target class and any of its super classes, which matches the supplied
     * methodFilter.
     * 
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the methodFilter.
     * 
     **/
    public static Set<Method> getMethods(Class<?> target, MethodFilter methodFilter) {
        Class<?> clazz = target;
        Set<Method> matches = getDeclaredMethods(clazz, methodFilter);      
        while((clazz = clazz.getSuperclass()) != null) {     
            matches.addAll(getDeclaredMethods(clazz, methodFilter));
        }
        return matches;
    }
    
    /**
     * Returns the method declared by the target class and any of its super classes, which matches the supplied
     * methodFilter, if method is found null is returned. If more than one method is found the
     * first in the resulting set iterator is returned.
     * 
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return method that match the methodFilter or null if no such method was found.
     * 
     **/
    public static Method getMethod(Class<?> target, MethodFilter methodFilter) {
        Set<Method> methods = getMethods(target, methodFilter);
        if (!methods.isEmpty()) {
            return methods.iterator().next();
        }
        return null;
    }
      
    /**
     * Returns a map of methods annotated with an annotation from the annotations parameter.
     * 
     * @param methodFilter  Filter for methods, may be null to include all annotated methods.
     * @param annotations   Method annotations to find methods for
     * 
     * @return Methods that is annotated with the supplied annotation set.
     **/
    public static Map<Class<? extends Annotation>, Set<Method>> findAnnotatedMethods(Class<?> clazz, MethodFilter methodFilter, Collection<Class<? extends Annotation>> annotations) {
        
        Map<Class<? extends Annotation>, Set<Method>> annotatedMethods = new HashMap<Class<? extends Annotation>, Set<Method>>();
        for (Class<? extends Annotation> annotation : annotations) { 
            MethodFilter annotationFilter = new MethodFilterBuilder().annotated(annotation).build();
            if(methodFilter != null) {
                annotationFilter = new MethodFilterList(annotationFilter, methodFilter);
            }
            Set<Method> methods = getMethods(clazz, annotationFilter);
            annotatedMethods.put(annotation, methods);
        }
        
        return annotatedMethods;
    }
    
    
    /**
     * Returns true if method has no return type
     * 
     * @param method
     *            The method to inspect
     * 
     * @return true if return type is void
     **/
    public static boolean hasMethodNoReturnType(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    /**
     * Verify that the supplied method's signature matches return type and
     * parameters types
     * 
     * @param method
     *            Method to inspect
     * @param returnType
     *            Return type to match
     * @param parameters
     *            Parameter types to match
     * 
     * @throws IllegalArgumentException
     *             if method fails to match return type or parameters types
     */   
    public static void verifyMethodSignature(Method method, Class<?> returnType, Class<?>... parameters) {
        if(method == null) {
                throw new IllegalArgumentException("Method is null");
        }
        if (!method.getReturnType().equals(returnType)) {
                throw new IllegalArgumentException("Method " + method + " return type " + method.getReturnType()
                        + " does not match: " + returnType);
        }
        Class<?>[] actualParameters = method.getParameterTypes();
        if (actualParameters.length != parameters.length) {
            throw new IllegalArgumentException("Method " + method + " number of parameters " + actualParameters.length
                    + " does not match: " + parameters.length);
        }
        if (!Arrays.equals(actualParameters,parameters)) {
            throw new IllegalArgumentException("Method " + method + " types of parameters "
                    + Arrays.toString(actualParameters) + " does not match: " + Arrays.toString(parameters));
        }
    }
}
