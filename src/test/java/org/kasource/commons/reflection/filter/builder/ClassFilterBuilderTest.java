package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.impl.AnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.AssignableFromTypeFilter;
import org.kasource.commons.reflection.filter.impl.AssignableToTypeFilter;
import org.kasource.commons.reflection.filter.impl.AndClassFilter;
import org.kasource.commons.reflection.filter.impl.IsAnnotationClassFilter;
import org.kasource.commons.reflection.filter.impl.IsAnonymousClassFilter;
import org.kasource.commons.reflection.filter.impl.IsArrayClassFilter;
import org.kasource.commons.reflection.filter.impl.IsEnumClassFilter;
import org.kasource.commons.reflection.filter.impl.IsInterfaceClassFilter;
import org.kasource.commons.reflection.filter.impl.IsLocalClassFilter;
import org.kasource.commons.reflection.filter.impl.IsMemberClassFilter;
import org.kasource.commons.reflection.filter.impl.IsPrimitiveClassFilter;
import org.kasource.commons.reflection.filter.impl.IsSyntheticClassFilter;
import org.kasource.commons.reflection.filter.impl.MetaAnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.ModifierFilter;
import org.kasource.commons.reflection.filter.impl.NameFilter;
import org.kasource.commons.reflection.filter.impl.NegationClassFilter;
import org.kasource.commons.reflection.filter.impl.OrClassFilter;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ClassFilterBuilderTest {


    private ClassFilterBuilder builder = new ClassFilterBuilder();

    @Test(expected = IllegalStateException.class)
    public void buildNoFilter() {
        builder.build();
    }

    @Test
    public void nameTest() {
        ClassFilter filter = builder.name("testName").build();
        assertThat(filter, is(instanceOf(NameFilter.class)));
    }

    @Test
    public void isPublicTest() {
        ClassFilter filter = builder.isPublic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isStaticTest() {
        ClassFilter filter = builder.isStatic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isFinalTest() {
        ClassFilter filter = builder.isFinal().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isAbstractTest() {
        ClassFilter filter = builder.isAbstract().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void notFilterTest() {
        ClassFilter filter = builder.not().isPublic().build();
        assertThat(filter, is(instanceOf(NegationClassFilter.class)));
    }

    @Test
    public void orFilterTest() {
        ClassFilter filter = builder.isPrivate().or().isProtected().build();
        assertThat(filter, is(instanceOf(OrClassFilter.class)));
    }

    @Test
    public void isInterface() {
        ClassFilter filter = builder.isInterface().build();
        assertThat(filter, is(instanceOf(IsInterfaceClassFilter.class)));
    }

    @Test
    public void isAnnotation() {
        ClassFilter filter = builder.isAnnotation().build();
        assertThat(filter, is(instanceOf(IsAnnotationClassFilter.class)));
    }

    @Test
    public void isAnonymous() {
        ClassFilter filter = builder.isAnonymous().build();
        assertThat(filter, is(instanceOf(IsAnonymousClassFilter.class)));
    }

    @Test
    public void isArray() {
        ClassFilter filter = builder.isArray().build();
        assertThat(filter, is(instanceOf(IsArrayClassFilter.class)));
    }

    @Test
    public void isEnum() {
        ClassFilter filter = builder.isEnum().build();
        assertThat(filter, is(instanceOf(IsEnumClassFilter.class)));
    }


    @Test
    public void isLocal() {
        ClassFilter filter = builder.isLocal().build();
        assertThat(filter, is(instanceOf(IsLocalClassFilter.class)));
    }

    @Test
    public void isMember() {
        ClassFilter filter = builder.isMember().build();
        assertThat(filter, is(instanceOf(IsMemberClassFilter.class)));
    }

    @Test
    public void isPrimitive() {
        ClassFilter filter = builder.isPrimitive().build();
        assertThat(filter, is(instanceOf(IsPrimitiveClassFilter.class)));
    }

    @Test
    public void isSynthetic() {
        ClassFilter filter = builder.isSynthetic().build();
        assertThat(filter, is(instanceOf(IsSyntheticClassFilter.class)));
    }

    @Test
    public void extendsType() {
        ClassFilter filter = builder.extendsType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromTypeFilter.class)));
    }

    @Test
    public void superType() {
        ClassFilter filter = builder.superType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableToTypeFilter.class)));
    }

    @Test
    public void annotated() {
        ClassFilter filter = builder.annotated(Retention.class).build();
        assertThat(filter, is(instanceOf(AnnotatedFilter.class)));
    }

    @Test
    public void metaAnnotated() {
        ClassFilter filter = builder.metaAnnotated(Retention.class).build();
        assertThat(filter, is(instanceOf(MetaAnnotatedFilter.class)));
    }

    @Test
    public void filterList() {
        ClassFilter filter = builder.isPrivate().isStatic().build();
        assertThat(filter, is(instanceOf(AndClassFilter.class)));

    }

    @Test
    public void isDefault() {
        ClassFilter filter = builder.isDefault().build();
        assertThat(filter, is(instanceOf(NegationClassFilter.class)));
    }

    @Test
    public void byModifiers() {
        ClassFilter filter = builder.byModifiers(Modifier.ABSTRACT).build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }
}
