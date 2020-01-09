package org.kasource.commons.reflection;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.kasource.commons.reflection.filter.builder.MethodFilterBuilder;


@RunWith(MockitoJUnitRunner.class)
public class ClassIntrospectorTest {

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private ClassFilter classFilter;

    @Mock
    private FieldFilter fieldFilter;

    @Mock
    private ConstructorFilter constructorFilter;


    private ClassIntrospectorImpl introspector = new ClassIntrospectorImpl(MyClass.class);

    @Test
    public void getDeclaredMethod() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        assertThat(introspector.getDeclaredMethod("privateOneParamter", String.class), equalTo(method));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMissingDeclaredMethod() throws SecurityException, NoSuchMethodException {

        introspector.getDeclaredMethod("noSuchMethod", String.class);
    }

    @Test
    public void getMethod() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("oneParameter", String.class);
        assertThat(introspector.getDeclaredMethod("oneParameter", String.class), equalTo(method));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMissingMethod() throws SecurityException, NoSuchMethodException {

        introspector.getMethod("noSuchMethod", String.class);
    }

    @Test
    public void getDeclaredMethods() throws SecurityException, NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        when(methodFilter.apply(method1)).thenReturn(true);
        when(methodFilter.apply(method2)).thenReturn(false);


        Set<Method> methods = introspector.getDeclaredMethods(methodFilter);
        assertThat(methods.contains(method1), is(true));
        assertThat(methods.contains(method2), is(false));
        assertThat(methods.size(), is(1));
    }

    @Test
    public void getMethods() throws SecurityException, NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        Method method3 = MyClass.class.getMethod("superClassMethod", String.class);

        when(methodFilter.apply(method1)).thenReturn(false);
        when(methodFilter.apply(method2)).thenReturn(false);
        when(methodFilter.apply(method3)).thenReturn(true);


        Set<Method> methods = introspector.getMethods(methodFilter);

        assertThat("The result should include one method", methods.size(), is(1));
        assertThat("The result should not contain method1", methods.contains(method1), is(false));
        assertThat("The result should not contain method2", methods.contains(method2), is(false));
        assertThat("The result should contain method3", methods.contains(method3), is(true));

    }

    @Test
    public void getInterfaces() {
        when(classFilter.apply(EventListener.class)).thenReturn(false);
        when(classFilter.apply(Serializable.class)).thenReturn(false);
        when(classFilter.apply(Runnable.class)).thenReturn(true);

        Set<Class<?>> interfaces = introspector.getInterfaces(classFilter);
        assertThat(interfaces.contains(EventListener.class), is(false));
        assertThat(interfaces.contains(Serializable.class), is(false));
        assertThat(interfaces.contains(Runnable.class), is(true));
        assertThat(interfaces.size(), is(1));
    }

    @Test
    public void getDeclaredInterfaces() {
        when(classFilter.apply(EventListener.class)).thenReturn(false);
        when(classFilter.apply(Serializable.class)).thenReturn(true);


        Set<Class<?>> interfaces = introspector.getDeclaredInterfaces(classFilter);
        assertThat(interfaces.contains(EventListener.class), is(false));
        assertThat(interfaces.contains(Serializable.class), is(true));

        assertThat(interfaces.size(), is(1));
    }

    @Test
    public void getDeclaredFields() throws SecurityException, NoSuchFieldException {
        Field name = MyClass.class.getDeclaredField("name");
        Field age = MyClass.class.getDeclaredField("age");

        when(fieldFilter.apply(name)).thenReturn(false);
        when(fieldFilter.apply(age)).thenReturn(true);

        Set<Field> fields = introspector.getDeclaredFields(fieldFilter);
        assertThat(fields.contains(name), is(false));
        assertThat(fields.contains(age), is(true));

        assertThat(fields.size(), is(1));
    }


    @Test
    public void getFields() throws SecurityException, NoSuchFieldException {
        Field name = MyClass.class.getDeclaredField("name");
        Field age = MyClass.class.getDeclaredField("age");
        Field superField = MyBase.class.getDeclaredField("superField");
        when(fieldFilter.apply(name)).thenReturn(false);
        when(fieldFilter.apply(age)).thenReturn(false);
        when(fieldFilter.apply(superField)).thenReturn(true);


        Set<Field> fields = introspector.getFields(fieldFilter);
        assertThat(fields.contains(name), is(false));
        assertThat(fields.contains(age), is(false));
        assertThat(fields.contains(superField), is(true));
        assertThat(fields.size(), is(1));
    }

    @Test
    public void getConstructors() throws SecurityException, NoSuchMethodException {
        Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
        Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
        Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
        when(constructorFilter.apply(defCons)).thenReturn(true);
        when(constructorFilter.apply(cons1)).thenReturn(false);
        when(constructorFilter.apply(cons2)).thenReturn(true);

        Set<Constructor<MyClass>> constructors = introspector.findConstructors(constructorFilter, MyClass.class);
        assertThat(constructors.contains(defCons), is(true));
        assertThat(constructors.contains(cons1), is(false));
        assertThat(constructors.contains(cons2), is(true));
        assertThat(constructors.size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConstructorsWrongType() {
        introspector.findConstructors(constructorFilter, MyBase.class);
    }

    @Test
    public void getConstructor() throws SecurityException, NoSuchMethodException {
        Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
        Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
        Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
        when(constructorFilter.apply(defCons)).thenReturn(false);
        when(constructorFilter.apply(cons1)).thenReturn(false);
        when(constructorFilter.apply(cons2)).thenReturn(true);

        Constructor<MyClass> constructor = introspector.findFirstConstructor(constructorFilter, MyClass.class);
        assertThat(constructor, equalTo(cons2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConstructorNoMatch() throws SecurityException, NoSuchMethodException {
        Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
        Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
        Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
        when(constructorFilter.apply(defCons)).thenReturn(false);
        when(constructorFilter.apply(cons1)).thenReturn(false);
        when(constructorFilter.apply(cons2)).thenReturn(false);

        Constructor<MyClass> constructor = introspector.findFirstConstructor(constructorFilter, MyClass.class);
        assertThat(constructor, equalTo(cons2));
    }


    @Test(expected = IllegalArgumentException.class)
    public void getConstructorWrongType() {
        introspector.findFirstConstructor(constructorFilter, MyBase.class);
    }

    @Test
    public void isAnnotationPresentTrue() {
        assertThat(introspector.isAnnotationPresent(ClassAnnotation2.class), is(true));
    }

    @Test
    public void isAnnotationPresentFalse() {
        assertThat(introspector.isAnnotationPresent(ClassAnnotation1.class), is(false));
    }

    @Test
    public void getAnnotation() {
        assertThat(introspector.getAnnotation(ClassAnnotation2.class).value(), equalTo("Value2"));
    }

    @Test
    public void getAnnotationNotFound() {
        assertThat(introspector.getAnnotation(ClassAnnotation1.class), nullValue());
    }

    @Test
    public void findAnnotatedMethods() throws SecurityException, NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        Method method3 = MyClass.class.getMethod("run");
        Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
        annotations.add(MethodAnnotation1.class);
        annotations.add(MethodAnnotation2.class);
        Map<Class<? extends Annotation>, Set<Method>> map = introspector.findAnnotatedMethods(null, annotations);
        Set<Method> anno1 = map.get(MethodAnnotation1.class);
        Set<Method> anno2 = map.get(MethodAnnotation2.class);
        assertThat(anno1.size(), is(2));
        assertThat(anno1.contains(method1), is(true));
        assertThat(anno1.contains(method2), is(true));
        assertThat(anno2.size(), is(1));
        assertThat(anno2.contains(method3), is(true));
    }

    @Test
    public void findAnnotatedMethodsWithFilter() throws SecurityException, NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        Method method3 = MyClass.class.getMethod("run");
        Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
        annotations.add(MethodAnnotation1.class);
        annotations.add(MethodAnnotation2.class);
        Map<Class<? extends Annotation>, Set<Method>> map = introspector.findAnnotatedMethods(new MethodFilterBuilder().isPublic().build(), annotations);
        Set<Method> anno1 = map.get(MethodAnnotation1.class);
        Set<Method> anno2 = map.get(MethodAnnotation2.class);
        assertThat(anno1.size(), is(1));
        assertThat(anno1.contains(method1), is(true));
        assertThat(anno1.contains(method2), is(false));
        assertThat(anno2.size(), is(1));
        assertThat(anno2.contains(method3), is(true));
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation1 {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation2 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation1 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation2 {
        String value();
    }


    private static class MyClass extends MyBase implements EventListener, Serializable {
        private String name;
        private int age;

        public MyClass() {
        }

        public MyClass(String name) {
        }


        private MyClass(String name, int age) {
        }

        @MethodAnnotation1
        public void oneParameter(String name) {
        }

        @MethodAnnotation1
        private void privateOneParamter(String name) {
        }
    }

    @ClassAnnotation2("Value2")
    private static class MyBase implements Runnable {
        private String superField;

        public void superClassMethod(String name) {
        }

        @MethodAnnotation2
        @Override
        public void run() {
            // TODO Auto-generated method stub

        }
    }
}
