package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Retention;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.impl.AnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.AssignableFromParameterFilter;
import org.kasource.commons.reflection.filter.impl.AssignableToParameterFilter;
import org.kasource.commons.reflection.filter.impl.AndConstructorFilter;
import org.kasource.commons.reflection.filter.impl.MetaAnnotatedFilter;
import org.kasource.commons.reflection.filter.impl.ModifierFilter;
import org.kasource.commons.reflection.filter.impl.NameFilter;
import org.kasource.commons.reflection.filter.impl.NegationConstructorFilter;
import org.kasource.commons.reflection.filter.impl.NumberOfParametersFilter;
import org.kasource.commons.reflection.filter.impl.OrConstructorFilter;
import org.kasource.commons.reflection.filter.impl.ParameterClassFilterFilter;
import org.kasource.commons.reflection.filter.impl.SignatureFilter;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ConstructorFilterBuilderTest {


    private ConstructorFilterBuilder builder = new ConstructorFilterBuilder();

    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }


    @Test
    public void notTest() {
        ConstructorFilter filter = builder.not().isPublic().build();
        assertThat(filter, is(instanceOf(NegationConstructorFilter.class)));
    }

    @Test
    public void orTest() {
        ConstructorFilter filter = builder.isPublic().or().isPrivate().or().isProtected().build();
        assertThat(filter, is(instanceOf(OrConstructorFilter.class)));
    }

    @Test
    public void listTest() {
        ConstructorFilter filter = builder.isPublic().isPrivate().build();
        assertThat(filter, is(instanceOf(AndConstructorFilter.class)));
    }

    @Test
    public void isPublic() {
        ConstructorFilter filter = builder.isPublic().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isProtected() {
        ConstructorFilter filter = builder.isProtected().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }

    @Test
    public void isPrivate() {
        ConstructorFilter filter = builder.isPrivate().build();
        assertThat(filter, is(instanceOf(ModifierFilter.class)));
    }


    @Test
    public void isDefault() {
        ConstructorFilter filter = builder.isDefault().build();
        assertThat(filter, is(instanceOf(NegationConstructorFilter.class)));
    }


    @Test
    public void annotated() {
        ConstructorFilter filter = builder.annotated(Retention.class).build();
        assertThat(filter, is(instanceOf(AnnotatedFilter.class)));
    }

    @Test
    public void metaAnnotated() {
        ConstructorFilter filter = builder.metaAnnotated(Retention.class).build();
        assertThat(filter, is(instanceOf(MetaAnnotatedFilter.class)));
    }

    @Test
    public void hasSignature() {
        ConstructorFilter filter = builder.hasSignature(int.class).build();
        assertThat(filter, is(instanceOf(SignatureFilter.class)));
    }


    @Test
    public void numberOfParameters() {
        ConstructorFilter filter = builder.numberOfParameters(4).build();
        assertThat(filter, is(instanceOf(NumberOfParametersFilter.class)));
    }

    @Test
    public void parametersExtendsType() {
        ConstructorFilter filter = builder.parametersExtendsType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromParameterFilter.class)));
    }

    @Test
    public void parametersSuperType() {
        ConstructorFilter filter = builder.parametersSuperType(List.class).build();
        assertThat(filter, is(instanceOf(AssignableToParameterFilter.class)));
    }

    @Test
    public void parameterExtendsType() {
        ConstructorFilter filter = builder.parameterExtendsType(0, List.class).build();
        assertThat(filter, is(instanceOf(AssignableFromParameterFilter.class)));
    }

    @Test
    public void parameterSuperType() {
        ConstructorFilter filter = builder.parameterSuperType(0, List.class).build();
        assertThat(filter, is(instanceOf(AssignableToParameterFilter.class)));
    }


    @Test
    public void parameterTypeFilter() {
        ConstructorFilter filter = builder.parameterTypeFilter(0, new NameFilter("Abstract.*")).build();
        assertThat(filter, is(instanceOf(ParameterClassFilterFilter.class)));
    }

    @Test
    public void parameterTypesFilter() {
        ConstructorFilter filter = builder.parametersTypesFilter(new NameFilter("Abstract.*")).build();
        assertThat(filter, is(instanceOf(ParameterClassFilterFilter.class)));
    }

}
