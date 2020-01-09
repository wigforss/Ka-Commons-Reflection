package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;

@RunWith(MockitoJUnitRunner.class)
public class AndConstructorFilterTest {

    private ConstructorFilter[] filters;

    @Mock
    private ConstructorFilter constructorFilter;

    @Mock
    private ConstructorFilter constructorFilter2;

    @InjectMocks
    private AndConstructorFilter filter = new AndConstructorFilter(filters);

    @Test
    public void nullFilterTest() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void emptpyFilterTest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new ConstructorFilter[]{}), filter, "filters");
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new ConstructorFilter[]{constructorFilter, constructorFilter2}), filter, "filters");
        Constructor<?> cons = MyClass.class.getConstructor(null);
        when(constructorFilter.apply(cons)).thenReturn(true);
        when(constructorFilter2.apply(cons)).thenReturn(true);


        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passFqlse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new ConstructorFilter[]{constructorFilter, constructorFilter2}), filter, "filters");
        Constructor<?> cons = MyClass.class.getConstructor(null);
        when(constructorFilter.apply(cons)).thenReturn(false);


        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passFalseSecondFilterFalse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(Arrays.asList(new ConstructorFilter[]{constructorFilter, constructorFilter2}), filter, "filters");
        Constructor<?> cons = MyClass.class.getConstructor(null);
        when(constructorFilter.apply(cons)).thenReturn(true);
        when(constructorFilter2.apply(cons)).thenReturn(false);


        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void describe() {
        InjectionUtils.injectInto(Arrays.asList(new ConstructorFilter[]{constructorFilter, constructorFilter2}), filter, "filters");

        when(constructorFilter.describe()).thenReturn("constructorFilter");
        when(constructorFilter2.describe()).thenReturn("constructorFilter2");

        assertThat(filter.describe(), equalTo("constructorFilter and constructorFilter2"));
    }


    private static class MyClass {
        public MyClass() {
        }
    }
}
