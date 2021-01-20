package Support.Nodes;

import Support.Leafs.LeafID;
import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class ProcNode {

    public SymbolTable symbolTable;

    public String name;
    public LeafID id = null;
    public ArrayList<ParDeclNode> paramDeclList = null;
    public ArrayList<ResultTypeNode> resultTypeList = null;
    public ProcBodyNode procBody = null;

    public SymbolTable initSymbolTable() {
        this.symbolTable = new SymbolTable();
        this.symbolTable.symbolTableName = "Function " + id.value + " scope";
        return this.symbolTable;
    }

    public ProcNode(String name, LeafID id, ArrayList<ParDeclNode> paramDeclList,
                    ArrayList<ResultTypeNode> resultTypeList, ProcBodyNode procBody){
        super();
        this.id = id;
        this.name = name;
        this.paramDeclList = paramDeclList;
        this.resultTypeList = resultTypeList;
        this.procBody = procBody;

    }

    public ProcNode(String name, LeafID id, ArrayList<ResultTypeNode> resultTypeList,
                    ProcBodyNode procBody){
        super();
        this.id = id;
        this.name = name;
        this.resultTypeList = resultTypeList;
        this.procBody = procBody;

    }

    @Override
    public String toString() {
        return "ProcNode{" +
                "id=" + id +
                ", paramDeclList=" + paramDeclList +
                ", resultTypeList=" + resultTypeList +
                ", procBody=" + procBody +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
