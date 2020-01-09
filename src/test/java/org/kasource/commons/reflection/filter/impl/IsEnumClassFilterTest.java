package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.ElementType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsEnumClassFilterTest {


    private IsEnumClassFilter filter = new IsEnumClassFilter();

    @Test
    public void passTrue() {
        assertThat(filter.apply(ElementType.class), is(true));
    }

    @Test
    public void passFalse() {
        assertThat(filter.apply(Integer.class), is(false));
    }
}
