package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafID{

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

    public LeafID(String value){
        super();
        this.value = value;
        this.name = "LeafID";
    }

    @Override
    public String toString() {
        return "LeafID{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
