package org.kasource.commons.reflection.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import org.kasource.commons.reflection.filter.builder.ConstructorFilterBuilder;


public class ConstructorUtilsTest {

    @Test
    public void newInstanceTest() {
        List list = ConstructorUtils.newInstance("java.util.ArrayList", List.class);
        assertThat(list, notNullValue());
        assertThat(list, is(instanceOf(ArrayList.class)));
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceTestUnknownClass() {
        ConstructorUtils.newInstance("org.rikard.MyClass", List.class);
    }

    @Test(expected = ClassCastException.class)
    public void newInstanceTestWrongTypeClass() {
        ConstructorUtils.newInstance("java.lang.Integer", List.class, new Class<?>[]{int.class}, new Object[]{5});
    }

    @Test
    public void newInstanceOneParamTest() {
        Number number = ConstructorUtils.newInstance("java.lang.Integer", Number.class, new Class<?>[]{int.class}, new Object[]{5});
        assertThat(number, notNullValue());
        assertThat(number, is(instanceOf(Integer.class)));
        assertThat(number.intValue(), is(5));
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceNoDefaultConstructor() {
        ConstructorUtils.newInstance("java.lang.Integer", Number.class);
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceOneParamWrongTypeTest() {
        ConstructorUtils.newInstance("java.lang.Integer", Number.class, new Class<?>[]{List.class}, new Object[]{new ArrayList<Integer>()});
    }

    @Test
    public void findFirstConstructor() throws NoSuchMethodException {
       Constructor<Integer> constructor = ConstructorUtils.findFirstConstructor(Integer.class, new ConstructorFilterBuilder().isPublic().hasSignature(int.class).build());
       assertThat(constructor, equalTo(Integer.class.getConstructor(int.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findFirstConstructorNotFound() throws NoSuchMethodException {
        Constructor<Integer> constructor = ConstructorUtils.findFirstConstructor(Integer.class, new ConstructorFilterBuilder().isPublic().hasSignature(List.class).build());
        assertThat(constructor, equalTo(Integer.class.getConstructor(int.class)));
    }

    @Test
    public void findConstructors() throws NoSuchMethodException {
        Set<Constructor<Integer>> constructors = ConstructorUtils.findConstructors(Integer.class, new ConstructorFilterBuilder().isPublic().build());
        assertThat(constructors, hasItem(equalTo(Integer.class.getConstructor(int.class))));
        assertThat(constructors, hasItem(equalTo(Integer.class.getConstructor(String.class))));
    }
}
