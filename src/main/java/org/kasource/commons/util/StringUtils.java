/**
 *
 */
package org.kasource.commons.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rikardwigforss
 *
 */
public final class StringUtils {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{\\w*\\}");

    private StringUtils() {
    }

    /**
     * Returns a string where maven-style variables ${variableName} are replaced with variables from
     * the variable map, system variable or environment variables.
     * <p>
     * Will resolve variable values differently depending on the systemOverrideMode flag, where true = override and false = fall back.
     * See the list below in which order variable values are resolved.
     * <p>
     * <b>systemOverrideMode = true</b>
     *<p>
     * 1. Find variable value in system properties<br>
     * 2. Find variable value in environment variables<br>
     * 3. Find variable value in the variables map<br>
     * <p>
     * <b>systemOverrideMode = true</b>
     * <p>
     * 1. Find variable value in the variables map<br>
     * 2. Find variable value in system properties<br>
     * 3. Find variable value in environment variables<br>
     *
     *
     * @param input                    The text to perform variable substitution upon
     * @param variables                Map of variable values
     * @param systemOverrideMode       true system variables will override what's in the variables map
     *
     * @return the input with the variables found replaced with it's values.
     **/
    public static String replaceVariables(String input, Map<String, Object> variables, boolean systemOverrideMode) {
        Matcher matcher = VARIABLE_PATTERN.matcher(input);
        String result = input;
        while (matcher.find()) {
            String variableName = matcher.group();
            variableName = variableName.substring(2, variableName.length() - 1);
            Object variable = resolveVariable(variableName, variables, systemOverrideMode);
            if (variable != null) {
                result = result.replaceFirst(VARIABLE_PATTERN.pattern(), variable.toString().replace("\\", "\\\\"));
            }
        }
        return result;
    }

    /**
     *  Helper to {@link #replaceVariables(String, Map, boolean)} which resolves and returns a variable value
     *  depending on mode.
     *
     * @param variableName              Name of the variable to find
     * @param variables                 Map of variable values
     * @param systemOverrideMode        Override = true, Fall back = false
     *
     * @return The variable value for variableName.
     */
    private static Object resolveVariable(String variableName, Map<String, Object> variables, boolean systemOverrideMode) {
        Object variable = null;
        if (variables != null) {
            variable = variables.get(variableName);
        }
        String environmentValue = System.getenv(variableName);
        String systemValue = System.getProperty(variableName);
        if (systemOverrideMode) {
            return resolveVariableWithSystemOverride(variable, environmentValue, systemValue);
        } else {
            return resolveVariable(variable, environmentValue, systemValue);
        }
    }

    private static Object resolveVariable(Object variable,
                                          String environmentValue,
                                          String systemValue) {
        if (variable != null) {
            return variable;
        } else {
            if (systemValue != null) {
                return systemValue;
            } else {
                return environmentValue;
            }
        }

    }

    private static Object resolveVariableWithSystemOverride(Object variable,
                                                            String environmentValue,
                                                            String systemValue) {
        if (systemValue != null) {
            return systemValue;
        } else {
            if (environmentValue != null) {
                return environmentValue;
            } else {
                return variable;
            }
        }
    }
}
