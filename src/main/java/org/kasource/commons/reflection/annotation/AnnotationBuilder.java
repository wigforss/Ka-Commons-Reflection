package org.kasource.commons.reflection.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.sf.cglib.proxy.Enhancer;
import org.kasource.commons.reflection.annotation.cglib.AnnotationMethodInterceptor;

public class AnnotationBuilder<T extends Annotation> {
    private static final String ERROR_MESSAGE = "Invalid attribute type: %s, attribute '%s"
            + "' of @%s should be %s";
    private static final String DEFAULT_ATTRIBUTE = "value";

    private Class<T> annotationClass;
    private Map<String, Method> attributes = new HashMap<>();
    private Map<String, Object> attributeData = new HashMap<>();

    public AnnotationBuilder(final Class<T> annotationClass) {
        this.annotationClass = annotationClass;
        setAttributes();
        setAttributeData();
    }

    public AnnotationBuilder(final Class<T> annotationClass, final Object value) {
        this(annotationClass);
        validateAttributeName(DEFAULT_ATTRIBUTE);
        validateAttributeType(DEFAULT_ATTRIBUTE, value.getClass());
        setAttributeData(DEFAULT_ATTRIBUTE, value);
    }

    public AnnotationBuilder<T> value(Object value) {
        validateAttributeName(DEFAULT_ATTRIBUTE);
        validateAttributeType(DEFAULT_ATTRIBUTE, value.getClass());
        setAttributeData(DEFAULT_ATTRIBUTE, value);
        return this;
    }

    public AnnotationBuilder<T> attr(String name, Object value) {
        validateAttributeName(name);
        validateAttributeType(name, value.getClass());
        setAttributeData(name, value);
        return this;
    }


    @SuppressWarnings("unchecked")
    public T build() {
        validateAttributeData();
        return (T) Enhancer.create(annotationClass, new AnnotationMethodInterceptor(annotationClass, attributes, attributeData));
    }

    private void setAttributeData(String name, Object value) {
        Method method = attributes.get(name);
        Class<?> returnType = method.getReturnType();
        if (returnType.isArray() && !value.getClass().isArray()) {
            Object array = Array.newInstance(returnType.getComponentType(), 1);
            Array.set(array, 0, value);
            attributeData.put(name, array);
        } else {
            attributeData.put(name, value);
        }
    }

    private void validateAttributeName(String name) {
        if (!attributes.containsKey(name)) {
            throw new IllegalArgumentException("Invalid attribute: @" + annotationClass.getName()
                    + " does not have any attribute named '" + name + "'");
        }
    }

    private void validateAttributeType(String name, Class<?> type) {
        Method method = attributes.get(name);
        Class<?> returnType = method.getReturnType();
        if (returnType.isArray()) {
            validateArrayReturnType(name, type, returnType);
        } else if (returnType.isPrimitive()) {
            validatePrimitiveReturnType(name, type, returnType);
        } else {
            if (!returnType.isAssignableFrom(type)) {
                throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                        type.getName(),
                        name,
                        annotationClass.getName(),
                        returnType.getName()));
            }

        }

    }

    private void validatePrimitiveReturnType(String name, Class<?> type, Class<?> returnType) {
        if (type.isPrimitive()) {
            if (!returnType.equals(type)) {
                throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                        type.getName(),
                        name,
                        annotationClass.getName(),
                        returnType.getName()));
            }
        } else {
            // If wrapper class TYPE field should be present
            try {
                Class wrappedType = (Class) type.getField("TYPE").get(null);
                if (!returnType.equals(wrappedType)) {
                    throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                            type.getName(),
                            name,
                            annotationClass.getName(),
                            returnType.getName()));
                }
            } catch (final NoSuchFieldException | IllegalAccessException  e) {
                throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                        type.getName(),
                        name,
                        annotationClass.getName(),
                        returnType.getName()), e);
            }
        }
    }

    private void validateArrayReturnType(String name, Class<?> type, Class<?> returnType) {
        if (!type.isArray() && !returnType.getComponentType().isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                    type.getName(),
                    name,
                    annotationClass.getName(),
                    returnType.getComponentType().getName()) + "[]");
        } else if (type.isArray() && !returnType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE,
                    type.getComponentType().getName() + "[]",
                    name,
                    annotationClass.getName(),
                    returnType.getComponentType().getName() + "[]"));
        }
    }

    private void validateAttributeData() {
        Optional<Map.Entry<String, Object>> missingAttributeValue = attributeData.entrySet().stream()
                .filter(e -> e.getValue() == null)
                .findFirst();

        if (missingAttributeValue.isPresent()) {
            throw new IllegalStateException("Missing attribute value: attribute '" + missingAttributeValue.get().getKey()
                    + "' of @" + annotationClass.getName() + " has not been set");
        }
    }

    private void setAttributes() {
        Arrays.stream(annotationClass.getMethods()).forEach(this::setAttribute);
    }

    @SuppressWarnings("checkstyle:booleanexpressioncomplexity")
    private void setAttribute(Method method) {
        if (method.getParameterTypes().length == 0
                && !method.getName().equals("annotationType")
                && !method.getName().equals("hashCode")
                && !method.getName().equals("toString")
                && !method.getReturnType().equals(Void.TYPE)) {

            attributes.put(method.getName(), method);
        }
    }

    private void setAttributeData() {
        attributes.entrySet().stream()
                .forEach(e -> attributeData.put(e.getKey(), e.getValue().getDefaultValue()));
    }
}
