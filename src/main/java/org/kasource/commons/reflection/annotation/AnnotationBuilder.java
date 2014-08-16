package org.kasource.commons.reflection.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;



import net.sf.cglib.proxy.Enhancer;

import org.kasource.commons.reflection.annotation.cglib.AnnotationMethodInterceptor;

public class AnnotationBuilder<T extends Annotation> {
    private Class<T> annotationClass;
    private Map<String, Method> attributes = new HashMap<String, Method>();
    private Map<String, Object> attributeData = new HashMap<String, Object>();
    
    public AnnotationBuilder(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
        setAttributes();
        setAttributeData();
    }
    
    public AnnotationBuilder(Class<T> annotationClass, Object value) {
       this(annotationClass);
       value(value);
    }
    
    public AnnotationBuilder<T> value(Object value) {
        validateAttributeName("value");
        validateAttributeType("value", value.getClass());
        setAttributeData("value", value);
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
        Object instance = Enhancer.create(annotationClass, new AnnotationMethodInterceptor(annotationClass, attributes, attributeData));
        return (T) instance;
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
        if(!attributes.containsKey(name)) {
            throw new IllegalArgumentException("Invalid attribute: @" + annotationClass.getName() + " does not have any attribute named '" + name + "'");
        }
    }
    
    private void validateAttributeType(String name, Class<?> type) {
        Method method = attributes.get(name);
        Class<?> returnType = method.getReturnType();
        if (returnType.isArray()) {
            if (!type.isArray() && !returnType.getComponentType().isAssignableFrom(type)) {
                throw new IllegalArgumentException("Invalid attribute type: " + type.getName() +", attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getComponentType().getName() + " or " + returnType.getComponentType().getName() + "[]");
            } else if (type.isArray() && !returnType.isAssignableFrom(type)){
                throw new IllegalArgumentException("Invalid attribute type: " + type.getComponentType().getName() + "[], attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getComponentType().getName()+"[]");
            }
        } else if (returnType.isPrimitive()){
            if (type.isPrimitive()) {
                if (!returnType.equals(type)) {
                    throw new IllegalArgumentException("Invalid attribute type: " + type.getName() + ", attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getName());
                }
            } else {
                // If wrapper class TYPE field should be present
                try {
                    Class wrappedType = (Class) type.getField("TYPE").get(null);
                    if (!returnType.equals(wrappedType)) {
                        throw new IllegalArgumentException("Invalid attribute type: " + type.getName() + ", attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getName());
                    }
                } catch (final Exception e) {
                    throw new IllegalArgumentException("Invalid attribute type: " + type.getName() + ", attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getName());
                }
            }
        } else {
            if (!returnType.isAssignableFrom(type)) {
                throw new IllegalArgumentException("Invalid attribute type: " + type.getName() + ", attribute '" + name + "' of @" + annotationClass.getName() + " should be " + returnType.getName());
            }  
            
        }
      
    }
    
    private void validateAttributeData() {
        for(Map.Entry<String, Object> data : attributeData.entrySet()) {
            if (data.getValue() == null) {
                throw new IllegalStateException("Missing attribute value: attribute '" + data.getKey() + "' of @" + annotationClass.getName() + " has not been set");
            }
        }
    }
    
    private void setAttributes() {
        for (Method method : annotationClass.getMethods()) {
            
            if (method.getParameterTypes().length == 0
                && !method.getName().equals("annotationType")
                && !method.getName().equals("hashCode")
                && !method.getName().equals("toString")
                && !method.getReturnType().equals(Void.TYPE)) {
                
                attributes.put(method.getName(), method);
            }
        }
    }
    
    private void setAttributeData() {
        for (Map.Entry<String, Method> attribute : attributes.entrySet()) {
            attributeData.put(attribute.getKey(), attribute.getValue().getDefaultValue());
        }
    }
}
