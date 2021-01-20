package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafNull {

    public String name;
    public Object value = null;
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

    public LeafNull(){
        super();
        this.name = "LeafNull";
    }

    @Override
    public String toString() {
        return "LeafNull{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
