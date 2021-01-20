package Support.Nodes;

import VisitorPack.Visitor;

public class ResultTypeNode {

    public String name;
    public TypeNode type = null;
    public boolean isVoid = false;

    public ResultTypeNode(String name, TypeNode n) {
        super();
        this.name = name;
        this.type = n;
    }

    public ResultTypeNode(String name, boolean isVoid) {
        super();
        this.name = name;
        this.isVoid = isVoid;
    }

    @Override
    public String toString() {
        return "ResultTypeNode{" +
                "type=" + type +
                ", isVoid=" + isVoid +
                ", name='" + name + '\'' +
                '}';
    }
    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
