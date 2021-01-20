package Support.Nodes;

import VisitorPack.Visitor;

public class WhileStatNode implements StatNode {

    public String name;
    public StatListNode preCondList = null;
    public StatListNode afterCondList = null;
    public ExprNode condition = null;

    public WhileStatNode (String name, ExprNode condition, StatListNode a_condList) {
        this.name = name;
        this.condition = condition;
        this.afterCondList = a_condList;
        this.preCondList = null;
    }

    public WhileStatNode (String name, StatListNode p_condList, ExprNode condition, StatListNode a_condList) {
        this.name = name;
        this.condition = condition;
        this.afterCondList = a_condList;
        this.preCondList = p_condList;
    }

    @Override
    public String toString() {
        return "WhileStatNode{" +
                "preCondList=" + preCondList +
                ", afterCondList=" + afterCondList +
                ", condition=" + condition +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
