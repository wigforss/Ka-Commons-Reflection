package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SignatureFilterTest {

    private SignatureFilter filter = new SignatureFilter(String.class, int.class);

    @Test
    public void filterMethodPass() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("twoParamters", String.class, int.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void filterMethodNotPassTooFewParameters() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("oneParamter", String.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void filterMethodNotPassTooManyParameters() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("threeParameters", String.class, int.class, int.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void filterMethodNotPassWrongType() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("twoParametersWrongType", String.class, String.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void filterConstructorPass() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class, int.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void filterConstructorNotPassTooFewParameters() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void filterConstructorNotPassTooManyParameters() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class, int.class, int.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void filterConstructorNotPassWrongType() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class, String.class);
        assertThat(filter.apply(cons), is(false));
    }

    private static class MyClass {
        public MyClass(String name) {
        }

        public MyClass(String name, int age) {
        }

        public MyClass(String name, String ssn) {
        }

        public MyClass(String name, int age, int length) {
        }

        public void oneParamter(String name) {
        }

        public void twoParamters(String name, int age) {
        }

        public void twoParametersWrongType(String name, String ssn) {
        }

        public void threeParameters(String name, int age, int length) {
        }
    }
}
