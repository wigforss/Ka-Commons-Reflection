package org.kasource.commons.reflection.filter.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Class Filter Builder.
 * <p>
 * Builds a ClassFilter by configuring a filter and the invoke the build() method.
 * <p>
 * Supports AND (default) OR and NOT operators.
 * <p>
 * Example:
 * {@code
 * ClassFilter testClasses = new ClassFilterBuilder().isPublic().name("[A-Z]\\w*Test").build();
 * }
 *
 * @author rikardwi
 **/
@SuppressWarnings({"checkstyle:classdataabstractioncoupling", "checkstyle:classfanoutcomplexity", "PMD.LinguisticNaming"})
public class ClassFilterBuilder {

    private enum Operator { NONE, NOT, OR }

    private List<ClassFilter> filters = new ArrayList<>();
    private Operator operator = Operator.NONE;

    /**
     * Adds a new filter and applies current operator.
     *
     * @param filter filter to add.
     **/
    private void add(ClassFilter filter) {
        switch (operator) {
            case NOT:
                filters.add(new NegationClassFilter(filter));
                operator = Operator.NONE;
                break;
            case OR:
                filters.set(filters.size() - 1,
                        new OrClassFilter(filters.get(filters.size() - 1), filter));
                operator = Operator.NONE;
                break;
            default:
                filters.add(filter);
                break;
        }
    }


    /**
     * Sets current operator to NOT.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }

    /**
     * Sets current operator to OR.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }

    /**
     * Adds filter.
     *
     * @param classFilter Filter to use.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder with(ClassFilter classFilter) {
        add(classFilter);
        return this;
    }

    /**
     * Adds filter for interface classes classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isInterface() {
        add(new IsInterfaceClassFilter());
        return this;
    }

    /**
     * Adds filter for annotations classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAnnotation() {
        add(new IsAnnotationClassFilter());
        return this;
    }

    /**
     * Adds filter for anonymous classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAnonymous() {
        add(new IsAnonymousClassFilter());
        return this;
    }

    /**
     * Adds filter for array classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isArray() {
        add(new IsArrayClassFilter());
        return this;
    }

    /**
     * Adds filter for enum classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isEnum() {
        add(new IsEnumClassFilter());
        return this;
    }

    /**
     * Adds filter for local classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isLocal() {
        add(new IsLocalClassFilter());
        return this;
    }

    /**
     * Adds filter for member classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isMember() {
        add(new IsMemberClassFilter());
        return this;
    }

    /**
     * Adds filter for primitive classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPrimitive() {
        add(new IsPrimitiveClassFilter());
        return this;
    }

    /**
     * Adds filter for synthetic classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isSynthetic() {
        add(new IsSyntheticClassFilter());
        return this;
    }

    /**
     * Adds filter for classes that is assignable from a superType.
     * <p>
     * extendsType(java.lang.Number) will be true for java.lang.Integer, since java.lang.Integer is extends java.lang.Number.
     *
     * @param superType the class any matching classes should extend or implement.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder extendsType(Class<?> superType) {
        add(new AssignableFromTypeFilter(superType));
        return this;
    }

    /**
     * Adds filter for classes is assignable to an extendedType.
     * <p>
     * superType(java.lang.Integer) will be true for java.lang.Number, since java.lang.Number is a superType of java.lang.Integer.
     *
     * @param extendedType The class that any matching should be a super class of.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder superType(Class<?> extendedType) {
        add(new AssignableToTypeFilter(extendedType));
        return this;
    }

    /**
     * Adds filter for classes annotated with the supplied annotation.
     *
     * @param annotation The annotation to be present on any matching class.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedFilter(annotation));
        return this;
    }

    /**
     * Adds filter for classes annotated with an annotation which is annotated with the supplied annotation.
     * <p>
     * Think springs stereotype annotations: @Service and @Repository is annotated with @Component. Thus
     * I can find any classes @Component classes even though they are actually annotated with Service or
     * Repository with a metaAnnotated(org.springframework.stereotype.Component) filter.
     *
     * @param annotation The meta annotation to find.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder metaAnnotated(Class<? extends Annotation> annotation) {
        add(new MetaAnnotatedFilter(annotation));
        return this;
    }

    /**
     * Adds filter for classes which fully qualified names matches the supplied regularExpression.
     *
     * @param nameRegExp The regular expression to match fully qualified name with.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder name(String nameRegExp) {
        add(new NameFilter(nameRegExp));
        return this;
    }

    /**
     * Adds filter for public classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPublic() {
        add(new ModifierFilter(Modifier.PUBLIC));
        return this;
    }

    /**
     * Adds filter for protected classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isProtected() {
        add(new ModifierFilter(Modifier.PROTECTED));
        return this;
    }

    /**
     * Adds filter for private classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPrivate() {
        add(new ModifierFilter(Modifier.PRIVATE));
        return this;
    }

    /**
     * Adds filter for default access classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isDefault() {
        add(new NegationClassFilter(new ModifierFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }

    /**
     * Adds filter for classes by modifiers.
     *
     * @param modifiers Modifiers to filter on.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder byModifiers(int modifiers) {
        add(new ModifierFilter(modifiers));
        return this;
    }

    /**
     * Adds filter for classes by modifiers.
     *
     * @param modifiers Modifiers to filter on.
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder byModifiers(org.kasource.commons.reflection.Modifier... modifiers) {
        add(new ModifierFilter(modifiers));
        return this;
    }

    /**
     * Adds filter for final classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isFinal() {
        add(new ModifierFilter(Modifier.FINAL));
        return this;
    }

    /**
     * Adds filter for abstract classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAbstract() {
        add(new ModifierFilter(Modifier.ABSTRACT));
        return this;
    }

    /**
     * Adds filter for static classes.
     *
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isStatic() {
        add(new ModifierFilter(Modifier.STATIC));
        return this;
    }

    /**
     * Builds the filter and returns the result.
     *
     * @return the ClassFilter built.
     * @throws IllegalStateException if no filter has been added.
     **/
    public ClassFilter build() throws IllegalStateException {
        if (filters.isEmpty()) {
            throw new IllegalStateException("No filters set.");
        }
        if (filters.size() == 1) {
            return filters.get(0);
        }
        ClassFilter[] classFilters = new ClassFilter[filters.size()];
        filters.toArray(classFilters);
        return new AndClassFilter(classFilters);
    }
}
