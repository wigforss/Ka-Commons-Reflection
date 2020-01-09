package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssignableFromTypeFilterTest {


    private AssignableFromTypeFilter filter = new AssignableFromTypeFilter(List.class);

    @Test
    public void passClassTrue() {
        assertThat(filter.apply(ArrayList.class), is(true));
    }

    @Test
    public void passClassFalse() {
        assertThat(filter.apply(HashSet.class), is(false));
    }

    @Test
    public void passFieldTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("list");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("nonList");
        assertThat(filter.apply(field), is(false));
    }


    private static class MyClass {

        @SuppressWarnings("unused")
        public ArrayList<String> list = new ArrayList<String>();
        @SuppressWarnings("unused")
        public int nonList;
    }
}
