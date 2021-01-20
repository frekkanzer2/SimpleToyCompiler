package Support.Nodes;

import Support.Leafs.LeafID;
import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class CallProcNode  implements StatNode {

    public String name = null;
    public LeafID id = null;
    public ArrayList<ExprNode> exprList = null;
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

    public CallProcNode(String name, LeafID id){
        super();
        this.name = name;
        this.id = id;
        this.exprList = null;
    }

    public CallProcNode(String name, LeafID id, ArrayList<ExprNode> exprList){
        super();
        this.name = name;
        this.id = id;
        this.exprList = exprList;
    }

    @Override
    public String toString() {
        return "CallProcNode{" +
                "id=" + id +
                ", exprList=" + exprList +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
