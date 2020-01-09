package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class AndMethodFilterTest {

    private MethodFilter[] filters;

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private MethodFilter methodFilter2;

    @InjectMocks
    private AndMethodFilter filter = new AndMethodFilter(filters);

    @Test
    public void nullFilterTest() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParameters");
        assertThat(filter.apply(method), is(true));

    }

    @Test
    public void emptpyFilterTest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new MethodFilter[]{}), filter, "filters");
        Method method = MyClass.class.getMethod("noParameters");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new MethodFilter[]{methodFilter, methodFilter2}), filter, "filters");
        Method method = MyClass.class.getMethod("noParameters");
        when(methodFilter.apply(method)).thenReturn(true);
        when(methodFilter2.apply(method)).thenReturn(true);


        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFqlse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new MethodFilter[]{methodFilter, methodFilter2}), filter, "filters");
        Method method = MyClass.class.getMethod("noParameters");
        when(methodFilter.apply(method)).thenReturn(false);


        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passFalseSecondFilterFalse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new MethodFilter[]{methodFilter, methodFilter2}), filter, "filters");
        Method method = MyClass.class.getMethod("noParameters");
        when(methodFilter.apply(method)).thenReturn(true);
        when(methodFilter2.apply(method)).thenReturn(false);


        assertThat(filter.apply(method), is(false));
    }


    @Test
    public void describe() {
        InjectionUtils.injectInto(Arrays.asList(new MethodFilter[]{methodFilter, methodFilter2}), filter, "filters");

        when(methodFilter.describe()).thenReturn("methodFilter");
        when(methodFilter2.describe()).thenReturn("methodFilter2");

        assertThat(filter.describe(), is(equalTo("methodFilter and methodFilter2")));
    }

    private static class MyClass {
        @SuppressWarnings("unused")
        public void noParameters() {
        }
    }
}
