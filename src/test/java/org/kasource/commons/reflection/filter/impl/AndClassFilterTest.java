package org.kasource.commons.reflection.filter.impl;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class AndClassFilterTest {

    @Mock
    private ClassFilter classFilter1;

    @Mock
    private ClassFilter classFilter2;

    private ClassFilter[] filters = {};


    private AndClassFilter filter = new AndClassFilter(filters);

    @Test
    public void testNullFilter() {
        assertThat(filter.apply(Integer.class), is(true));
    }

    @Test
    public void testEmptyFilter() {
        ClassFilter[] filterArray = {};
        InjectionUtils.injectInto(Arrays.asList(filterArray), filter, "filters");

        assertThat(filter.apply(Integer.class), is(true));
    }

    @Test
    public void testTwoFilterSuccessFilter() {
        ClassFilter[] filterArray = {classFilter1, classFilter2};
        InjectionUtils.injectInto(Arrays.asList(filterArray), filter, "filters");
        when(classFilter1.apply(Integer.class)).thenReturn(true);
        when(classFilter2.apply(Integer.class)).thenReturn(true);

        assertThat(filter.apply(Integer.class), is(true));
    }

    @Test
    public void testTwoFilterNonPassFilter() {
        ClassFilter[] filterArray = {classFilter1, classFilter2};
        InjectionUtils.injectInto(Arrays.asList(filterArray), filter, "filters");
        when(classFilter1.apply(Integer.class)).thenReturn(true);
        when(classFilter2.apply(Integer.class)).thenReturn(false);

        assertThat(filter.apply(Integer.class), is(false));
    }

    @Test
    public void describe() {
        ClassFilter[] filterArray = {classFilter1, classFilter2};
        InjectionUtils.injectInto(Arrays.asList(filterArray), filter, "filters");

        when(classFilter1.describe()).thenReturn("classFilter1");
        when(classFilter2.describe()).thenReturn("classFilter2");

        assertThat(filter.describe(), equalTo("classFilter1 and classFilter2"));
    }
}
