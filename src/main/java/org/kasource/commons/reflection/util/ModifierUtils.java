package org.kasource.commons.reflection.util;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public final class ModifierUtils {

    private ModifierUtils() {
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public static String toString(int modifiers) {
        List<String> modifierList = new ArrayList<>();

        addAccessModifier(modifiers, modifierList);

        if (Modifier.isAbstract(modifiers)) {
            modifierList.add("abstract");
        }
        if (Modifier.isStatic(modifiers)) {
            modifierList.add("static");
        }
        if (Modifier.isFinal(modifiers)) {
            modifierList.add("final");
        }
        if (Modifier.isTransient(modifiers)) {
            modifierList.add("transient");
        }
        if (Modifier.isVolatile(modifiers)) {
            modifierList.add("volatile");
        }
        if (Modifier.isSynchronized(modifiers)) {
            modifierList.add("synchronized");
        }
        if (Modifier.isNative(modifiers)) {
            modifierList.add("native");
        }
        if (Modifier.isInterface(modifiers)) {
            modifierList.add("interface");
        }

        return StringUtils.join(modifierList.toArray(), " ");
    }

    private static void addAccessModifier(int modifiers, List<String> modifierList) {
        if (Modifier.isPublic(modifiers)) {
            modifierList.add("public");
        } else if (Modifier.isPrivate(modifiers)) {
            modifierList.add("private");
        } else if (Modifier.isProtected(modifiers)) {
            modifierList.add("protected");
        }
    }
}
