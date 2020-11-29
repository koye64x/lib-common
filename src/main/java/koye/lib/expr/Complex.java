package koye.lib.expr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Complex implements Expr {

    private final OPER oper;

    private final List<Expr> args;

    private final Set<OPT> optSet = new HashSet<>();

    protected Complex(OPER oper, Set<OPT> opts, List<Expr> args) {
        this.oper = oper;
        this.args = args;
        if (opts != null) {
            optSet.addAll(opts);
        }
    }

    protected Complex(OPER oper, List<Expr> args) {
        this.oper = oper;
        this.args = args;
    }

    protected Complex(OPER oper, Set<OPT> opts, Expr... args) {
        this(oper, opts, Arrays.asList(args));
    }

    protected Complex(OPER oper, Expr... args) {
        this(oper, Arrays.asList(args));
    }

    public OPER getOper() {
        return oper;
    }

    public List<Expr> getArgs() {
        return args;
    }

}
