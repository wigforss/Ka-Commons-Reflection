package org.kasource.commons.reflection;


public enum Modifier {
    /**
     * The modifier abstract.
     **/
    ABSTRACT(java.lang.reflect.Modifier.ABSTRACT),

    /**
     * The modifier final.
     **/
    FINAL(java.lang.reflect.Modifier.FINAL),

    /**
     * The modifier native.
     **/
    NATIVE(java.lang.reflect.Modifier.NATIVE),

    /**
     * The modifier private.
     **/
    PRIVATE(java.lang.reflect.Modifier.PRIVATE),

    /**
     * The modifier protected.
     **/
    PROTECTED(java.lang.reflect.Modifier.PROTECTED),

    /**
     * The modifier public.
     **/
    PUBLIC(java.lang.reflect.Modifier.PUBLIC),

    /**
     * The modifier static.
     **/
    STATIC(java.lang.reflect.Modifier.STATIC),

    /**
     * The modifier strict.
     **/
    STRICT(java.lang.reflect.Modifier.STRICT),

    /**
     * The modifier synchronized.
     **/
    SYNCHRONIZED(java.lang.reflect.Modifier.SYNCHRONIZED),

    /**
     * The modifier transient.
     **/
    TRANSIENT(java.lang.reflect.Modifier.TRANSIENT),

    /**
     * The modifier volatile.
     **/
    VOLATILE(java.lang.reflect.Modifier.VOLATILE);

    private int value;

    Modifier(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
