package org.kasource.commons.reflection.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {
   
    /**
     * Private constructor.
     **/
    private AnnotationUtils() { }
    
    
   
    /**
     * Returns true if the supplied annotation is present on the target class
     * or any of its super classes.
     * 
     * @param annotation Annotation to find.
     * 
     * @return true if the supplied annotation is present on the target class
     * or any of its super classes.
     **/
    public static boolean isAnnotationPresent(Class<?> target, Class<? extends Annotation> annotation) {
        Class<?> clazz = target;
        if (clazz.isAnnotationPresent(annotation)) {
            return true;
        }
        while((clazz = clazz.getSuperclass()) != null) {
            if (clazz.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns true if any of the supplied annotations is present on the target class
     * or any of its super classes.
     * 
     * @param annotations Annotations to find.
     * 
     * @return true if any of the supplied annotation is present on the target class
     * or any of its super classes.
     **/
    public static boolean isAnyAnnotationPresent(Class<?> target, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotationClass : annotations) {
            if (isAnnotationPresent(target, annotationClass)) {
                return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * Returns the annotation of the annotationClass of the clazz or any of it super classes.
     * 
     * @param clazz
     *           The class to inspect.
     * @param annotationClass
     *           Class of the annotation to return
     *           
     * @return The annotation of annnotationClass if found else null.
     */
    public static <T extends Annotation> T getAnnotation( Class<?> target, Class<T> annotationClass) {
        Class<?> clazz = target;
        T annotation = clazz.getAnnotation(annotationClass);
        if(annotation != null) {
            return annotation;
        }
        while((clazz = clazz.getSuperclass()) != null) {
            annotation = clazz.getAnnotation(annotationClass);
            if(annotation != null) {
                return annotation;
            }
        }
        return null;
    }
    
    
    /**
     * Returns index of the first parameter with matching annotationClass, -1 if no parameter
     * found with the supplied annotation.
     * 
     * @param method           Method to inspect parameters of.
     * @param annotationClass  Annotation to look for.
     * 
     * @return index of the first parameter found with annotationClass, -1 if not parameter found.
     **/
    public static int getAnnotatedParameterIndex(Method method, Class<? extends Annotation> annotationClass) {
        Annotation[][] annotationsForParameters = method.getParameterAnnotations();
        for (int i = 0; i < annotationsForParameters.length; i++) {
            Annotation[] parameterAnnotations = annotationsForParameters[i];
            if (hasAnnotation(parameterAnnotations, annotationClass)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns true if any annotation in parameterAnnotations matches annotationClass.
     * 
     * @param parameterAnnotations Annotations to inspect.
     * @param annotationClass      Annotation to find.
     * 
     * @return  true if any annotation in parameterAnnotations matches annotationClass, else false.
     */
    private static boolean hasAnnotation(Annotation[] parameterAnnotations,  
                                                        Class<? extends Annotation> annotationClass) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return true;
            }
        }
        return false;
    }
    
  

        
}
