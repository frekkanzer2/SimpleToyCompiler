package Support.Nodes;

import Support.Leafs.LeafID;
import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class ReadStatNode implements StatNode {

    public String name;
    public ArrayList<LeafID> ilist = null;
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

    public ReadStatNode(String name, ArrayList<LeafID> ilist){
        this.name = name;
        this.ilist = ilist;
    }

    @Override
    public String toString() {
        return "ReadStatNode{" +
                "ilist=" + ilist +
                '}';
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
