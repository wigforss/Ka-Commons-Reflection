package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.kasource.commons.reflection.filter.MethodFilter;


@RunWith(MockitoJUnitRunner.class)
public class NegationMethodFilterTest {


    @Mock
    private MethodFilter methodFilter;


    @InjectMocks
    private NegationMethodFilter filter = new NegationMethodFilter(methodFilter);


    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        when(methodFilter.apply(method)).thenReturn(false);

        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        when(methodFilter.apply(method)).thenReturn(true);


        assertThat(filter.apply(method), is(false));
    }

    private static class MyClass {
        @SuppressWarnings("unused")
        public void noParamters() {
        }
    }
}
