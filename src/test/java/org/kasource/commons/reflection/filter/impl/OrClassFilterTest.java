package org.kasource.commons.reflection.filter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrClassFilterTest {

    @Mock
    private ClassFilter left;

    @Mock
    private ClassFilter right;


    @InjectMocks
    private OrClassFilter filter = new OrClassFilter(left, right);


    @Test
    public void passTrue1() {
        when(left.apply(Integer.class)).thenReturn(false);
        when(right.apply(Integer.class)).thenReturn(true);

        assertThat(filter.apply(Integer.class), is(true));
    }

    @Test
    public void passTrue2() {
        when(left.apply(Integer.class)).thenReturn(true);

        assertThat(filter.apply(Integer.class), is(true));
    }


    @Test
    public void passFalse() {
        when(left.apply(Integer.class)).thenReturn(false);
        when(right.apply(Integer.class)).thenReturn(false);

        assertThat(filter.apply(Integer.class), is(false));
    }
}
