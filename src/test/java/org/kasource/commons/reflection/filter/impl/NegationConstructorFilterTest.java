package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NegationConstructorFilterTest {

    @Mock
    private ConstructorFilter methodFilter;

    @InjectMocks
    private NegationConstructorFilter filter = new NegationConstructorFilter(methodFilter);


    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        when(methodFilter.apply(cons)).thenReturn(false);

        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        when(methodFilter.apply(cons)).thenReturn(true);


        assertThat(filter.apply(cons), is(false));
    }

    private static class MyClass {
        @SuppressWarnings("unused")
        public MyClass(String msg) {
        }
    }
}
