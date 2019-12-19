package koye.lib.expression;

import koye.lib.common.utils.ReflectUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static koye.lib.expression.OPTION.IGNORE_CASE;

public abstract class AbstractExpression<T> implements Expression<T> {

    private final Set<OPTION> options;

    public AbstractExpression(OPTION... options) {
        checkOptions(options);
        this.options = new HashSet<>(Arrays.asList(options));
    }

    private void checkOptions(OPTION[] options) {
        Set<OPTION> availableOptionsSet = new HashSet<>(Arrays.asList(availableOptions()));
        Set<OPTION> unavailableOptionsSet = new HashSet<>(Arrays.asList(options));
        unavailableOptionsSet.removeAll(availableOptionsSet);
        if (!unavailableOptionsSet.isEmpty()) {
           throw new IllegalArgumentException("Unallowable options (" +
                   unavailableOptionsSet.stream().map(Enum::name).collect(Collectors.joining(","))+") for expression " +
                   getClass().getSimpleName());
        }
    }

    public <V> void checkExpressionType(Expression<V> e) {
        if (options.contains(IGNORE_CASE)
                && ReflectUtils.getClassGenericType(e.getClass()) != String.class) {
            throw new IllegalArgumentException("Generic type of expression must be String for option "
                    + IGNORE_CASE.name());
        }
    }

    public boolean optionsContain(OPTION option) {
        return options.contains(option);
    }

    public abstract OPTION[] availableOptions();

}
