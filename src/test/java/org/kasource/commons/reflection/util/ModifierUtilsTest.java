package org.kasource.commons.reflection.util;


import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ModifierUtilsTest {

    @Test
    public void privateStaticFinal() {
        int modifiers =  Modifier.FINAL | Modifier.PRIVATE | Modifier.STATIC;
        String result = ModifierUtils.toString(modifiers);
        assertThat(result, equalTo("private static final"));
    }


    @Test
    public void publicAbstractSynchronizedNative() {
        int modifiers =  Modifier.NATIVE | Modifier.SYNCHRONIZED |Modifier.ABSTRACT | Modifier.PUBLIC;
        String result = ModifierUtils.toString(modifiers);
        assertThat(result, equalTo("public abstract synchronized native"));
    }

    @Test
    public void protectedTransientVolatile() {
        int modifiers =  Modifier.TRANSIENT | Modifier.PROTECTED | Modifier.VOLATILE;
        String result = ModifierUtils.toString(modifiers);
        assertThat(result, equalTo("protected transient volatile"));
    }

    @Test
    public void staticInterface() {
        int modifiers =  Modifier.STATIC | Modifier.INTERFACE;
        String result = ModifierUtils.toString(modifiers);
        assertThat(result, equalTo("static interface"));
    }

}
