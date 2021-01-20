package Support.Nodes;

import VisitorPack.Visitor;

import java.util.ArrayList;

public class VarDeclNode{

    public String name;
    public TypeNode type = null;
    public ArrayList<IdInitNode> identifiers = null;

    public VarDeclNode(String name,  TypeNode type, ArrayList<IdInitNode> identifiers) {
        this.name = name;
        this.type = type;
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "VarDeclNode{" +
                "type=" + type +
                ", identifiers=" + identifiers +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}


