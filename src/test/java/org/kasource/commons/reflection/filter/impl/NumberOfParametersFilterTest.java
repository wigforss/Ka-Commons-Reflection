package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NumberOfParametersFilterTest {


    private NumberOfParametersFilter filter = new NumberOfParametersFilter(1);

    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getName");
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passConstructorTrue() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalse() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class, int.class);
        assertThat(filter.apply(cons), is(false));
    }



    private static class MyClass {
        @SuppressWarnings("unused")
        private String name;

        @SuppressWarnings("unused")
        public MyClass(String name) {
            this.name = name;
        }

        @SuppressWarnings("unused")
        public MyClass(String name, int age) {
            this.name = name;
        }

        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }

        @SuppressWarnings("unused")
        public void setName(String name) {
            this.name = name;
        }
    }
}
