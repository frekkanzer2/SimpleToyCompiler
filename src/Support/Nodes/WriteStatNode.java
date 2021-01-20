package Support.Nodes;

import VisitorPack.Visitor;

import java.util.ArrayList;

public class WriteStatNode implements StatNode {

    public String name;
    public ArrayList<ExprNode> elist = null;

    public WriteStatNode(String name, ArrayList<ExprNode> elist){
        this.name = name;
        this.elist = elist;
    }

    public WriteStatNode(ArrayList<ExprNode> elist) {
        this.elist = elist;
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
