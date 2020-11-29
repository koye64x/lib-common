package koye.lib.expr;

public class Const implements Expr {

    private final Object value;

    public Const(Object value) {
        assert value != null;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
