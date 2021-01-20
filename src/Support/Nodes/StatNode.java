package Support.Nodes;

import VisitorPack.Visitor;

public interface StatNode {

    public Object accept(Visitor v);

}
