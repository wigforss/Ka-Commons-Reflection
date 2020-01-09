package org.kasource.commons.reflection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.unitils.inject.annotation.InjectInto;


public class AnnotationBuilderTest {

    @Test
    public void noAttributes() throws SecurityException, NoSuchMethodException {
        PostConstruct annotation = new AnnotationBuilder<PostConstruct>(PostConstruct.class).build();
        PostConstruct postConstruct = AnnotatedClass.class.getMethod("initilaize").getAnnotation(PostConstruct.class);
        assertThat(annotation.annotationType(), equalTo((Class) PostConstruct.class));
        assertThat(postConstruct.equals(annotation), is(true));
        assertThat(annotation.equals(postConstruct), is(true));
    }

    @Test
    public void multipleAttributes() throws SecurityException, NoSuchFieldException {
        String nameAttribute = "Test";

        Resource resource = new AnnotationBuilder<Resource>(Resource.class).attr("name", "Test").build();
        assertThat(resource.annotationType(), equalTo((Class) Resource.class));
        assertThat(resource.name(), equalTo(nameAttribute));
        Resource realResource = AnnotatedClass.class.getDeclaredField("variable").getAnnotation(Resource.class);


        assertThat(realResource.equals(resource), is(true));
        assertThat(resource.equals(realResource), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void missingAttribute() {
        new AnnotationBuilder<InjectInto>(InjectInto.class).build();

    }

    @Test(expected = IllegalArgumentException.class)
    public void settingUnknownAttribute() {
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
        assertThat(annotation, equalTo(target));
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingWrongComponentTypeInArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class).value("String").build();
        Target target = PostConstruct.class.getAnnotation(Target.class);
        assertThat(annotation, equalTo(target));
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingWrongArrayTypeInArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class).value(new String[]{"Test", "Test2"}).build();
        Target target = PostConstruct.class.getAnnotation(Target.class);
        assertThat(annotation, equalTo(target));
    }

    @Test
    public void setArray() {
        Target annotation = new AnnotationBuilder<Target>(Target.class, new ElementType[]{ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}).build();
        Target target = Resource.class.getAnnotation(Target.class);
        assertThat(annotation, equalTo(target));
    }

    private static class AnnotatedClass {
        @Resource(name = "Test")
        private String variable;

        @PostConstruct
        public void initilaize() {

        }
    }
}
