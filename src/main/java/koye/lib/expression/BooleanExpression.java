package koye.lib.expression;

import static koye.lib.expression.OPTION.NEGATIVE;

public abstract class BooleanExpression extends AbstractExpression<Boolean> {

    public BooleanExpression(OPTION... options) {
        super(options);
    }

    public abstract <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter);

    final public <C, V> Boolean result(C container, ValueGetter<C, V> valueGetter) {
        return optionsContain(NEGATIVE) != operationResult(container, valueGetter);
    }

    @Override
    public OPTION[] availableOptions() {
        return new OPTION[]{NEGATIVE};
    }

}
