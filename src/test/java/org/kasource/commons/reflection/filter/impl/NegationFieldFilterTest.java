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
public class NegationFieldFilterTest {


    @Mock
    private FieldFilter fieldFilter;

    @InjectMocks
    private NegationFieldFilter filter = new NegationFieldFilter(fieldFilter);

    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        when(fieldFilter.apply(field)).thenReturn(false);


        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        when(fieldFilter.apply(field)).thenReturn(true);


        assertThat(filter.apply(field), is(false));
    }

    private static class MyClass {

        @SuppressWarnings("unused")
        public int number;

    }
}
