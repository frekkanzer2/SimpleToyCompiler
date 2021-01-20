package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafBool  {

    public String name;
    public boolean value;
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

    public LeafBool(boolean value){
        super();
        this.value = value;
        this.name = "LeafBool";
    }

    @Override
    public String toString() {
        return "LeafBool{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
