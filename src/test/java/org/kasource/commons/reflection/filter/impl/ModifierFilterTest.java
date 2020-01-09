package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModifierFilterTest {



    @Test
    public void passClassTrue() {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        assertThat(filter.apply(String.class), is(true));
    }

    @Test
    public void passClassFalse() {
        ModifierFilter filter = new ModifierFilter(Modifier.PRIVATE);

        assertThat(filter.apply(Integer.class), is(false));
    }


    @Test
    public void passFieldTrue() throws SecurityException, NoSuchFieldException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Field field = MyClass.class.getField("publicList");
        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFieldFalse() throws SecurityException, NoSuchFieldException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Field field = MyClass.class.getDeclaredField("privateNumber");
        assertThat(filter.apply(field), is(false));
    }



    @Test
    public void passConstructorTrue() throws SecurityException, NoSuchMethodException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Constructor<?> cons = MyClass.class.getConstructor();
        assertThat(filter.apply(cons), is(true));
    }

    @Test
    public void passConstructorFalse() throws SecurityException, NoSuchMethodException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Constructor<?> cons = MyClass.class.getDeclaredConstructor(String.class);
        assertThat(filter.apply(cons), is(false));
    }

    @Test
    public void passMethodTrue() throws SecurityException, NoSuchMethodException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Method method = MyClass.class.getMethod("publicMethod");
        assertThat(filter.apply(method), is(true));
    }

    @Test
    public void passMethodFalse() throws SecurityException, NoSuchMethodException {
        ModifierFilter filter = new ModifierFilter(Modifier.PUBLIC);
        Method method = MyClass.class.getDeclaredMethod("privateMethod");
        assertThat(filter.apply(method), is(false));
    }


    private static class MyClass {

        @SuppressWarnings("unused")
        public ArrayList<String> publicList = new ArrayList<String>();
        @SuppressWarnings("unused")
        private int privateNumber;

        public MyClass() {
        }


        private MyClass(String name) {
        }

        @SuppressWarnings("unused")
        public void publicMethod() {
        }

        @SuppressWarnings("unused")
        private void privateMethod() {
        }
    }

}
