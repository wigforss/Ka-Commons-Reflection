package org.kasource.commons.reflection.filter.impl;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.FieldFilter;


/**
 * Filters fields which are Enum constants.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 *
 * @author rikardwi
 **/
public class IsEnumConstantFieldFilter implements FieldFilter {

    @Override
    public boolean apply(Field field) {
        return field.isEnumConstant();
    }

    @Override
    public String describe() {
        return "is enum constant";
    }
}
