package Support.Nodes;

import VisitorPack.Visitor;

import java.util.ArrayList;

public class ProcBodyNode{
    public String name;
    public ArrayList<VarDeclNode> varDeclList = null;
    public StatListNode statListNode = null;
    public ArrayList<ExprNode> returnsExpr = null;

    public ProcBodyNode(ArrayList<VarDeclNode> varDeclList, StatListNode statListNode, ArrayList<ExprNode> returnsExpr) {
        this.name = "ProcBodyNode";
        this.varDeclList = varDeclList;
        this.statListNode = statListNode;
        this.returnsExpr = returnsExpr;
    }

    public ProcBodyNode(ArrayList<VarDeclNode> varDeclList, ArrayList<ExprNode> returnsExpr) {
        this.name = "ProcBodyNode";
        this.varDeclList = varDeclList;
        this.returnsExpr = returnsExpr;
    }

    @Override
    public String toString() {
        return "ProcBodyNode{" +
                "varDeclList=" + varDeclList +
                ", statListNode=" + statListNode +
                ", returnsExpr=" + returnsExpr +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}