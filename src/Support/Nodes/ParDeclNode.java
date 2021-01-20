package Support.Nodes;

import Support.Leafs.LeafID;
import VisitorPack.Visitor;

import java.util.ArrayList;

public class ParDeclNode{
    public String name;
    public TypeNode type = null;
    public ArrayList<LeafID> identifiers = null;

    public ParDeclNode(String s, TypeNode type, ArrayList<LeafID> identifiers) {
        this.name = s;
        this.type = type;
        this.identifiers = identifiers;
    }

    public void addIdentifier(LeafID id){
        identifiers.add(0, id);
    }

    @Override
    public String toString() {
        return "ParDeclNode{" +
                "type=" + type +
                ", identifiers=" + identifiers +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
