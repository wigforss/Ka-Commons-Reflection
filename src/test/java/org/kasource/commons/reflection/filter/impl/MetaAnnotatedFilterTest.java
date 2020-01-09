package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MetaAnnotatedFilterTest {

    private MetaAnnotatedFilter filter = new MetaAnnotatedFilter(SuperAnnotation.class);

    @Test
    public void passTrue() {
        assertThat(filter.apply(MyClass.class), is(true));
    }

    @Test
    public void passFalse() {
        assertThat(filter.apply(MyAnnotation.class), is(false));
    }

    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("firstMethod");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("secondMethod");
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passConstructorTrue() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalse() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passFieldTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("annotated");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("notAnnotated");
        assertThat(filter.apply(field), is(false));
    }

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SuperAnnotation {
    }

    @SuperAnnotation
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {

    }

    @MyAnnotation
    private static class MyClass {

        @MyAnnotation
        private int annotated;
        private int notAnnotated;

        @MyAnnotation
        @SuppressWarnings("unused")
        public MyClass() {
        }

        public MyClass(String name) {
        }
        @MyAnnotation
        @SuppressWarnings("unused")
        public void firstMethod() {
        }


        @SuppressWarnings("unused")
        public void secondMethod() {
        }
    }
}
