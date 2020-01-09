package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class AndFieldFilterTest {

    private FieldFilter[] filters;

    @Mock
    private FieldFilter fieldFilter;

    @Mock
    private FieldFilter fieldFilter2;


    private AndFieldFilter filter = new AndFieldFilter(filters);

    @Test
    public void nullFiltersTest() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passTrueEmptyList() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(Arrays.asList(new FieldFilter[]{}), filter, "filters");

        Field field = MyClass.class.getField("number");
        assertThat(filter.apply(field), is(true));
    }


    @Test
    public void passTrueTwoFiltersFirstTrue() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(Arrays.asList(new FieldFilter[]{fieldFilter, fieldFilter2}), filter, "filters");
        Field field = MyClass.class.getField("number");
        when(fieldFilter.apply(field)).thenReturn(true);
        when(fieldFilter2.apply(field)).thenReturn(true);


        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFalseTwoFiltersFirstTrue() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(Arrays.asList(new FieldFilter[]{fieldFilter, fieldFilter2}), filter, "filters");
        Field field = MyClass.class.getField("number");
        when(fieldFilter.apply(field)).thenReturn(true);
        when(fieldFilter2.apply(field)).thenReturn(false);


        assertThat(filter.apply(field), is(false));
    }


    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(Arrays.asList(new FieldFilter[]{fieldFilter, fieldFilter2}), filter, "filters");
        Field field = MyClass.class.getField("number");
        when(fieldFilter.apply(field)).thenReturn(false);


        assertThat(filter.apply(field), is(false));
    }

    @Test
    public void describe() {
        InjectionUtils.injectInto(Arrays.asList(new FieldFilter[]{fieldFilter, fieldFilter2}), filter, "filters");

        when(fieldFilter.describe()).thenReturn("fieldFilter");
        when(fieldFilter2.describe()).thenReturn("fieldFilter2");

        assertThat(filter.describe(), is(equalTo("fieldFilter and fieldFilter2")));
    }

    private static class MyClass {

        @SuppressWarnings("unused")
        public int number;

    }
}
