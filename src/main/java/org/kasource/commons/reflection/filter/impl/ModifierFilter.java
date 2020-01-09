package org.kasource.commons.reflection.filter.impl;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kasource.commons.reflection.Modifier;
import org.kasource.commons.reflection.filter.ClassFilter;
import org.kasource.commons.reflection.filter.ConstructorFilter;
import org.kasource.commons.reflection.filter.FieldFilter;
import org.kasource.commons.reflection.filter.MethodFilter;
import org.kasource.commons.reflection.util.ModifierUtils;


/**
 * Filters classes by modifiers.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class ModifierFilter implements ClassFilter, MethodFilter, FieldFilter, ConstructorFilter {

    private int modifier;

    public ModifierFilter(int modifier) {
        this.modifier = modifier;
    }

    public ModifierFilter(final Modifier... modifiers) {
        int modifierInt = 0;
        for (Modifier modifierEnum : modifiers) {
            modifierInt = modifier | modifierEnum.getValue();
        }
        this.modifier = modifierInt;
    }

    @Override
    public boolean apply(Class<?> clazz) {
        return (clazz.getModifiers() & modifier) > 0;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean apply(Constructor constructor) {
        return (constructor.getModifiers() & modifier) > 0;

    }

    @Override
    public boolean apply(Field field) {
        return (field.getModifiers() & modifier) > 0;
    }

    @Override
    public boolean apply(Method method) {
        return (method.getModifiers() & modifier) > 0;
    }

    @Override
    public String describe() {
        return "is " + ModifierUtils.toString(modifier);
    }

}
