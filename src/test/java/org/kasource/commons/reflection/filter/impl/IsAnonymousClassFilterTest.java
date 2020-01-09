package org.kasource.commons.reflection.filter.impl;

import java.util.EventListener;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsAnonymousClassFilterTest {


    private IsAnonymousClassFilter filter = new IsAnonymousClassFilter();

    @Test
    public void passTrue() {
        EventListener listener = new EventListener() {
        };
        assertThat(filter.apply(listener.getClass()), is(true));
    }

    @Test
    public void passFalse() {
        assertThat(filter.apply(Integer.class), is(false));
    }
}
