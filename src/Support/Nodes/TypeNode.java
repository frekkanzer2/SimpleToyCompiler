package Support.Nodes;

import VisitorPack.Visitor;

public class TypeNode {

    public String name;
    public String type;


   public TypeNode (String type){
       this.name = "TypeNode";
       this.type = type;
   }

    @Override
    public String toString() {
        return "TypeNode{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
