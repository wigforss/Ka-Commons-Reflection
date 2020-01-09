package org.kasource.commons.reflection.filter.impl;

import java.lang.annotation.Retention;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsAnnotationClassFilterTest {


    private IsAnnotationClassFilter filter = new IsAnnotationClassFilter();

    @Test
    public void passTrue() {
        assertThat(filter.apply(Retention.class), is(true));
    }

    @Test
    public void passFalse() {
        assertThat(filter.apply(Integer.class), is(false));
    }
}
