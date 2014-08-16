package org.kasource.commons.reflection.util;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import org.junit.Test;

public class AnnotationUtilsTest {

   

    
   
    
    @Test
    public void isAnnotationPresentTrue() {
        assertTrue(AnnotationUtils.isAnnotationPresent(MyClass.class, ClassAnnotation2.class));
    }
    
    @Test
    public void isAnnotationPresentFalse() {
        assertFalse(AnnotationUtils.isAnnotationPresent(MyClass.class, ClassAnnotation1.class));
    }
    
    @Test
    public void getAnnotation() {
        assertEquals("Value2", AnnotationUtils.getAnnotation(MyClass.class, ClassAnnotation2.class).value());
    }
    
    @Test
    public void getAnnotationNotFound() {
        assertEquals(null, AnnotationUtils.getAnnotation(MyClass.class, ClassAnnotation1.class));
    }
    
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation1{}
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation2{}
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation1{}
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation2{
        String value();
    }
    
    private static class MyClass extends MyBase implements EventListener, Serializable {
        private String name;
        private int age;
        
        public MyClass() {}
        
        public MyClass(String name) {}
        
        
        private MyClass(String name, int age) {}
        
        @MethodAnnotation1
        public void oneParameter(String name){}
        
        @MethodAnnotation1
        private void privateOneParamter(String name){}
    }
    
    @ClassAnnotation2("Value2")
    private static class MyBase implements Runnable {
        private String superField;
        public void superClassMethod(String name){}

        @MethodAnnotation2
        @Override
        public void run() {
            // TODO Auto-generated method stub
            
        }
    }
}
