package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class OrMethodFilterTest {

    @Mock
    private MethodFilter left;

    @Mock
    private MethodFilter right;

    @Mock
    private MethodFilter other;

    @Mock
    private MethodFilter other2;

    @InjectMocks
    private OrMethodFilter filter = new OrMethodFilter(left, right);

    @Test
    public void passTrueRight() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        when(left.apply(method)).thenReturn(false);
        when(right.apply(method)).thenReturn(true);

        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passTrueLeft() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        when(left.apply(method)).thenReturn(true);

        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passTrueTheRest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{other}, filter, "rest");
        Method method = MyClass.class.getMethod("noParamters");
        when(left.apply(method)).thenReturn(false);
        when(right.apply(method)).thenReturn(false);
        when(other.apply(method)).thenReturn(true);

        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passTrueTheRest2() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{other, other2}, filter, "rest");
        Method method = MyClass.class.getMethod("noParamters");
        when(left.apply(method)).thenReturn(false);
        when(right.apply(method)).thenReturn(false);
        when(other.apply(method)).thenReturn(false);
        when(other2.apply(method)).thenReturn(true);

        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        when(left.apply(method)).thenReturn(false);
        when(right.apply(method)).thenReturn(false);


        assertThat(filter.apply(method), is(false));
    }

    private static class MyClass {
        @SuppressWarnings("unused")
        public void noParamters() {
        }
    }
}
