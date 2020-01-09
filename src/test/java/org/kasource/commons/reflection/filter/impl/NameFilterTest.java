package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NameFilterTest {


    @Test
    public void passClassTrue() {
        NameFilter filter = new NameFilter("java\\.lang\\..*");
        assertThat(filter.apply(String.class), is(true));
    }

    @Test
    public void passClassFalse() {
        NameFilter filter = new NameFilter("filter");
        assertThat(filter.apply(Integer.class), is(false));
    }

    @Test
    public void passFieldTrue() throws SecurityException, NoSuchFieldException {
        NameFilter filter = new NameFilter(".*List.*");
        Field field = MyClass.class.getField("myList");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        NameFilter filter = new NameFilter(".*List.*");
        Field field = MyClass.class.getDeclaredField("myInt");
        assertThat(filter.apply(field), is(false));
    }


    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        NameFilter filter = new NameFilter("get[A-Z]\\w*");
        Method method = MyClass.class.getMethod("getMyList");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        NameFilter filter = new NameFilter("get[A-Z]\\w*");
        Method method = MyClass.class.getMethod("setMyList", List.class);
        assertThat(filter.apply(method), is(false));
    }

    private static class MyClass {
        @SuppressWarnings("unused")
        public List<String> myList;
        @SuppressWarnings("unused")
        private int myInt;

        @SuppressWarnings("unused")
        public List<String> getMyList() {
            return myList;
        }

        @SuppressWarnings("unused")
        public void setMyList(List<String> myList) {
            this.myList = myList;
        }
    }

}
