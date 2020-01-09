package org.kasource.commons.reflection.filter.impl;

import org.kasource.commons.reflection.filter.ClassFilter;

/**
 * Class filter that evaluates two other class filters (left and right) and
 * returns the result of OR:ing the result of the left and right filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use builders from the se.hiq.oss.commons.reflection.filter.builder package
 *
 * @author rikardwi
 **/
public class OrClassFilter implements ClassFilter {

    private ClassFilter left;
    private ClassFilter right;

    public OrClassFilter(final ClassFilter left, final ClassFilter right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean apply(Class<?> clazz) {
        return left.apply(clazz) || right.apply(clazz);
    }

    @Override
    public String describe() {
        return left.describe() + " or " + right.describe();
    }

}
