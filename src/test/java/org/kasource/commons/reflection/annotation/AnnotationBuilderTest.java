package org.kasource.commons.reflection.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.junit.Test;
import org.unitils.inject.annotation.InjectInto;


public class AnnotationBuilderTest {

    @Test
    public void noAttributes() throws SecurityException, NoSuchMethodException {
        PostConstruct annotation = new AnnotationBuilder<PostConstruct>(PostConstruct.class).build();
        PostConstruct postConstruct = AnnotatedClass.class.getMethod("initilaize").getAnnotation(PostConstruct.class);
        assertEquals(PostConstruct.class, annotation.annotationType());
        assertTrue(postConstruct.equals(annotation));
        assertTrue(annotation.equals(postConstruct));
    }
    
    @Test
    public void multipleAttributes() throws SecurityException, NoSuchFieldException {
        String nameAttribute = "Test";
        
        Resource resource = new AnnotationBuilder<Resource>(Resource.class).attr("name", "Test").build();
        assertEquals(Resource.class, resource.annotationType());
        assertEquals(nameAttribute, resource.name());
        Resource realResource = AnnotatedClass.class.getDeclaredField("variable").getAnnotation(Resource.class);
        
        
        assertTrue(realResource.equals(resource));
        assertTrue(resource.equals(realResource));       
    }
    
    @Test(expected = IllegalStateException.class)
    public void missingAttribute () {
        new AnnotationBuilder<InjectInto>(InjectInto.class).build();
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void settingUnknownAttribute () {
        new AnnotationBuilder<InjectInto>(InjectInto.class).attr("name", "Test").build();
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongDataType() {
        new AnnotationBuilder<Resource>(Resource.class).attr("shareable", "Test").build();
        
    }
    
    @Test
    public void settingComponentTypeInArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class).value(ElementType.METHOD).build();
        Target target = PostConstruct.class.getAnnotation(Target.class);
        assertEquals(target, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void settingWrongComponentTypeInArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class).value("String").build();
        Target target = PostConstruct.class.getAnnotation(Target.class);
        assertEquals(target, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void settingWrongArrayTypeInArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class).value(new String[]{"Test", "Test2"}).build();
        Target target = PostConstruct.class.getAnnotation(Target.class);
        assertEquals(target, annotation);
    }
    
    @Test
    public void setArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class, new ElementType[]{ElementType.TYPE,ElementType.FIELD,ElementType.METHOD}).build();
        Target target = Resource.class.getAnnotation(Target.class);
        assertEquals(target, annotation);
    }
    
    private static class AnnotatedClass {
        
       
        @Resource(name = "Test")
        private String variable;
        
        @PostConstruct
        public void initilaize() {
            
        }
    }
}
