package Support.Nodes;

import VisitorPack.Visitor;

import java.util.ArrayList;

public class StatListNode{

    public String name;
    public ArrayList<StatNode> slist = null;

    public StatListNode(String name, ArrayList<StatNode> slist){
        super();
        this.name = name;
        this.slist = slist;
    }

    public void add(StatNode snode) {
        this.slist.add(0, snode);
    }

    public int getSize() {return this.slist.size();}

    @Override
    public String toString() {
        return "StatListNode{" +
                "slist=" + slist +
                ", name='" + name + '\'' +
                '}';
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

}
