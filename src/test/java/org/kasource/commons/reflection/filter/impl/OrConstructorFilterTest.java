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
public class OrConstructorFilterTest {
    @Mock
    private ConstructorFilter left;

    @Mock
    private ConstructorFilter right;

    @InjectMocks
    private OrConstructorFilter filter = new OrConstructorFilter(left, right);

    @Test
    public void passTrueRight() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        when(left.apply(cons)).thenReturn(false);
        when(right.apply(cons)).thenReturn(true);

        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passTrueLeft() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        when(left.apply(cons)).thenReturn(true);

        assertThat(filter.apply(cons), is(true));
    }


    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        when(left.apply(cons)).thenReturn(false);
        when(right.apply(cons)).thenReturn(false);


        assertThat(filter.apply(cons), is(false));
    }

    private static class MyClass {

        public MyClass(String name) {
        }
    }
}
