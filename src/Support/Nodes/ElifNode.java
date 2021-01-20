package Support.Nodes;

import VisitorPack.Visitor;

public class ElifNode {
    public String  name = null;
    public ExprNode condition = null;
    public StatListNode body = null;

    public ElifNode(ExprNode condition, StatListNode body) {
        this.name = "ElifOp";
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ElifNode{" +
                "condition=" + condition +
                ", body=" + body +
                ", name='" + name + '\'' +
                '}';
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
