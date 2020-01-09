package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeClassFilterTest {

    @Mock
    private ClassFilter classFilter;


    @InjectMocks
    private TypeClassFilter filter = new TypeClassFilter(classFilter);

    @Test
    public void passFieldTrue() throws SecurityException, NoSuchFieldException {
        when(classFilter.apply(int.class)).thenReturn(true);

        Field field = MyClass.class.getField("number");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        when(classFilter.apply(int.class)).thenReturn(false);

        Field field = MyClass.class.getField("number");
        assertThat(filter.apply(field), is(false));
    }

    @Test
    public void passClassTrue() throws SecurityException, NoSuchFieldException {
        when(classFilter.apply(int.class)).thenReturn(true);

        assertThat(filter.apply(int.class), is(true));
    }

    @Test
    public void passClassFalse() {
        when(classFilter.apply(int.class)).thenReturn(false);


        assertThat(filter.apply(int.class), is(false));
    }


    private static class MyClass {

        @SuppressWarnings("unused")
        public int number;

    }
}
