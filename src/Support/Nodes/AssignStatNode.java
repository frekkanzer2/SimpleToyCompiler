package Support.Nodes;

import Support.Leafs.LeafID;
import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class AssignStatNode implements StatNode {

    public String name = null;
    public ArrayList<LeafID> idList = null;
    public ArrayList<ExprNode> exprList = null;
    // Semantic check
    public SymbolTable.ValueType type = null;

    public void setType(String t) {
        try {
            this.type = SymbolTable.StringToType(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public AssignStatNode(String name, ArrayList<LeafID> idList, ArrayList<ExprNode> exprList) {
        super();
        this.name = name;
        this.idList = idList;
        this.exprList = exprList;
    }

    @Override
    public String toString() {
        return "AssignStatNode{" +
                "idList=" + idList +
                ", exprList=" + exprList +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
