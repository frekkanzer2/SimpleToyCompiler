package VisitorPack;

import Support.Leafs.*;
import Support.Nodes.*;

public interface Visitor {
    public Object visit(ProgramNode programNode);

    public Object visit(VarDeclNode varDeclNode);

    public Object visit(ReadStatNode readStatNode);

    public Object visit(CallProcNode callProcNode);

    public Object visit(ElifNode elifNode);

    public Object visit(ExprNode exprNode);

    public Object visit(IdInitNode idInitNode);

    public Object visit(IfNode ifNode);

    public Object visit(ParDeclNode parDeclNode);

    public Object visit(ProcBodyNode procBodyNode);

    public Object visit(ProcNode procNode);

    public Object visit(ResultTypeNode resultTypeNode);

    public Object visit(StatListNode statListNode);

    public Object visit(TypeNode typeNode);

    public Object visit(WhileStatNode whileStatNode);

    public Object visit(WriteStatNode writeStatNode);

    public Object visit(LeafBool leafBool);

    public Object visit(LeafFloatConst leafFloatConst);

    public Object visit(LeafID leafID);

    public Object visit(LeafIntConst leafIntConst);

    public Object visit(LeafNull leafNull);

    public Object visit(LeafStringConst leafStringConst);

    public Object visit(AssignStatNode assignStatNode);
}
