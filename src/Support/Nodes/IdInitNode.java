package Support.Nodes;

import Support.Leafs.LeafID;
import Utils.SymbolTable;
import VisitorPack.Visitor;

public class IdInitNode{

    public String name;
    public LeafID varName = null;
    public ExprNode initValue = null;

    public IdInitNode(LeafID varName, ExprNode initValue) {
        this.name = "IdInitOP";
        this.varName = varName;
        this.initValue = initValue;
    }

    public IdInitNode(LeafID varName) {
        this.name = "IdInitOP";
        this.varName = varName;
        this.initValue = null;
    }

    @Override
    public String toString() {
        return "IdInitNode{" +
                "varName=" + varName +
                ", initValue=" + initValue +
                ", name='" + name + '\'' +
                '}';
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
