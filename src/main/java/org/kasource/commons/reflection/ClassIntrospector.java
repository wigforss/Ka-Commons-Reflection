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

/**
 * Class Introspection.
 * <p>
 * This interface can be used to extract information about class meta data
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
 * ClassIntroSpector introspector = new ClassIntroSpectorImpl(MyClass.class);
 * Set<Method> getters = introspector.getMethods(new MethodFilterBuilder()
 *                          .name("get[A-Z].*").or().name("is[A-Z].*").or().name("has[A-Z]*")
 *                          .isPublic()
 *                          .not().returnType(Void.TYPE)
 *                          .numberOfParameters(0)
 *                          .build());
 * }
 *
 * @author rikardwi
 **/
public interface ClassIntrospector {

    /**
     * Returns the named method from class <i>clazz</i>, does not throw checked exceptions.
     *
     * @param name   The name of the method to get
     * @param params Parameter types for the method
     *
     * @return Returns the named method from class <i>clazz</i>.
     * @throws IllegalArgumentException if method could not be found or security
     *                                  issues occurred when trying to retrieve the method.
     */
    Method getDeclaredMethod(String name, Class<?>... params) throws IllegalArgumentException;

    /**
     * Returns the named public method from class <i>clazz</i> or any of its super classes, does not throw checked exceptions.
     *
     * @param name   The name of the method to get
     * @param params Parameter types for the method
     *
     * @return Returns the named method from class <i>clazz</i>.
     * @throws IllegalArgumentException if method could not be found or security
     *                                  issues occurred when trying to retrieve the method.
     */
    Method getMethod(String name, Class<?>... params) throws IllegalArgumentException;

    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     *
     * @param methodFilter The method filter to apply.
     *
     * @return methods that match the params argument in their method signature
     **/
    Set<Method> getDeclaredMethods(MethodFilter methodFilter);

    /**
     * Returns true if the supplied annotation is present on the target class
     * or any of its super classes.
     *
     * @param annotation Annotation to find.
     *
     * @return true if the supplied annotation is present on the target class
     * or any of its super classes.
     **/
    boolean isAnnotationPresent(Class<? extends Annotation> annotation);

    /**
     * Returns true if any of the supplied annotations is present on the target class
     * or any of its super classes.
     *
     * @param annotations Varargs Annotations to find.
     *
     * @return true if any of the supplied annotation is present on the target class
     * or any of its super classes.
     **/
    boolean isAnyAnnotationPresent(Class<? extends Annotation>... annotations);

    /**
     * Returns the annotation of the annotationClass of the clazz or any of it super classes.
     *
     * @param annotationClass Class of the annotation to return
     * @param <T> Type of the annotation
     *
     * @return The annotation of annnotationClass if found else null.
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    /**
     * Returns the methods declared by the target class and any of its super classes, which matches the supplied
     * methodFilter.
     *
     * @param methodFilter The method filter to apply.
     * @return methods that match the methodFilter.
     **/
    Set<Method> getMethods(MethodFilter methodFilter);

    /**
     * Returns the method declared by the target class and any of its super classes, which matches the supplied
     * methodFilter, if method is found null is returned. If more than one method is found the
     * first in the resulting set iterator is returned.
     *
     * @param methodFilter The method filter to apply.
     * @return method that match the methodFilter or null if no such method was found.
     **/
    Method getMethod(MethodFilter methodFilter);

    /**
     * Returns a set of interfaces from the target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     *
     * @param filter The class filter to use.
     * @return a set of interfaces from the target class that passes the supplied filter.
     */
    Set<Class<?>> getInterfaces(ClassFilter filter);

    /**
     * Returns the interface from target class that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     * <p>
     * If no interface is found null is returned.
     *
     * @param filter The class filter to use.
     * @return the interface from target class that passes the supplied filter, may
     * be null if no match is found.
     */
    Class<?> getInterface(ClassFilter filter);

    /**
     * Returns a set of interfaces that that passes the supplied filter.
     *
     * @param filter The class filter to use.
     * @return all Interface classes from clazz that passes the filter.
     */
    Set<Class<?>> getDeclaredInterfaces(ClassFilter filter);

    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class.
     *
     * @param filter Filter to use.
     * @return All matching fields declared by the target class.
     **/
    Set<Field> getDeclaredFields(FieldFilter filter);

    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class or any of its super classes.
     *
     * @param filter Filter to use.
     * @return All matching fields declared by the target class.
     **/
    Set<Field> getFields(FieldFilter filter);

    /**
     * Returns set of constructors that matches the filter parameter.
     *
     * @param filter Filter to apply.
     * @param ofType Class to get constructor for, must match target class.
     * @param <T> Type of the constructor.
     *
     * @return constructors that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     **/
    <T> Set<Constructor<T>> findConstructors(ConstructorFilter filter, Class<T> ofType)
            throws IllegalArgumentException;

    /**
     * Returns the first constructor found that matches the filter parameter.
     *
     * @param filter Filter to apply.
     * @param ofType Class to get constructor for, must match target class.
     * @param <T> The type of the constructor.
     *
     * @return the first constructor found that matches the filter parameter.
     * @throws IllegalArgumentException if ofType does not match the target class.
     *                                  or no constructor is found matching the filter.
     **/
    <T> Constructor<T> findFirstConstructor(ConstructorFilter filter, Class<T> ofType);


    /**
     * Returns a map of methods annotated with an annotation from the annotations parameter.
     *
     * @param methodFilter Filter for methods, may be null to include all annotated methods.
     * @param annotations  Method annotations to find methods for
     *
     * @return Methods that is annotated with the supplied annotation set.
     **/
    Map<Class<? extends Annotation>, Set<Method>> findAnnotatedMethods(MethodFilter methodFilter,
                                                                       Collection<Class<? extends Annotation>> annotations);

}
