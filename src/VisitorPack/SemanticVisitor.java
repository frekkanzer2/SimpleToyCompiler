package VisitorPack;

import Support.Leafs.*;
import Support.Nodes.*;
import Utils.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class SemanticVisitor implements Visitor {

    public Stack<SymbolTable> symTabStack = new Stack<>();
    private ArrayList<SymbolTable.ValueType> actualScopeReturnTypes = null;

    @Override
    public Object visit(ProgramNode programNode) {

        symTabStack.push(programNode.initSymbolTable());
        //----------------------------------------------------------------------
        //check if main exists
        if(programNode.varDeclList != null && programNode.varDeclList.size() > 0)
            for(VarDeclNode declNode: programNode.varDeclList)
                declNode.accept(this);

        boolean isMainChecked = false;

        for(ProcNode pNode : programNode.procList){
            if(pNode.id.value.equalsIgnoreCase("main")){
                programNode.procList.remove(pNode);
                programNode.procList.add(pNode);
                isMainChecked = true;
                break;
            }
        }
        if(!isMainChecked){
            System.err.println("Semantic Error: Main not declared");
            System.exit(0);
        }
        //----------------------------------------------------------------------
        if(programNode.procList != null && programNode.procList.size() > 0)
            for(ProcNode proc: programNode.procList)
                proc.accept(this);

        System.out.println(symTabStack.firstElement().toString());

        symTabStack.pop();

        return null;

    }

    @Override
    public Object visit(VarDeclNode varDeclNode) {

        varDeclNode.type.accept(this);

        if(varDeclNode.identifiers != null && varDeclNode.identifiers.size() > 0)
            for(IdInitNode idInitNode : varDeclNode.identifiers) {

                try {
                    SymbolTable picked = symTabStack.peek();
                    picked.createEntry_variable(idInitNode.varName.value, varDeclNode.type.type);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                idInitNode.accept(this);

            }

        return null;

    }

    @Override
    public Object visit(ReadStatNode readStatNode) {
        for(LeafID id: readStatNode.ilist){
            id.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(CallProcNode callProcNode) {

        // Taking it from sym table

        SymbolTable _st = symTabStack.peek();
        SymbolTable.SymbolTableEntry _ste = null;
        try {
            if (_st.containsKey(callProcNode.id.value)) {
                try {
                    _ste = _st.containsEntry(callProcNode.id.value);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Checking arguments with sym table

        if(callProcNode.exprList != null && callProcNode.exprList.size() > 0) {
            ArrayList<ExprNode> _tempExprNode = new ArrayList<>();
            for (ExprNode e : callProcNode.exprList) {
                e.accept(this);
                if (e.value1 instanceof CallProcNode) {
                    CallProcNode cpn = (CallProcNode) e.value1;
                    try {
                        SymbolTable.SymbolTableEntry ste = _st.containsEntry(((CallProcNode) e.value1).id.value);
                        for (SymbolTable.ValueType vt : ste.outputParams) {
                            // expr node wrapper da inserire nella lista di expr node temporanei
                            // per ogni parametro di ritorno delle callproc
                            ExprNode en = new ExprNode("", null);
                            en.setType(vt);
                            _tempExprNode.add(en);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        System.exit(0);
                    }
                } else _tempExprNode.add(e);
            }
            if (_ste.inputParams.size() != _tempExprNode.size()) {
                System.err.println("Semantic error: number of params doesn't match in function " + callProcNode.id.value + " call");
                System.exit(0);
            } else {
                for (int i = 0; i < _ste.inputParams.size(); i++) {
                    if (_tempExprNode.get(i).types.get(0) != _ste.inputParams.get(i)) {
                        System.err.println("Semantic error: type mismatch in function " + callProcNode.id.value + " call");
                        System.exit(0);
                    }
                }
            }
        } else {
            if (_ste.inputParams != null && _ste.inputParams.size() > 0) {
                System.err.println("Semantic error: number of params doesn't match in function " + callProcNode.id.value + " call");
                System.exit(0);
            }
        }

        // Assign types to output arraylist

        callProcNode.setTypes(_ste.outputParams);

        return null;
    }

    @Override
    public Object visit(ElifNode elifNode) {
        elifNode.condition.accept(this);
        if(elifNode.condition.types.size() > 1) {
            System.err.println("Semantic error: multiple condition return typ found in elif condition");
            System.exit(0);
        } else if (elifNode.condition.types.get(0) != SymbolTable.ValueType.Boolean) {
            System.err.println("Semantic error: condition type not allowed in while");
            System.exit(0);
        }

        elifNode.body.accept(this);

        return null;
    }

    @Override
    public Object visit(ExprNode exprNode) {
        if (exprNode.value1 != null && exprNode.value2 != null) {
            // 2 exprs
            ((ExprNode) exprNode.value1).accept(this);
            ((ExprNode) exprNode.value2).accept(this);
            if (((ExprNode) exprNode.value1).value1 instanceof CallProcNode)
                if (((ExprNode) exprNode.value1).types.size() > 1) {
                    System.err.println("Semantic error: function call as first parameter returns 2 or more values in a binary operation. This is not allowed.");

                    System.exit(0);
                }
            if (((ExprNode) exprNode.value2).value1 instanceof CallProcNode)
                if (((ExprNode) exprNode.value2).types.size() > 1) {
                    System.err.println("Semantic error: function call as second parameter returns 2 or more values in a binary operation. This is not allowed.");
                    System.err.println( exprNode.value1);
                    System.err.println( exprNode.value2);
                    System.exit(0);
                }
            if (exprNode.name.equalsIgnoreCase("addop") ||
                    exprNode.name.equalsIgnoreCase("diffop") ||
                    exprNode.name.equalsIgnoreCase("mulop") ||
                    exprNode.name.equalsIgnoreCase("divop")) {
                if (exprNode.name.equalsIgnoreCase("addop") &&
                        ((((ExprNode) exprNode.value1).types.get(0) == SymbolTable.ValueType.String &&
                        ((ExprNode) exprNode.value2).types.get(0) != SymbolTable.ValueType.String) ||
                        (((ExprNode) exprNode.value1).types.get(0) != SymbolTable.ValueType.String &&
                                ((ExprNode) exprNode.value2).types.get(0) == SymbolTable.ValueType.String))) {
                    // viene controllato prima che value1 sia stringa e che value 2 sia diverso da stringa per lanciare l'errore
                    // si può, però, presentare la casistica in cui value1 NON sia stringa e value2 lo sia, quindi la situazione inversa
                    // tale problema non era gestito, quindi ho effettuato il controllo anche della situazione viceversa
                    System.err.println("Semantic error: type not compatible with operation");
                    System.exit(0);
                } else if (!exprNode.name.equalsIgnoreCase("addop") &&
                        (((ExprNode) exprNode.value1).types.get(0) == SymbolTable.ValueType.String ||
                                ((ExprNode) exprNode.value2).types.get(0) == SymbolTable.ValueType.String)) {
                    System.err.println("Semantic error: type not compatible with operation");
                    System.exit(0);
                }
                SymbolTable.ValueType takenType = getType_operations(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0));
                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with operation");
                    System.exit(0);
                } else exprNode.setType(takenType);
            }
            else if (exprNode.name.equalsIgnoreCase("andop") ||
                    exprNode.name.equalsIgnoreCase("orop")){

                SymbolTable.ValueType takenType = getType_andor(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0));
                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with operation");
                    System.exit(0);
                } else exprNode.setType(takenType);
            } else {
                SymbolTable.ValueType takenType = getType_boolean(
                        ((ExprNode) exprNode.value1).types.get(0),
                        ((ExprNode) exprNode.value2).types.get(0));
                if (takenType == null) {
                    System.err.println("Semantic error: type not compatible with operation");
                    System.exit(0);
                } else exprNode.setType(takenType);
            }
        } else if (exprNode.value1 != null) {
            // 1 expr
            // leaf cases
            if (exprNode.value1 instanceof LeafNull){
                ((LeafNull) exprNode.value1).accept(this);
                exprNode.setType(((LeafNull) exprNode.value1).type);
            } else if (exprNode.value1 instanceof LeafIntConst){
                ((LeafIntConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafIntConst) exprNode.value1).type);
            } else if (exprNode.value1 instanceof LeafFloatConst){
                ((LeafFloatConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafFloatConst) exprNode.value1).type);
            } else if (exprNode.value1 instanceof LeafStringConst){
                ((LeafStringConst) exprNode.value1).accept(this);
                exprNode.setType(((LeafStringConst) exprNode.value1).type);
            } else if (exprNode.value1 instanceof LeafBool){
                ((LeafBool) exprNode.value1).accept(this);
                exprNode.setType(((LeafBool) exprNode.value1).type);
            } else if (exprNode.value1 instanceof LeafID){
                ((LeafID) exprNode.value1).accept(this);
                exprNode.setType(((LeafID) exprNode.value1).type);
            } else if (exprNode.value1 instanceof CallProcNode){
                ((CallProcNode) exprNode.value1).accept(this);
                exprNode.setTypes(((CallProcNode) exprNode.value1).types);
            } else if (((ExprNode)exprNode).name.equalsIgnoreCase("uminusop")) {

                ((ExprNode) exprNode.value1).accept(this);

                if (((ExprNode) exprNode.value1).value1 instanceof CallProcNode)
                    if (((ExprNode) exprNode.value1).types.size() > 1) {
                        System.err.println("Semantic error: function call as first parameter returns 2 or more values in a Unary operation. This is not allowed.");
                        System.exit(0);
                    }

                if (isType_uminus(((ExprNode) exprNode.value1).types.get(0)))
                    exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
                else {
                    System.err.println("Semantic error: uminus conversion error, type mismatch");
                    System.exit(0);
                }
            } else if (((ExprNode)exprNode).name.equalsIgnoreCase("notop")) {
                ((ExprNode) exprNode.value1).accept(this);

                if (((ExprNode) exprNode.value1).value1 instanceof CallProcNode)
                    if (((ExprNode) exprNode.value1).types.size() > 1) {
                        System.err.println("Semantic error: function call as first parameter returns 2 or more values in a Unary operation. This is not allowed.");
                        System.exit(0);
                    }

                if (isType_not(((ExprNode) exprNode.value1).types.get(0)))
                    exprNode.setType(((ExprNode) exprNode.value1).types.get(0));
                else {
                    System.err.println("Semantic error: not operation conversion error, type mismatch");
                    System.exit(0);
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(IdInitNode idInitNode) {
        idInitNode.varName.accept(this);

        if(idInitNode.initValue != null){
            idInitNode.initValue.accept(this);
            if(idInitNode.initValue.types.size() > 1){
                System.err.println("Semantic error: multiple value to initialize a variable");
                System.exit(0);
            }

            if (!checkTypeCompatibility(idInitNode.varName.type, idInitNode.initValue.types.get(0))) {
                System.err.println("Semantic error: wrong initialization for variable " + idInitNode.varName.value);
                System.exit(0);
            }

        }
        return null;
    }

    @Override
    public Object visit(IfNode ifNode) {
        ifNode.condition.accept(this);
        if(ifNode.condition.types.size() > 1) {
            System.err.println("Semantic error: multiple condition return typ found in if condition");
            System.exit(0);
        } else if (ifNode.condition.types.get(0) != SymbolTable.ValueType.Boolean) {
            System.err.println("Semantic error: condition type not allowed in IF statement");
            System.exit(0);
        }

        if(ifNode.ifBody != null && ifNode.ifBody.getSize() > 0) ifNode.ifBody.accept(this);

        if(ifNode.elifList != null && ifNode.elifList.size() > 0){
            for(ElifNode elifNode: ifNode.elifList)elifNode.accept(this);
        }
        if(ifNode.elseBody != null && ifNode.elseBody.getSize() > 0) ifNode.elseBody.accept(this);

        return null;
    }

    @Override
    public Object visit(ParDeclNode parDeclNode) {

        // Local scope
        for (LeafID lid : parDeclNode.identifiers) {
            try {

                symTabStack.peek().createEntry_variable(lid.value, parDeclNode.type.type);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        return null;

    }

    @Override
    public Object visit(ProcNode procNode) {

        ArrayList<SymbolTable.ValueType> in, out;
        in = new ArrayList<>();
        out = new ArrayList<>();

        try {

            if (procNode.paramDeclList != null && procNode.paramDeclList.size() > 0) {
                if (procNode.id.value.equals("main")) {
                    System.err.println("Semantic Error: main must not have parameters");
                    System.exit(0);
                }

                for (ParDeclNode pdn : procNode.paramDeclList) {
                    SymbolTable.ValueType vtype = SymbolTable.StringToType(pdn.type.type);
                    for (int i = 0; i < pdn.identifiers.size(); i++) in.add(vtype);
                }
            }
            if (procNode.resultTypeList != null && procNode.resultTypeList.size() > 0)
                for (ResultTypeNode rtn : procNode.resultTypeList) {
                    SymbolTable.ValueType vtype;
                    if (!rtn.isVoid) {
                        vtype = SymbolTable.StringToType(rtn.type.type);
                        out.add(vtype);
                    } else out.add(SymbolTable.ValueType.Void);
                }
            symTabStack.firstElement().createEntry_function(procNode.id.value, in, out);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        SymbolTable _st = procNode.initSymbolTable();
        _st.setFatherSymTab(symTabStack.firstElement());
        symTabStack.push(_st);

        //////////////////////////////////////////////////

        if (procNode.paramDeclList != null && procNode.paramDeclList.size() > 0) {
            for (ParDeclNode pdn : procNode.paramDeclList)
                pdn.accept(this);
        }
        procNode.procBody.accept(this);

        //////////////////////////////////////////////////
        //check return values
        if(actualScopeReturnTypes != null){
            if (procNode.id.value.equalsIgnoreCase("main")) {
                try {
                    SymbolTable.SymbolTableEntry main_entry = symTabStack.peek().containsEntry("main");
                    if (main_entry.outputParams.get(0) != SymbolTable.ValueType.Void) throw new Exception("Semantic error: main function must be declared void");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            if (actualScopeReturnTypes.size() == 0) {
                if (out.size() == 1 && out.get(0) != SymbolTable.ValueType.Void) {
                    System.err.println("Missing return in " + procNode.id.value);
                    System.exit(0);
                }
            } else {
                if (out.size() > 0 && out.get(0) == SymbolTable.ValueType.Void){
                    System.err.println("Semantic error: return values not allowed in void declared function " + procNode.id.value);
                    System.exit(0);
                }
                if (actualScopeReturnTypes.size() != out.size()){
                    System.err.println("Semantic error: return values list are not in the same of return types declared for function " + procNode.id.value);
                    System.exit(0);
                }
                for(int i = 0; i < actualScopeReturnTypes.size(); i++){
                    if (!checkTypeCompatibility(out.get(i), actualScopeReturnTypes.get(i))) {
                        System.err.println("Semantic error: return attributes in function " + procNode.id.value + " does not match with types declared in function signature");
                        System.exit(0);
                    }
                }
            }
            actualScopeReturnTypes = null;
        }

        //////////////////////////////////////////////

        System.out.println(symTabStack.peek().toString());
        symTabStack.pop();

        return null;
    }

    @Override
    public Object visit(ProcBodyNode procBodyNode) {


        if (procBodyNode.varDeclList != null && procBodyNode.varDeclList.size() > 0)
            for (VarDeclNode vdl : procBodyNode.varDeclList)
                vdl.accept(this);

        if(procBodyNode.statListNode != null && procBodyNode.statListNode.getSize() > 0)
            procBodyNode.statListNode.accept(this);

        if(procBodyNode.returnsExpr != null && procBodyNode.returnsExpr.size() > 0){

            actualScopeReturnTypes = new  ArrayList<SymbolTable.ValueType>();

            for(ExprNode returnExpr: procBodyNode.returnsExpr){
                returnExpr.accept(this);
                for(SymbolTable.ValueType valueType: returnExpr.types)
                    if (valueType != SymbolTable.ValueType.Void && valueType != SymbolTable.ValueType.Null)
                        actualScopeReturnTypes.add(valueType);
            }

        } else {

            actualScopeReturnTypes = new  ArrayList<SymbolTable.ValueType>();

        }

        return null;

    }

    @Override
    public Object visit(ResultTypeNode resultTypeNode) {
        return null;
    }

    @Override
    public Object visit(StatListNode statListNode) {
        if (statListNode.getSize() > 0)
            for (StatNode snode : statListNode.slist) {
                snode.accept(this);
            }
        return null;
    }

    @Override
    public Object visit(TypeNode typeNode) {
        return null;
    }

    @Override
    public Object visit(WhileStatNode whileStatNode) {
        whileStatNode.condition.accept(this);
        if (whileStatNode.condition.types.size() > 1) {
            System.err.println("Semantic error: multiple conditions found in while");
            System.exit(0);
        } else if (whileStatNode.condition.types.get(0) != SymbolTable.ValueType.Boolean) {
            System.err.println("Semantic error: condition type not allowed in while");
            System.exit(0);
        }
        if (whileStatNode.preCondList != null && whileStatNode.preCondList.getSize() > 0)
            whileStatNode.preCondList.accept(this);
        if (whileStatNode.afterCondList.getSize() > 0)
            whileStatNode.afterCondList.accept(this);
        return null;
    }

    @Override
    public Object visit(WriteStatNode writeStatNode) {
        for(ExprNode expr: writeStatNode.elist){
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LeafBool leafBool) {
        leafBool.setType("bool");
        return null;
    }

    @Override
    public Object visit(LeafFloatConst leafFloatConst) {
        leafFloatConst.setType("float");
        return null;
    }

    @Override
    public Object visit(LeafID leafID) {
        SymbolTable _st = symTabStack.peek();
        try {
            if (_st.containsKey(leafID.value)) {
                SymbolTable.SymbolTableEntry _ste = null;
                try {
                    _ste = _st.containsEntry(leafID.value);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                leafID.type = _ste.vtype;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    @Override
    public Object visit(LeafIntConst leafIntConst) {
        leafIntConst.setType("int");
        return null;
    }

    @Override
    public Object visit(LeafNull leafNull) {
        leafNull.setType("null");
        return null;
    }

    @Override
    public Object visit(LeafStringConst leafStringConst) {
        leafStringConst.setType("string");
        return null;
    }

    @Override
    public Object visit(AssignStatNode assignStatNode) {

        ArrayList<SymbolTable.ValueType> valueTypesTMP = new ArrayList<>();
        for(LeafID id : assignStatNode.idList) id.accept(this);
        for(ExprNode value : assignStatNode.exprList){
            value.accept(this);
            for(SymbolTable.ValueType valueType: value.types) valueTypesTMP.add(valueType);
        }

        if(valueTypesTMP.size() != assignStatNode.idList.size()) {
            System.err.println("Semantic Error: ID does not match whith assign values in assign stat");
            System.exit(0);
        }

        for(int i = 0; i < valueTypesTMP.size(); i++){
            if(!checkTypeCompatibility(assignStatNode.idList.get(i).type, valueTypesTMP.get(i))) {
                System.err.println("Semantic Error: ID type does not match" +
                        " whith Assign Value type in assign stat for id: " + assignStatNode.idList.get(i).value);
                System.exit(0);
            }
        }

        return null;
    }

    // Type checking

    public static boolean checkTypeCompatibility(SymbolTable.ValueType variable, SymbolTable.ValueType assign){
        if (assign == SymbolTable.ValueType.Null)
            return true;
        if (variable == SymbolTable.ValueType.Integer && assign == SymbolTable.ValueType.Integer)
            return true;
        if (variable == SymbolTable.ValueType.Float && assign == SymbolTable.ValueType.Float)
            return true;
        if (variable == SymbolTable.ValueType.Float && assign == SymbolTable.ValueType.Integer)
            return true;
        if (variable == SymbolTable.ValueType.Boolean && assign == SymbolTable.ValueType.Boolean)
            return true;
        if (variable == SymbolTable.ValueType.String && assign == SymbolTable.ValueType.String)
            return true;
        return false;
    }

    public static boolean isType_uminus(SymbolTable.ValueType t1){
        if (t1 == SymbolTable.ValueType.Integer)
            return true;
        if (t1 == SymbolTable.ValueType.Float)
            return true;
        return false;
    }

    public static boolean isType_not(SymbolTable.ValueType t1){
        if (t1 == SymbolTable.ValueType.Boolean)
            return true;
        return false;
    }

    public static SymbolTable.ValueType getType_operations(SymbolTable.ValueType t1, SymbolTable.ValueType t2){
        if (t1 == SymbolTable.ValueType.Integer && t2 == SymbolTable.ValueType.Integer)
            return SymbolTable.ValueType.Integer;
        if (t1 == SymbolTable.ValueType.Integer && t2 == SymbolTable.ValueType.Float)
            return SymbolTable.ValueType.Float;
        if (t1 == SymbolTable.ValueType.Float && t2 == SymbolTable.ValueType.Integer)
            return SymbolTable.ValueType.Float;
        if (t1 == SymbolTable.ValueType.Float && t2 == SymbolTable.ValueType.Float)
            return SymbolTable.ValueType.Float;
        if (t1 == SymbolTable.ValueType.String && t2 == SymbolTable.ValueType.String)
            return SymbolTable.ValueType.String;
        return null;
    }

    public static SymbolTable.ValueType getType_boolean(SymbolTable.ValueType t1, SymbolTable.ValueType t2){
        if (t1 == SymbolTable.ValueType.Boolean && t2 == SymbolTable.ValueType.Boolean)
            return SymbolTable.ValueType.Boolean;
        if (t1 == SymbolTable.ValueType.Integer && t2 == SymbolTable.ValueType.Integer)
            return SymbolTable.ValueType.Boolean;
        if (t1 == SymbolTable.ValueType.Integer && t2 == SymbolTable.ValueType.Float)
            return SymbolTable.ValueType.Boolean;
        if (t1 == SymbolTable.ValueType.Float && t2 == SymbolTable.ValueType.Integer)
            return SymbolTable.ValueType.Boolean;
        if (t1 == SymbolTable.ValueType.Float && t2 == SymbolTable.ValueType.Float)
            return SymbolTable.ValueType.Boolean;
        if (t1 == SymbolTable.ValueType.String && t2 == SymbolTable.ValueType.String)
            return SymbolTable.ValueType.Boolean;
        return null;
    }

    public static SymbolTable.ValueType getType_andor(SymbolTable.ValueType t1, SymbolTable.ValueType t2){
        if (t1 == SymbolTable.ValueType.Boolean && t2 == SymbolTable.ValueType.Boolean)
            return SymbolTable.ValueType.Boolean;
        return null;
    }

}
