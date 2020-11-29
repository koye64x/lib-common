package koye.lib.expr;

import java.util.*;
import java.util.stream.Collectors;

public interface Expr {

    static Expr equal(Expr a, Expr b) {
        return new Complex(OPER.EQUAL, a, b);
    }

    static Expr notEqual(Expr a, Expr b) {
        return new Complex(OPER.NOT, equal(a, b));
    }

    static Expr great(Expr a, Expr b) {
        return new Complex(OPER.GREAT, a, b);
    }

    static Expr greatOrEqual(Expr a, Expr b) {
        return new Complex(OPER.GREAT_OR_EQUAL, a, b);
    }

    static Expr less(Expr a, Expr b) {
        return new Complex(OPER.LESS, a, b);
    }

    static Expr lessOrEqual(Expr a, Expr b) {
        return new Complex(OPER.LESS_OR_EQUAL, a, b);
    }

    static Expr in(Expr a, Collection<?> values) {
        List<Expr> args = new ArrayList<>(Collections.singletonList(a));
        List<Const> constants = values.stream().map(Const::new).collect(Collectors.toList());
        args.addAll(constants);
        return new Complex(OPER.IN, args);
    }

    static Expr between(Term a, Object b, Object c) {
        List<Expr> args = new ArrayList<>(Collections.singletonList(a));
        args.add(new Const(b));
        args.add(new Const(c));
        return new Complex(OPER.BETWEEN, args);
    }

    static Expr contain(Term term, String subStr) {
        return new Complex(OPER.CONTAIN, term, new Const(subStr));
    }

    static Expr startsWith(Term term, String subStr) {
        return new Complex(OPER.STARTS_WITH, term, new Const(subStr));
    }

    static Expr endsWith(Term term, String subStr) {
        return new Complex(OPER.ENDS_WITH, term, new Const(subStr));
    }

    static Expr isNull(String termName) {
        return new Complex(OPER.IS_NULL, new Term(termName));
    }

    static Expr isNotNull(String termName) {
        return new Complex(OPER.IS_NOT_NULL, new Term(termName));
    }

    static Expr and(Expr... args) {
        return new Complex(OPER.AND, args);
    }

    static Expr or(Expr... args) {
        return new Complex(OPER.OR, args);
    }

    static Expr not(Expr arg) {
        return new Complex(OPER.NOT, arg);
    }

}
