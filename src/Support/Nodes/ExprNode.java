package Support.Nodes;

import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class ExprNode {

    public String name;
    public Object value1 = null;
    public Object value2 = null; //If value2==null -> this node has only one sub-node
    // Semantic check
    public ArrayList<SymbolTable.ValueType> types = new ArrayList<>();

    public void setTypes(ArrayList t) {
        try {
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i) instanceof String)
                    this.types.add(SymbolTable.StringToType((String)t.get(i)));
                else this.types.add((SymbolTable.ValueType) t.get(i));
            }
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public void setType(String t) {
        try {
            this.types.add(SymbolTable.StringToType(t));
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public void setType(SymbolTable.ValueType t) {
        try {
            this.types.add(t);
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
    }

    public ExprNode(String name, Object n){
        this.value1 = n;
        this.name = name;
    }

    public ExprNode(String name, Object n, Object m) {
        this.value1 = n;
        this.value2 = m;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExprNode{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
