package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ParameterClassFilterFilterTest {

    @Mock
    private ClassFilter classFilter;

    @Mock
    private ClassFilter classFilter2;


    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        when(classFilter.apply(int.class)).thenReturn(true);

        Method method = MyClass.class.getMethod("oneParamter", int.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        when(classFilter.apply(int.class)).thenReturn(false);

        Method method = MyClass.class.getMethod("oneParamter", int.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodFalseTooFewParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        Method method = MyClass.class.getMethod("noParamters");
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodTrueTwoParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(classFilter, classFilter2);

        when(classFilter.apply(int.class)).thenReturn(true);
        when(classFilter2.apply(String.class)).thenReturn(true);

        Method method = MyClass.class.getMethod("twoParamters", int.class, String.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalseTooManyParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(classFilter, classFilter2);
        Method method = MyClass.class.getMethod("threeParamters", int.class, int.class, int.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodTrueEmptyFilters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter();
        Method method = MyClass.class.getMethod("noParamters");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passConstructorTrue() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        when(classFilter.apply(String.class)).thenReturn(true);

        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalse() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        when(classFilter.apply(String.class)).thenReturn(false);

        Constructor<?> cons = MyClass.class.getConstructor(String.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passConstructorFalseTooFewParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(0, classFilter);
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passConstructorTrueTwoParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(classFilter, classFilter2);

        when(classFilter.apply(String.class)).thenReturn(true);
        when(classFilter2.apply(int.class)).thenReturn(true);

        Constructor<?> cons = MyClass.class.getConstructor(String.class, int.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalseTooManyParameters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter(classFilter, classFilter2);
        Constructor<?> cons = MyClass.class.getConstructor(String.class, int.class, int.class);
        assertThat(filter.apply(cons), is(false));
    }


    @Test
    public void passConstructorTrueEmptyFilters() throws SecurityException, NoSuchMethodException {
        ParameterClassFilterFilter filter = new ParameterClassFilterFilter();
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(true));
    }



    private static class MyClass {
        public MyClass() {
        }

        public MyClass(String name) {
        }

        public MyClass(String name, int age) {
        }

        public MyClass(String name, int age, int length) {
        }

        @SuppressWarnings("unused")
        public void oneParamter(int number) {
        }

        @SuppressWarnings("unused")
        public void twoParamters(int number, String string) {
        }

        @SuppressWarnings("unused")
        public void threeParamters(int number1, int number2, int number3) {
        }

        @SuppressWarnings("unused")
        public void noParamters() {
        }
    }
}
