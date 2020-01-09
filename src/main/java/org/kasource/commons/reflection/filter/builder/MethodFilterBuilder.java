package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.ClassFilter;
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

/**
 * Builder for MethodFilters.
 * <p>
 * This class offers functionality for building more complex
 * filter compositions in an expressive manner. The filters added will be
 * evaluated with AND operator. This builder allows both NOT and OR as
 * additional operators.
 * <p>
 * Examples:
 * {@code
 * MethodFilter publicGetter = new MethodFilterBuilder().isPublic().name("get[A-Z]\\w*").not().hasReturnType(Void.TYPE).build();
 * MethodFilter nonPublicfilter = new MethdodFilterBuilder().not().isPublic().build();
 * MethodFilter privateOrProtected = new MethodFilterBuilder().isPrivate().or().isProtected().build();
 * }
 *
 * @author rikardwi
 **/
@SuppressWarnings({"checkstyle:classdataabstractioncoupling", "PMD.LinguisticNaming"})
public class MethodFilterBuilder {
    public static final MethodFilter FILTER_GETTERS = new MethodFilterBuilder()
            .name("get[A-Z]\\w*").or().name("is[A-Z]\\w*").or().name("has[A-Z]\\w*")
            .isPublic().not().returnType(Void.TYPE)
            .numberOfParameters(0)
            .build();
    public static final MethodFilter FILTER_SETTERS = new MethodFilterBuilder()
            .name("set[A-Z]\\w*")
            .isPublic()
            .returnType(Void.TYPE)
            .numberOfParameters(1)
            .build();

    private enum Operator { NONE, NOT, OR };
    private List<MethodFilter> filters = new ArrayList<>();
    private Operator operator = Operator.NONE;

    /**
     * Adds filter to filter list and apply operator.
     *
     * @param filter filter to add.
     **/
    private void add(MethodFilter filter) {
        switch (operator) {
            case NOT:
                filters.add(new NegationMethodFilter(filter));
                operator = Operator.NONE;
                break;
            case OR:
                filters.set(filters.size() - 1,
                        new OrMethodFilter(filters.get(filters.size() - 1), filter));
                operator = Operator.NONE;
                break;
            default:
                filters.add(filter);
                break;
        }
    }

    /**
     * Set current operator to NOT, which will be applied
     * on the next filter added.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }

    /**
     * Set current operator to OR, which will be applied
     * on the next filter added, by applying OR to the last filter
     * added and the next.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }

    /**
     * Adds filter.
     *
     * @param methodFilter Filter to use.
     *
     * @return This builder for method chaining.
     **/
    public MethodFilterBuilder with(MethodFilter methodFilter) {
        add(methodFilter);
        return this;
    }

    /**
     * Adds a filter for public methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isPublic() {
        add(new ModifierFilter(Modifier.PUBLIC));
        return this;
    }

    /**
     * Adds a filter for getter methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isGetter() {
        isPublic()
                .not().returnType(Void.TYPE)
                .name("get[A-Z].*|is[A-Z].*|has[A-Z].*")
                .numberOfParameters(0);
        return this;
    }

    /**
     * Adds a filter for setter methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isSetter() {
        isPublic()
                .returnType(Void.TYPE)
                .name("set[A-Z].*")
                .numberOfParameters(1);
        return this;
    }

    /**
     * Adds a filter for protected methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isProtected() {
        add(new ModifierFilter(Modifier.PROTECTED));
        return this;
    }

    /**
     * Adds a filter for private methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isPrivate() {
        add(new ModifierFilter(Modifier.PRIVATE));
        return this;
    }

    /**
     * Adds a filter for static methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isStatic() {
        add(new ModifierFilter(Modifier.STATIC));
        return this;
    }

    /**
     * Adds a filter for synchronized methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isSynchronized() {
        add(new ModifierFilter(Modifier.SYNCHRONIZED));
        return this;
    }

    /**
     * Adds a filter for default access methods only.
     *
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isDefault() {
        add(new NegationMethodFilter(new ModifierFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }

    /**
     * Adds a filter for methods methods that has the supplied
     * modifiers.
     *
     * @param modifiers Modifiers to match.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder byModifiers(int modifiers) {
        add(new ModifierFilter(modifiers));
        return this;
    }

    /**
     * Adds a filter for methods methods that has the supplied
     * modifiers.
     *
     * @param modifiers Modifiers to match.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder byModifiers(org.kasource.commons.reflection.Modifier... modifiers) {
        add(new ModifierFilter(modifiers));
        return this;
    }

    /**
     * Adds a filter for methods which names matches
     * the supplied name regular expression.
     *
     * @param nameRegExp Regular Expression to match method name with.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder name(String nameRegExp) {
        add(new NameFilter(nameRegExp));
        return this;
    }

    /**
     * Adds a filter for methods which is annotated
     * with the supplied annotation.
     *
     * @param annotation The annotation to match.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedFilter(annotation));
        return this;
    }

    /**
     * Adds a filter for methods which is annotated
     * with an annotation which is annotated with
     * the supplied annotation.
     *
     * @param inheritedAnnotation The meta annotation to match method annotations with.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder metaAnnotated(Class<? extends Annotation> inheritedAnnotation) {
        add(new MetaAnnotatedFilter(inheritedAnnotation));
        return this;
    }

    /**
     * Adds a filter for methods which has the exact signature (types) as
     * the supplied parameter types.
     *
     * @param params Varargs type of the parameters, may be empty.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder hasSignature(Class<?>... params) {
        add(new SignatureFilter(params));
        return this;
    }

    /**
     * Adds a filter for methods which has a
     * return type matching the supplied parameter returnType.
     *
     * @param returnType The class to match return type with.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder returnType(Class<?> returnType) {
        add(new ReturnTypeMethodFilter(returnType));
        return this;
    }

    /**
     * Adds a filter for methods which has a
     * return type that extends the supplied parameter returnType.
     *
     * @param returnType The class to match return type with.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder returnTypeExtends(Class<?> returnType) {
        add(new ReturnTypeAssignableFromMethodFilter(returnType));
        return this;
    }

    /**
     * Adds a filter for methods with matching number of
     * parameters as the supplied parameter: numberOfParameters.
     *
     * @param numberOfParameters The number of parameters to match.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder numberOfParameters(int numberOfParameters) {
        add(new NumberOfParametersFilter(numberOfParameters));
        return this;
    }

    /**
     * Adds a filter that allows only methods which parameter types extends the supplied
     * types in the superType parameter.
     * <p>
     * Only methods with same number of parameters as the extendsClass parameter will
     * be allowed.
     *
     * @param superType Varargs classes that the parameters types should extend, may be empty.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parametersExtendsType(Class<?>... superType) {
        add(new AssignableFromParameterFilter(superType));
        return this;
    }

    /**
     * Adds a filter that allows only methods which parameter types which is a base class of the supplied
     * types in the baseType parameter.
     * <p>
     * Only methods with same number of parameters as the extendsClass parameter will
     * be allowed.
     *
     * @param baseType Varargs classes that the parameters types should extend, may be empty.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parametersSuperType(Class<?>... baseType) {
        add(new AssignableToParameterFilter(baseType));
        return this;
    }

    /**
     * Adds a filter for methods that has a parameter which extends the supplied type in the
     * extendsType parameter.
     * <p>
     * If a method does not have a parameter at parameterIndex (too few parameters) its not allowed.
     *
     * @param parameterIndex The index of the parameter to inspect.
     * @param superType      The class the parameter should extend.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parameterExtendsType(int parameterIndex, Class<?> superType) {
        add(new AssignableFromParameterFilter(parameterIndex, superType));
        return this;
    }

    /**
     * Adds a filter for methods that has a parameter which is a base class to the supplied type in the
     * baseType parameter.
     * <p>
     * If a method does not have a parameter at parameterIndex (too few parameters) its not allowed.
     *
     * @param parameterIndex The index of the parameter to inspect.
     * @param baseType       The class the parameter should have a base class.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parameterSuperType(int parameterIndex, Class<?> baseType) {
        add(new AssignableToParameterFilter(parameterIndex, baseType));
        return this;
    }


    /**
     * Adds a filter for methods that has a parameter which type passes the supplied class filter.
     * <p>
     * If a method does not have a parameter at parameterIndex (too few parameters) its not allowed.
     * <p>
     * Example:
     * {@code
     * MethodFilter filter = new MethodFilterBuilder()
     *                           .parameterTypeFilter(0, new ClassFilterBuilder()
     *                           .annotated(MyAnnotation.class)
     *                           .build()).build();
     * }
     * Created a filter for methods which first parameter type should be annotated with @MyAnnotation.
     *
     * @param parameterIndex The index of the parameter to inspect.
     * @param filter         The filter which the parameter type should pass.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parameterTypeFilter(int parameterIndex, ClassFilter filter) {
        add(new ParameterClassFilterFilter(parameterIndex, filter));
        return this;
    }

    /**
     * Adds a filter that allows only methods which parameter types passes the class filter supplied
     * in the parameter filter.
     * <p>
     * Only methods with same number of parameters as the filter parameter will
     * be allowed.
     *
     * @param filter Varargs class filters that the parameters should pass, may be empty.
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parametersTypesFilter(ClassFilter... filter) {
        add(new ParameterClassFilterFilter(filter));
        return this;
    }

    /**
     * Returns the MethodFilter built.
     *
     * @return the MethodFilter built.
     * @throws IllegalStateException if no filter was specified before calling build()
     **/
    public MethodFilter build() {
        if (filters.isEmpty()) {
            throw new IllegalStateException("No filter specified.");
        }
        if (filters.size() == 1) {
            return filters.get(0);
        }
        MethodFilter[] methodFilters = new MethodFilter[filters.size()];
        filters.toArray(methodFilters);
        return new AndMethodFilter(methodFilters);
    }

}
