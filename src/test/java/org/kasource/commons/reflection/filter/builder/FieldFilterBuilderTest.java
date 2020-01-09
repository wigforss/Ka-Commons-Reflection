package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.impl.AnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.AssignableFromTypeFilter;
import org.kasource.commons.reflection.filter.impl.AssignableToTypeFilter;
import org.kasource.commons.reflection.filter.impl.AndFieldFilter;
import org.kasource.commons.reflection.filter.impl.IsEnumConstantFieldFilter;
import org.kasource.commons.reflection.filter.impl.MetaAnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.ModifierFilter;
import org.kasource.commons.reflection.filter.impl.NameFilter;
import org.kasource.commons.reflection.filter.impl.NegationFieldFilter;
import org.kasource.commons.reflection.filter.impl.OrFieldFilter;
import org.kasource.commons.reflection.filter.impl.TypeClassFilter;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FieldFilterBuilderTest {


    private FieldFilterBuilder builder = new FieldFilterBuilder();

    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }

    @Test
    public void extendsType() {
        FieldFilter filter = builder.extendsType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromTypeFilter.class)));
    }

    @Test
    public void superType() {
        FieldFilter filter = builder.superType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableToTypeFilter.class)));
    }

    @Test
    public void typeFilter() {
        FieldFilter filter = builder.typeFilter(new NameFilter("Abstract*.")).build();
        assertThat(filter, is(instanceOf(TypeClassFilter.class)));
    }

    @Test
    public void isEnumConstant() {
        FieldFilter filter = builder.isEnumConstant().build();
        assertThat(filter, is(instanceOf(IsEnumConstantFieldFilter.class)));
    }

    @Test
    public void name() {
        FieldFilter filter = builder.name(".*List.*").build();
        assertThat(filter, is(instanceOf(NameFilter.class)));
    }

    @Test
    public void annotated() {
        FieldFilter filter = builder.annotated(Retention.class).build();
        assertThat(filter, is(instanceOf(AnnotatedFilter.class)));
    }

    @Test
    public void metaAnnotated() {
        FieldFilter filter = builder.metaAnnotated(Retention.class).build();
        assertThat(filter, is(instanceOf(MetaAnnotatedFilter.class)));
    }

    @Test
    public void notTest() {
        FieldFilter filter = builder.not().isPublic().build();
        assertThat(filter, is(instanceOf(NegationFieldFilter.class)));
    }

    @Test
    public void orTest() {
        FieldFilter filter = builder.isPrivate().or().isProtected().build();
        assertThat(filter, is(instanceOf(OrFieldFilter.class)));
    }

    @Test
    public void isPublic() {
        FieldFilter filter = builder.isPublic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isProtected() {
        FieldFilter filter = builder.isProtected().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isPrivate() {
        FieldFilter filter = builder.isPrivate().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isStatic() {
        FieldFilter filter = builder.isStatic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isTransient() {
        FieldFilter filter = builder.isTransient().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isFinal() {
        FieldFilter filter = builder.isFinal().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isVolatile() {
        FieldFilter filter = builder.isVolatile().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isDefault() {
        FieldFilter filter = builder.isDefault().build();
        assertThat(filter, is(instanceOf(NegationFieldFilter.class)));
    }

    @Test
    public void byModifiers() {
        FieldFilter filter = builder.byModifiers(Modifier.PROTECTED & Modifier.ABSTRACT).build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void listTest() {
        FieldFilter filter = builder.isStatic().isFinal().build();
        assertThat(filter, is(instanceOf(AndFieldFilter.class)));
    }

}
