package koye.lib.expr;

public class Term implements Expr {

    private final String name;

    public Term(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
