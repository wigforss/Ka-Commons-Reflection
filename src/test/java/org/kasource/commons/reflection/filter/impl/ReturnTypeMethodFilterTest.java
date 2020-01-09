package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReturnTypeMethodFilterTest {


    private ReturnTypeMethodFilter filter = new ReturnTypeMethodFilter(String.class);

    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getName");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        assertThat(filter.apply(method), is(false));
    }

    private static class MyClass {
        private String name;

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
