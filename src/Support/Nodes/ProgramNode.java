package Support.Nodes;

import Utils.SymbolTable;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class ProgramNode {

    public SymbolTable symbolTable;

    public String name;
    public ArrayList<VarDeclNode> varDeclList = null;
    public ArrayList<ProcNode> procList = null;

    public SymbolTable initSymbolTable() {
        this.symbolTable = new SymbolTable();
        this.symbolTable.symbolTableName = "Global scope";
        return this.symbolTable;
    }

    public ProgramNode (String name, ArrayList<VarDeclNode> varDeclList,
                        ArrayList<ProcNode> procList) {
        super();
        this.name = name;
        this.varDeclList = varDeclList;
        this.procList = procList;

    }

    public ProgramNode (String name, ArrayList<ProcNode> procList) {
        super();
        this.name = name;
        this.procList = procList;

    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "varDeclList=" + varDeclList +
                ", procList=" + procList +
                ", name='" + name + '\'' +
                '}';
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
