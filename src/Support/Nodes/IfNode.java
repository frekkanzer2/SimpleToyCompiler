package Support.Nodes;

import VisitorPack.Visitor;

import java.util.ArrayList;

public class IfNode implements StatNode{
    public String name;
    public ExprNode condition = null;
    public StatListNode ifBody = null;
    public ArrayList<ElifNode> elifList = null;
    public StatListNode elseBody = null;

    public IfNode(String name, ExprNode condition, StatListNode ifBody, ArrayList<ElifNode> elifList, StatListNode elseBody) {
        this.name = name;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elifList = elifList;
        this.elseBody = elseBody;
    }

    public IfNode(String name, ExprNode condition, StatListNode ifBody, ArrayList<ElifNode> elifList) {
        this.name = name;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elifList = elifList;
        this.elseBody = null;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "condition=" + condition +
                ", ifBody=" + ifBody +
                ", elifList=" + elifList +
                ", elseBody=" + elseBody +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
