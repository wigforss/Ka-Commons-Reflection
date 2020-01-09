package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class AssignableToParameterFilterTest {


    private AssignableToParameterFilter filter = new AssignableToParameterFilter(0, ArrayList.class);

    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("methodWithListParameter", List.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalseNoSuchParameterIndex() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParameter");
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("methodWithIntParameter", Integer.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodTrueTwoParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{ArrayList.class, Integer.class}), filter, "assignableTo");
        Method method = MyClass.class.getMethod("methodWithListAndIntegerParameter", List.class, Integer.class);
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalseTooFewParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{ArrayList.class, Integer.class}), filter, "assignableTo");
        Method method = MyClass.class.getMethod("methodTooManayParameter", ArrayList.class, Integer.class, List.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passMethodFalseTooManyParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{List.class, Integer.class}), filter, "assignableTo");
        Method method = MyClass.class.getMethod("methodWithListParameter", List.class);
        assertThat(filter.apply(method), is(false));
    }

    @Test
    public void passConstructorTrue() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(List.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalseNoSuchParameterIndex() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(null);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passConstructorFalse() throws SecurityException, NoSuchMethodException {
        Constructor<?> cons = MyClass.class.getConstructor(Integer.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passConstructorTrueTwoParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{ArrayList.class, Integer.class}), filter, "assignableTo");
        Constructor<?> cons = MyClass.class.getConstructor(List.class, Integer.class);
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalseTooFewParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{ArrayList.class, Integer.class}), filter, "assignableTo");
        Constructor<?> cons = MyClass.class.getConstructor(ArrayList.class, Integer.class, List.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passConstructorFalseTooManyParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignableToClass");
        InjectionUtils.injectInto(Arrays.asList(new Class[]{List.class, Integer.class}), filter, "assignableTo");
        Constructor<?> cons = MyClass.class.getConstructor(List.class);
        assertThat(filter.apply(cons), is(false));
    }



    private static class MyClass {

        public MyClass(List<String> list) {
        }

        public MyClass(Integer age) {
        }

        public MyClass(List<String> list, Integer age) {
        }

        public MyClass(ArrayList<String> list, Integer number, List<String> list2) {
        }

        public MyClass() {
        }

        @SuppressWarnings("unused")
        public void methodWithListParameter(List<String> list) {
        }

        @SuppressWarnings("unused")
        public void methodWithIntParameter(Integer number) {
        }

        @SuppressWarnings("unused")
        public void methodWithListAndIntegerParameter(List<String> list, Integer number) {
        }

        @SuppressWarnings("unused")
        public void methodTooManayParameter(ArrayList<String> list, Integer number, List<String> list2) {
        }

        @SuppressWarnings("unused")
        public void noParameter() {
        }
    }
}
