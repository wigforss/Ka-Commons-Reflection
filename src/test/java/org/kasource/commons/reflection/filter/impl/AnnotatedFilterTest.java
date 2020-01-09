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
public class AnnotatedFilterTest {


    private AnnotatedFilter filter = new AnnotatedFilter(MyAnnotation.class);


    @Test
    public void passClassTrue() {
        assertThat(filter.apply(MyClass.class), is(true));
    }

    @Test
    public void passClassFalse() {
        assertThat(filter.apply(MyAnnotation.class), is(false));
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
        Field field = MyClass.class.getField("annotated");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("notAnnotated");
        assertThat(filter.apply(field), is(false));
    }

    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("annotated");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("notAnnotated");
        assertThat(filter.apply(method), is(false));
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
    @interface MyAnnotation {

    }

    @MyAnnotation
    private static class MyClass {
        @SuppressWarnings("unused")
        @MyAnnotation
        public int annotated;
        @SuppressWarnings("unused")
        public int notAnnotated;

        @MyAnnotation
        public MyClass() {
        }

        public MyClass(String name) {
        }

        @SuppressWarnings("unused")
        @MyAnnotation
        public void annotated() {

        }

        @SuppressWarnings("unused")
        public void notAnnotated() {

        }
    }

}
