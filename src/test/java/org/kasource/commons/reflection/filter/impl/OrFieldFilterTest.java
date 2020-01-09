package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrFieldFilterTest {


    @Mock
    private FieldFilter left;

    @Mock
    private FieldFilter right;

    @InjectMocks
    private OrFieldFilter filter = new OrFieldFilter(left, right);

    @Test
    public void passTrueLeft() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        when(left.apply(field)).thenReturn(true);


        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passTrueRight() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        when(left.apply(field)).thenReturn(false);
        when(right.apply(field)).thenReturn(true);


        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        when(left.apply(field)).thenReturn(false);
        when(right.apply(field)).thenReturn(false);


        assertThat(filter.apply(field), is(false));
    }

    private static class MyClass {

        @SuppressWarnings("unused")
        public int number;

    }
}
