package org.kasource.commons.reflection.annotation.cglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.when;

import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.collection.builder.SetBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationMethodInterceptorTest {

    @Mock
    private Map<String, Object> attributeData;


    @Mock
    private Set<Map.Entry<String, Object>> attributeDataEntrySet;

    @Mock
    private Iterator<Map.Entry<String, Object>> attributDataIterator;

    @Mock
    private Map.Entry<String, Object> attributDataEntry1;

    @Mock
    private Map.Entry<String, Object> attributDataEntry2;

    private Class<? extends Annotation> annotationType = Resource.class;

    @Mock
    private Map<String, Method> attributes;

    @Mock
    private Set<Map.Entry<String, Method>> attributesEntrySet;

    @Mock
    private Iterator<Map.Entry<String, Method>> attributeIterator;

    @Mock
    private Map.Entry<String, Method> attributeEntry;

    @Mock
    private Object obj;

    @Mock
    private MethodProxy proxy;

    @Mock
    private RuntimeException runtimeException;

    @InjectMocks
    private AnnotationMethodInterceptor interceptor = new AnnotationMethodInterceptor(annotationType, attributes, attributeData);


    @Test
    public void annotationTypeTest() throws Throwable {
        Method method = MyClass.class.getMethod("annotationType");
        Object[] args = {};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) annotationType));
    }

    @Test
    public void toStringTest() throws Throwable {
        Method method = MyClass.class.getMethod("toString");
        Object[] args = {};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) ("@" + annotationType.getName() + "()")));
    }

    @Test
    public void toStringWithAttributesTest() throws Throwable {
        Method method = MyClass.class.getMethod("toString");
        Object[] args = {};
        when(attributeData.size()).thenReturn(2);
        when(attributeData.entrySet()).thenReturn(new SetBuilder<Map.Entry<String, Object>>().add(attributDataEntry1,attributDataEntry2).build());
        when(attributDataEntry1.getKey()).thenReturn("name");
        when(attributDataEntry1.getValue()).thenReturn("nameAttributeValue");
        when(attributDataEntry2.getKey()).thenReturn("description");
        when(attributDataEntry2.getValue()).thenReturn("descriptionAttributeValue");

        String toString = (String) interceptor.intercept(obj, method, args, proxy);

        assertThat(toString, startsWith("@" + annotationType.getName() + "("));
        assertThat(toString, containsString("name = 'nameAttributeValue'"));
        assertThat(toString, containsString("description = 'descriptionAttributeValue'"));
        assertThat(toString, endsWith(")"));
    }

    @Test
    public void equalsTrue() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getAnnotation(Resource.class)};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) true));
    }

    @Test
    public void equalsWithAttributesTrue() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getMethod("annotationType").getAnnotation(Resource.class)};

        when(attributes.entrySet()).thenReturn(attributesEntrySet);
        when(attributesEntrySet.iterator()).thenReturn(attributeIterator);
        when(attributeIterator.hasNext()).thenReturn(true, false);
        when(attributeIterator.next()).thenReturn(attributeEntry);
        when(attributeEntry.getKey()).thenReturn("name");
        when(attributeEntry.getValue()).thenReturn(Resource.class.getMethod("name"));
        when(attributeData.get("name")).thenReturn("annotationType");

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) true));
    }

    @Test
    public void equalsWithAttributesFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getMethod("annotationType").getAnnotation(Resource.class)};

        when(attributes.entrySet()).thenReturn(attributesEntrySet);
        when(attributesEntrySet.iterator()).thenReturn(attributeIterator);
        when(attributeIterator.hasNext()).thenReturn(true, false);
        when(attributeIterator.next()).thenReturn(attributeEntry);
        when(attributeEntry.getKey()).thenReturn("name");
        when(attributeEntry.getValue()).thenReturn(Resource.class.getMethod("name"));
        when(attributeData.get("name")).thenReturn("otherValue");

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }

    @Test
    public void equalsWithAttributesNullValueFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getMethod("annotationType").getAnnotation(Resource.class)};

        when(attributes.entrySet()).thenReturn(attributesEntrySet);
        when(attributesEntrySet.iterator()).thenReturn(attributeIterator);
        when(attributeIterator.hasNext()).thenReturn(true, false);
        when(attributeIterator.next()).thenReturn(attributeEntry);
        when(attributeEntry.getKey()).thenReturn("name");
        when(attributeEntry.getValue()).thenReturn(Resource.class.getMethod("name"));
        when(attributeData.get("name")).thenReturn(null);

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }

    @Test
    public void equalsOnExceptionFalse() throws Throwable {
        Method invalidMethod = String.class.getMethod("length");

        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getMethod("annotationType").getAnnotation(Resource.class)};
        when(attributes.entrySet()).thenReturn(attributesEntrySet);
        when(attributesEntrySet.iterator()).thenReturn(attributeIterator);
        when(attributeIterator.hasNext()).thenReturn(true, false);
        when(attributeIterator.next()).thenReturn(attributeEntry);
        when(attributeEntry.getKey()).thenReturn("name");
        when(attributeEntry.getValue()).thenReturn(invalidMethod);

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }

    @Test
    public void equalsFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {MyClass.class.getMethod("annotationType").getAnnotation(Resource.class)};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) true));
    }


    @Test
    public void equalsNullValueFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {null};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }

    @Test
    public void equalsOtherTypeValueFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {"String value"};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }

    @Test
    public void equalsOtherAnnotationTypeValueFalse() throws Throwable {
        Method method = MyClass.class.getMethod("equals", Object.class);
        Object[] args = {this.getClass().getMethod("equalsOtherTypeValueFalse").getAnnotation(Test.class)};

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) false));
    }


    @Test
    public void hashCodeTest() throws Throwable {
        Method method = MyClass.class.getMethod("hashCode");
        Object[] args = {};
        int hashCode = proxy.hashCode();

        assertThat(interceptor.intercept(obj, method, args, proxy), equalTo((Object) hashCode));
    }


    @Test
    public void methodTest() throws Throwable {

        MyClass myClass = new MyClass();
        Method method = MyClass.class.getMethod("attributeMethod");
        Object[] args = {};

        when(attributeData.get("attributeMethod")).thenReturn(true);

        assertThat(interceptor.intercept(myClass, method, args, proxy), equalTo((Object) true));

    }

    @Resource
    private static class MyClass {

        @Resource(name = "annotationType")
        public void annotationType() {

        }


        public boolean attributeMethod() {
            return true;
        }

        @Override
        public String toString() {
            return "MyClass";
        }

        @Override
        public boolean equals(Object otherObject) {
            return true;
        }

        @Override
        public int hashCode() {
            return 5;
        }

        public String name() {
            return "annotationType";
        }
    }

}
