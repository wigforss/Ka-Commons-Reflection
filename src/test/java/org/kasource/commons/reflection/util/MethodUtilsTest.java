package org.kasource.commons.reflection.util;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class MethodUtilsTest {


    @Test
    public void hasMethodNoReturnTypeTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        assertThat(MethodUtils.hasMethodNoReturnType(method), is(true));
    }

    @Test
    public void hasMethodNoReturnTypeFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getName");
        assertThat(MethodUtils.hasMethodNoReturnType(method), is(false));

    }

    @Test
    public void verifyMethodSignature() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        MethodUtils.verifyMethodSignature(method, Void.TYPE, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMethodSignatureNullMethod() throws SecurityException, NoSuchMethodException {

        MethodUtils.verifyMethodSignature(null, Void.TYPE, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMethodSignatureWrongParamType() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        MethodUtils.verifyMethodSignature(method, Void.TYPE, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMethodSignatureTooManyParams() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        MethodUtils.verifyMethodSignature(method, Void.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMethodSignatureTooFewParams() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        MethodUtils.verifyMethodSignature(method, Void.TYPE, String.class, int.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMethodSignatureWrongReturnType() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        MethodUtils.verifyMethodSignature(method, String.class, String.class);
    }

    private static class MyClass {
        public void setName(String name) {
        }

        public String getName() {
            return "name";
        }
    }


}
