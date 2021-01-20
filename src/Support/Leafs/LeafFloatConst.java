package Support.Leafs;

import Utils.SymbolTable;
import VisitorPack.Visitor;

public class LeafFloatConst {

    public  String  name;
    public Float value = null;
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

    public LeafFloatConst(Float value){
        super();
        this.value = value;
        this.name = "LeafFloatConst";
    }

    @Override
    public String toString() {
        return "LeafFloatConst{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
