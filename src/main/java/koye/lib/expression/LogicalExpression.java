package koye.lib.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class LogicalExpression extends BooleanExpression {

    private final List<BooleanExpression> items;

    public LogicalExpression(BooleanExpression... expressions) {
        if (expressions.length < 2) {
            throw new IllegalArgumentException("2 or more expression expected for operation " + getClass().getSimpleName());
        }
        this.items = new LinkedList<>();
        items.addAll(Arrays.asList(expressions));
    }

    public List<BooleanExpression> getItems() {
        return Collections.unmodifiableList(items);
    }

    public LogicalExpression add(BooleanExpression expression) {
        items.add(expression);
        return this;
    }

    public class AND extends LogicalExpression {

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            for (BooleanExpression item : items) {
                if (!item.result(container, valueGetter)) {
                    return false;
                }
            }
            return true;
        }
    }

    public class OR extends LogicalExpression {

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            for (BooleanExpression item : items) {
                if (item.result(container, valueGetter)) {
                    return true;
                }
            }
            return false;
        }
    }

    public class NOT extends BooleanExpression {

        private final BooleanExpression expression;

        public NOT(BooleanExpression expression) {
            this.expression = expression;
        }

        public BooleanExpression getExpression() {
            return expression;
        }

        @Override
        public OPTION[] availableOptions() {
            return OPTION.exclude(super.availableOptions(), new OPTION[]{OPTION.NEGATIVE});
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return !expression.result(container, valueGetter);
        }

    }

}
