package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafIntConst {

    public String name;
    public Integer value = null;
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

    public LeafIntConst(Integer value){
        super();
        this.value = value;
        this.name = "LeafIntConst";
    }

    @Override
    public String toString() {
        return "LeafIntConst{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
