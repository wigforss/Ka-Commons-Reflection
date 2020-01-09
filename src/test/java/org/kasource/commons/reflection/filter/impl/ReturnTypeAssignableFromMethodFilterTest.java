package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ReturnTypeAssignableFromMethodFilterTest {


    private ReturnTypeAssignableFromMethodFilter filter = new ReturnTypeAssignableFromMethodFilter(List.class);

    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getNames");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setNames", ArrayList.class);
        assertThat(filter.apply(method), is(false));
    }

    private static class MyClass {
        private ArrayList<String> names;

        @SuppressWarnings("unused")
        public ArrayList<String> getNames() {
            return names;
        }

        @SuppressWarnings("unused")
        public void setNames(ArrayList<String> names) {
            this.names = names;
        }
    }

}
