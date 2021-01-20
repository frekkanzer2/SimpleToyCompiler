package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafStringConst{

    public String name;
    public String value = null;
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

    public LeafStringConst(String value){
        super();
        this.value = value;
        this.name = "LeafStringConst";
    }

    @Override
    public String toString() {
        return "LeafStringConst{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
