package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class IsEnumConstantFieldFilterTest {


    private IsEnumConstantFieldFilter filter = new IsEnumConstantFieldFilter();

    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyEnum.class.getField("FIRST");

        assertThat(filter.apply(field), is(true));
    }

    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");

        assertThat(filter.apply(field), is(false));
    }

    enum MyEnum {FIRST, SECOND}

    ;

    private static class MyClass {
        @SuppressWarnings("unused")
        public ElementType elementType = ElementType.ANNOTATION_TYPE;

        @SuppressWarnings("unused")
        public int number;

    }
}
