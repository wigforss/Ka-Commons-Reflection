package org.kasource.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.kasource.commons.reflection.util.AnnotationUtils;
import org.kasource.commons.reflection.util.ClassUtils;
import org.kasource.commons.reflection.util.ConstructorUtils;
import org.kasource.commons.reflection.util.FieldUtils;
import org.kasource.commons.reflection.util.MethodUtils;

/**
 * Class Introspection Implementation.
 * <p>
 * This class can be used to extract information about class meta data
 * such as interfaces, methods and fields.
 * <p>
 * The difference between getFields and getDeclaredField is that getFields
 * returns fields declared by super classes as well as fields declared by
 * the target class. While getDeclaredField only returns fields declared by
 * the target class.
 * <p>
 * The same pattern is applied for any getXXX and getDeclaredXXX method pairs.
 * <p>
 * ClassFilter, FieldFilter and MethodFilter is used to query (filter) for interfaces, fields
 * and methods.
 * <p>
 * Example: Finding all getters:
 * {@code
 * ClassIntroSpector introspector = new ClassIntroSpector(MyClass.class);
 * Set<Method> getters = introspector.getMethods(new MethodFilterBuilder()
 *                          .name("get[A-Z].*").or().name("is[A-Z].*").or().name("has[A-Z]*")
 *                          .isPublic()
 *                          .not().returnType(Void.TYPE)
 *                          .numberOfParameters(0).build());
 * }
 *
 * @author rikardwi
 **/
public class ClassIntrospectorImpl implements ClassIntrospector {

    private Class<?> target;

    /**
     * Constructor.
     *
     * @param target The target class to introspect.s
     **/
    public ClassIntrospectorImpl(final Class<?> target) {
        this.target = target;
    }


    /**
     * Returns the named method from class <i>clazz</i>, does not throw checked exceptions.
     *
     * @param name   The name of the method to get
     * @param params Parameter types for the method
     *
     * @return Returns the named method from class <i>target</i>.
     * @throws IllegalArgumentException if method could not be found or security
     *                                  issues occurred when trying to retrieve the method.
     */
    @Override
    public Method getDeclaredMethod(String name, Class<?>... params) throws IllegalArgumentException {
        return MethodUtils.getDeclaredMethod(target, name, params);
    }

    /**
     * Returns the named public method from class <i>target</i> or any of its super classes, does not throw checked exceptions.
     *
     * @param name   The name of the method to get
     * @param params Parameter types for the method
     *
     * @return Returns the named method from class <i>target</i>.
     * @throws IllegalArgumentException if method could not be found or security
     *                                  issues occurred when trying to retrieve the method.
     */
    @Override
    public Method getMethod(String name, Class<?>... params) throws IllegalArgumentException {
        return MethodUtils.getMethod(target, name, params);
    }

    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     *
     * @param methodFilter The method filter to apply.
     *
     * @return methods that match the params argument in their method signature
     **/
    @Override
    public Set<Method> getDeclaredMethods(MethodFilter methodFilter) {
        return MethodUtils.getDeclaredMethods(target, methodFilter);
    }

    /**
     * Returns true if the supplied annotation is present on the target class
     * or any of its super classes.
     *
     * @param annotation Annotation to find.
     *
     * @return true if the supplied annotation is present on the target class
     * or any of its super classes.
     **/
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return AnnotationUtils.isAnnotationPresent(target, annotation);
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
    @Override
    public boolean isAnyAnnotationPresent(Class<? extends Annotation>... annotations) {
        return AnnotationUtils.isAnyAnnotationPresent(target, annotations);

    }

    /**
     * Returns the annotation of the annotationClass of the clazz or any of it super classes.
     *
     * @param annotationClass Class of the annotation to return
     * @param <T> Type of annotation.
     *
     * @return The annotation of annnotationClass if found else null.
     */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return AnnotationUtils.getAnnotation(target, annotationClass);
    }


    /**
     * Returns the methods declared by the target class and any of its super classes, which matches the supplied
     * methodFilter.
     *
     * @param methodFilter The method filter to apply.
     * @return methods that match the methodFilter.
     **/
    @Override
    public Set<Method> getMethods(MethodFilter methodFilter) {
        return MethodUtils.getMethods(target, methodFilter);
    }

    /**
     * Returns the method declared by the target class and any of its super classes, which matches the supplied
     * methodFilter, if method is found null is returned. If more than one method is found the
     * first in the resulting set iterator is returned.
     *
     * @param methodFilter The method filter to apply.
     *
     * @return method that match the methodFilter or null if no such method was found.
     **/
    @Override
    public Method getMethod(MethodFilter methodFilter) {
        return MethodUtils.getMethod(target, methodFilter);
    }


    /**
     * Returns a set of interfaces from the target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     *
     * @param filter The class filter to use.
     *
     * @return a set of interfaces from the target class that passes the supplied filter.
     */
    @Override
    public Set<Class<?>> getInterfaces(ClassFilter filter) {
        return ClassUtils.getInterfaces(target, filter);
    }

    /**
     * Returns the interface from target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     * <p>
     * If no interface is found null is returned.
     *
     * @param filter The class filter to use.
     *
     * @return the interface from target class that passes the supplied filter, may
     * be null if no match is found.
     */
    @Override
    public Class<?> getInterface(ClassFilter filter) {
        return ClassUtils.getInterface(target, filter);
    }


    /**
     * Returns a set of interfaces that that passes the supplied filter.
     *
     * @param filter The class filter to use.
     *
     * @return all Interface classes from clazz that passes the filter.
     */
    @Override
    public Set<Class<?>> getDeclaredInterfaces(ClassFilter filter) {
        return ClassUtils.getDeclaredInterfaces(target, filter);
    }


    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class.
     *
     * @param filter Filter to use.
     *
     * @return All matching fields declared by the target class.
     **/
    @Override
    public Set<Field> getDeclaredFields(FieldFilter filter) {
        return FieldUtils.getDeclaredFields(target, filter);
    }


    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class or any of its super classes.
     *
     * @param filter Filter to use.
     *
     * @return All matching fields declared by the target class.
     **/
    @Override
    public Set<Field> getFields(FieldFilter filter) {
        return FieldUtils.getFields(target, filter);
    }

    /**
     * Returns set of constructors that matches the filter parameter.
     *
     * @param filter Filter to apply.
     * @param ofType Class to get constructor for, must match target class.
     * @param <T> Type of constructor
     *
     * @return Set of constructors that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     **/
    @Override
    public <T> Set<Constructor<T>> findConstructors(ConstructorFilter filter, Class<T> ofType) throws IllegalArgumentException {
        if (!ofType.equals(target)) {
            throw new IllegalArgumentException("ofType must be target class: " + target);
        }
        return ConstructorUtils.findConstructors(ofType, filter);
    }


    /**
     * Returns the first constructor found that matches the filter parameter.
     *
     * @param filter Filter to apply.
     * @param ofType Class to get constructor for, must match target class.
     * @param <T> Type of constructor
     *
     * @return the first constructor found that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     *                                  or no constructor is found matching the filter.
     **/
    @Override
    public <T> Constructor<T> findFirstConstructor(ConstructorFilter filter, Class<T> ofType) {
        if (!ofType.equals(target)) {
            throw new IllegalArgumentException("ofType must be target class: " + target);
        }
        return ConstructorUtils.findFirstConstructor(ofType, filter);
    }



    /**
     * Returns a map of methods annotated with an annotation from the annotations parameter.
     *
     * @param methodFilter Filter for methods, may be null to include all annotated methods.
     * @param annotations  Method annotations to find methods for
     *
     * @return Methods that is annotated with the supplied annotation set.
     **/
    @Override
    public Map<Class<? extends Annotation>, Set<Method>> findAnnotatedMethods(MethodFilter methodFilter,
                                                                              Collection<Class<? extends Annotation>> annotations) {
        return MethodUtils.findAnnotatedMethods(target, methodFilter, annotations);
    }

}
