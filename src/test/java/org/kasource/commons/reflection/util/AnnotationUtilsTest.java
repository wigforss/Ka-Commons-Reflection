package org.kasource.commons.reflection.util;


import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class AnnotationUtilsTest {


    @Test
    public void isAnnotationPresentTrue() {
        assertThat(AnnotationUtils.isAnnotationPresent(MyClass.class, ClassAnnotation2.class), is(true));
    }

    @Test
    public void isAnnotationPresentCurrentClassTrue() {
        assertThat(AnnotationUtils.isAnnotationPresent(MyClass.class, ClassAnnotation3.class), is(true));
    }

    @Test
    public void isAnnotationPresentFalse() {
        assertThat(AnnotationUtils.isAnnotationPresent(MyClass.class, ClassAnnotation1.class), is(false));
    }

    @Test
    public void isAnyAnnotationPresentTrue() {
        assertThat(AnnotationUtils.isAnyAnnotationPresent(MyClass.class, ClassAnnotation1.class, ClassAnnotation2.class), is(true));
    }

    @Test
    public void getAnnotation() {
        assertThat(AnnotationUtils.getAnnotation(MyClass.class, ClassAnnotation2.class).value(), equalTo("Value2"));
    }

    @Test
    public void getAnnotationNotFound() {
        assertThat(AnnotationUtils.getAnnotation(MyClass.class, ClassAnnotation1.class), nullValue());
    }

    @Test
    public void getAnnotatedParameterIndexFound() throws NoSuchMethodException {
        assertThat(AnnotationUtils.getAnnotatedParameterIndex(MyClass.class.getMethod("oneParameter", String.class), ParameterAnnotation1.class), is(0));
    }

    @Test
    public void getAnnotatedParameterIndexNotFound() throws NoSuchMethodException {
        assertThat(AnnotationUtils.getAnnotatedParameterIndex(MyClass.class.getMethod("oneParameter", String.class), ParameterAnnotation2.class), is(-1));
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation1 {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation2 {
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParameterAnnotation1 {
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParameterAnnotation2 {
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

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation3 {
    }

    @ClassAnnotation3
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
        public void oneParameter(@ParameterAnnotation1 String name) {
        }

        @MethodAnnotation1
        private void privateOneParamter(@ParameterAnnotation2 String name) {
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
