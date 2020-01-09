package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.kasource.commons.reflection.filter.impl.AnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.AssignableFromParameterFilter;
import org.kasource.commons.reflection.filter.impl.AssignableToParameterFilter;
import org.kasource.commons.reflection.filter.impl.MetaAnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.AndMethodFilter;
import org.kasource.commons.reflection.filter.impl.ModifierFilter;
import org.kasource.commons.reflection.filter.impl.NameFilter;
import org.kasource.commons.reflection.filter.impl.NegationMethodFilter;
import org.kasource.commons.reflection.filter.impl.NumberOfParametersFilter;
import org.kasource.commons.reflection.filter.impl.OrMethodFilter;
import org.kasource.commons.reflection.filter.impl.ParameterClassFilterFilter;
import org.kasource.commons.reflection.filter.impl.ReturnTypeAssignableFromMethodFilter;
import org.kasource.commons.reflection.filter.impl.ReturnTypeMethodFilter;
import org.kasource.commons.reflection.filter.impl.SignatureFilter;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MethodFilterBuilderTest {

    private MethodFilterBuilder builder = new MethodFilterBuilder();

    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }


    @Test
    public void notTest() {
        MethodFilter filter = builder.not().isPublic().build();
        assertThat(filter, is(instanceOf(NegationMethodFilter.class)));
    }

    @Test
    public void orTest() {
        MethodFilter filter = builder.isPublic().or().isPrivate().or().isProtected().build();
        assertThat(filter, is(instanceOf(OrMethodFilter.class)));

    }

    @Test
    public void listTest() {
        MethodFilter filter = builder.isPublic().isSynchronized().build();
        assertThat(filter, is(instanceOf(AndMethodFilter.class)));
    }

    @Test
    public void isPublic() {
        MethodFilter filter = builder.isPublic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isProtected() {
        MethodFilter filter = builder.isProtected().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isPrivate() {
        MethodFilter filter = builder.isPrivate().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isStatic() {
        MethodFilter filter = builder.isStatic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isSynchronized() {
        MethodFilter filter = builder.isSynchronized().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }


    @Test
    public void isDefault() {
        MethodFilter filter = builder.isDefault().build();
        assertThat(filter, is(instanceOf(NegationMethodFilter.class)));
    }

    @Test
    public void byModifiers() {
        MethodFilter filter = builder.byModifiers(Modifier.PUBLIC & Modifier.STATIC).build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void name() {
        MethodFilter filter = builder.name("get[A-Z].*").build();
        assertThat(filter, is(instanceOf(NameFilter.class)));
    }

    @Test
    public void annotated() {
        MethodFilter filter = builder.annotated(Retention.class).build();
        assertThat(filter, is(instanceOf(AnnotatedFilter.class)));
    }

    @Test
    public void metaAnnotated() {
        MethodFilter filter = builder.metaAnnotated(Retention.class).build();
        assertThat(filter, is(instanceOf(MetaAnnotatedFilter.class)));
    }

    @Test
    public void hasSignature() {
        MethodFilter filter = builder.hasSignature(int.class).build();
        assertThat(filter, is(instanceOf(SignatureFilter.class)));
    }

    @Test
    public void hasReturnType() {
        MethodFilter filter = builder.returnType(Void.TYPE).build();
        assertThat(filter, is(instanceOf(ReturnTypeMethodFilter.class)));
    }

    @Test
    public void returnTypeExtends() {
        MethodFilter filter = builder.returnTypeExtends(Void.TYPE).build();
        assertThat(filter, is(instanceOf(ReturnTypeAssignableFromMethodFilter.class)));
    }

    @Test
    public void numberOfParameters() {
        MethodFilter filter = builder.numberOfParameters(4).build();
        assertThat(filter, is(instanceOf(NumberOfParametersFilter.class)));
    }

    @Test
    public void parametersExtendsType() {
        MethodFilter filter = builder.parametersExtendsType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromParameterFilter.class)));
    }

    @Test
    public void parametersSuperType() {
        MethodFilter filter = builder.parametersSuperType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableToParameterFilter.class)));
    }

    @Test
    public void parameterExtendsType() {
        MethodFilter filter = builder.parameterExtendsType(0, List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromParameterFilter.class)));
    }

    @Test
    public void parameterSuperType() {
        MethodFilter filter = builder.parameterSuperType(0, List.class).build();
        assertThat(filter, is(instanceOf(AssignableToParameterFilter.class)));
    }


    @Test
    public void parameterTypeFilter() {
        MethodFilter filter = builder.parameterTypeFilter(0, new NameFilter("Abstract.*")).build();
        assertThat(filter, is(instanceOf(ParameterClassFilterFilter.class)));
    }

    @Test
    public void parameterTypesFilter() {
        MethodFilter filter = builder.parametersTypesFilter(new NameFilter("Abstract.*")).build();
        assertThat(filter, is(instanceOf(ParameterClassFilterFilter.class)));
    }

}
