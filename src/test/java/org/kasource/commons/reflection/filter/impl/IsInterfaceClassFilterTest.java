package org.kasource.commons.reflection.filter.impl;

import java.util.EventListener;
import java.util.EventObject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsInterfaceClassFilterTest {


    private IsInterfaceClassFilter filter = new IsInterfaceClassFilter();

    @Test
    public void passTrue() {
        assertThat(filter.apply(EventListener.class), is(true));
    }

    @Test
    public void passFalse() {
        assertThat(filter.apply(EventObject.class), is(false));
    }
}
