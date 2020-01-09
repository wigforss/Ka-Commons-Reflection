package org.kasource.commons.reflection.annotation.cglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Annotation Method Interceptor
 * <p>
 * Intercepts calls to an annotation.
 *
 * @author rikardwi
 **/
public class AnnotationMethodInterceptor implements MethodInterceptor {
    private Map<String, Object> attributeData;
    private Class<? extends Annotation> annotationType;
    private Map<String, Method> attributes;


    public AnnotationMethodInterceptor(final Class<? extends Annotation> annotationType,
                                       final Map<String, Method> attributes,
                                       final Map<String, Object> attributeData) {
        this.annotationType = annotationType;
        this.attributeData = attributeData;
        this.attributes = attributes;
    }


    /**
     * Intercept all methods calls.
     *
     * @param obj    The enhanced CGLIB instance
     * @param method Intercepted method
     * @param args   Method arguments
     * @param proxy  This method proxy
     **/
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.getName().equals("annotationType")) {
            return annotationType;
        } else if (method.getName().equals("toString")) {
            return toString();
        } else if (method.getName().equals("equals")) {
            return annotationEquals(args[0]);
        } else if (method.getName().equals("hashCode")) {
            return proxy.hashCode();
        } else {
            return attributeData.get(method.getName());
        }

    }

    /**
     * Returns true if the specified object represents an annotation that is logically equivalent to this one.
     *
     * @param object Object to inspect.
     * @return true if same annnotationType and all attributes are equal, else false.
     */
    private boolean annotationEquals(Object object) {
        if (object == null
                || !(object instanceof Annotation)
                || !annotationType.equals(((Annotation) object).annotationType())) {
            return false;
        }

        for (Map.Entry<String, Method> entry : attributes.entrySet()) {
            String methodName = entry.getKey();
            Method method = entry.getValue();
            boolean attributeEquals = compareAttribute(object, method, methodName);
            if (!attributeEquals) {
                return false;
            }

        }
        return true;
    }

    private boolean compareAttribute(Object object, Method method, String methodName) {
        try {
            Object otherValue = method.invoke(object);
            Object value = attributeData.get(methodName);
            if (!attributeEquals(value, otherValue)) {
                return false;
            }
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if two attributes are equal.
     *
     * @param value      First attribute value
     * @param otherValue Second attribute value
     * @return true if two attributes are equal.
     */
    private boolean attributeEquals(Object value, Object otherValue) {
        if (value == null) {
            return false;
        } else {
            if (value.getClass().isArray()) {
                return Arrays.equals((Object[]) value, (Object[]) otherValue);

            } else {
                return value.equals(otherValue);
            }
        }
    }

    @Override
    public String toString() {
        String typeString = "@" + annotationType.getName();
        if (attributeData.isEmpty()) {
            return typeString;
        }
        return typeString + "("
                + attributeData.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> e.getKey() + " = '" + e.getValue() + "'")
                .collect(Collectors.joining(", "))
                + ")";
    }


}

