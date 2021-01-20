package VisitorPack;

import Support.Leafs.*;
import Support.Nodes.*;
import Utils.SymbolTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeGenerationVisitor implements Visitor{

    public int STRING_SIZE = 512;
    public File file = null;
    public SymbolTable rootTable;
    public String actualFunctionName = null;
    public int statementCounter = 0;
    public int scanBoolglobalCaunter = 0;

    private void writeOnFile(String message) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(message);
            myWriter.close();

        } catch (IOException e) {
            System.err.println("Code generation error >> Cannot write on file!!!");
            System.exit(0);
        }
    }

    public File conversion(String filename, ProgramNode pnode) {

        try {
            file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
                if (file.delete()) {
                    System.out.println("Deleted the file: " + file.getName());
                    if (file.createNewFile()) {
                        System.out.println("File re-created: " + file.getName());
                    }
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred in file creation.");
            e.printStackTrace();
        }

        if (file == null) {
            System.err.println("Cannot generate the file.");
            System.exit(0);
        } else {
            writeOnFile("#include <stdio.h>\n#include <stdlib.h>\n#include <stdbool.h>\n#include <string.h>\n\n" + (String) visit(pnode));
            try {
                System.out.println(file.getCanonicalPath());
                Runtime.getRuntime().exec("astyle --style=google " + file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public Object visit(ProgramNode programNode) {

        String toWrite = "";
        rootTable = programNode.symbolTable;

        /////////add structures for function into file.c
        ArrayList<SymbolTable.SymbolTableEntry> functions_StructureToBuild = rootTable.getFunctions();
        if (functions_StructureToBuild != null) {
            toWrite += "// Structures for functions\n\n";
            for (SymbolTable.SymbolTableEntry ste: functions_StructureToBuild) {
                if (ste.outputParams.get(0) == SymbolTable.ValueType.Void) continue;
                // Structure name starts with "struct_NAME"
                toWrite += "struct struct_" + ste.id + " {\n";
                int index = 1;
                for (SymbolTable.ValueType vtype: ste.outputParams) {
                    switch (vtype) {
                        case Integer:
                            toWrite += ("int param" + index + ";\n");
                            break;
                        case Float:
                            toWrite += ("float param" + index + ";\n");
                            break;
                        case String:
                            toWrite += ("char param" + index + "[" + STRING_SIZE + "];\n");
                            break;
                        case Boolean:
                            toWrite += ("bool param" + index + ";\n");
                            break;
                    }
                    index++;
                }
                toWrite += "};\n\n";
            }
        }

        /////////add global variables into function file.c

        toWrite += "\n//Global Variables\n\n";
        for(VarDeclNode varDeclNode: programNode.varDeclList){
            toWrite += varDeclNode.accept(this) + "\n";
        }
        toWrite += "\n";

        /////////add function into function file.c

        toWrite += "\n\nchar* concat(char* first, char* second) {\n" +
                "    int i = 0, j = 0; \n" +
                "    char* str3 = malloc(" + STRING_SIZE + " * sizeof(char));\n" +
                "    // Insert the first string in the new string \n" +
                "    while (first[i] != '\\0') { \n" +
                "        str3[j] = first[i]; \n" +
                "        i++; \n" +
                "        j++; \n" +
                "    }\n" +
                "  \n" +
                "    // Insert the second string in the new string \n" +
                "    i = 0; \n" +
                "    while (second[i] != '\\0') { \n" +
                "        str3[j] = second[i]; \n" +
                "        i++; \n" +
                "        j++; \n" +
                "    } \n" +
                "    str3[j] = '\\0'; \n" +
                "    return str3;\n" +
                "}\n\n";

        toWrite += "\n//Program Functions\n\n";

        for(ProcNode procNode: programNode.procList){
            toWrite += procNode.accept(this) + "\n";
        }
        toWrite += "\n";


        return toWrite;

    }

    @Override
    public Object visit(ProcNode procNode) {
        //function name & return type
        String toWrite = "";
        String toReturnVar = "";

        if(procNode.id.value.equalsIgnoreCase("main")){
            toWrite += "int main (";
        } else {
            String funcName = "" + procNode.id.accept(this);

            if(procNode.resultTypeList.get(0).isVoid) toWrite += "void " + funcName + "(";
            else{
                toWrite += "struct struct_" + funcName + " " + funcName + "(";
                toReturnVar = "struct struct_" + funcName + " toReturn;\n";
            }
        }

        actualFunctionName = procNode.id.value; //Saving function name for later checks

        if(procNode.paramDeclList != null && procNode.paramDeclList.size() > 0){
            for(int i = 0; i < procNode.paramDeclList.size(); i++){
                if(i < procNode.paramDeclList.size() - 1 )
                    toWrite += procNode.paramDeclList.get(i).accept(this) + ", ";
                else toWrite += procNode.paramDeclList.get(i).accept(this);
            }
        }

        if(toReturnVar.equals("")) toWrite += "){\n"; //void return or main function
        else toWrite += "){\n" + toReturnVar;
        toWrite += procNode.procBody.accept(this);
        toWrite += "}\n";

        return toWrite;
    }

    @Override
    public Object visit(ProcBodyNode procBodyNode) {

        String toWrite = "";

        //local variable
        if(procBodyNode.varDeclList != null && procBodyNode.varDeclList.size() > 0){
            toWrite += "\n//Function Variables\n";
            for(VarDeclNode varDeclNode: procBodyNode.varDeclList){
                toWrite += "" + varDeclNode.accept(this) + "\n";
            }
        }

        if (procBodyNode.statListNode != null && procBodyNode.statListNode.getSize() > 0) {
            toWrite += "\n//Function body\n";
            toWrite += procBodyNode.statListNode.accept(this);
        }

        if (procBodyNode.returnsExpr != null && procBodyNode.returnsExpr.size() > 0) {
            toWrite += "\n//Return\n";
            if (actualFunctionName == null) {
                System.err.println("Code generation error > function generation error on name");
                System.exit(0);
            } else {
                // Checking callproc in returns
                int structIndex = 0;
                for (ExprNode enode: procBodyNode.returnsExpr) {
                    if (enode.value1 instanceof CallProcNode) {
                        structIndex++;
                        //struct struct_NAME str_return_NAME = callproc();
                        toWrite += "struct struct_" + ((CallProcNode) enode.value1).id.accept(this) + " str_return_" +
                                ((CallProcNode) enode.value1).id.accept(this) + "_" + structIndex + " =" + enode.accept(this) + ";\n";
                    }
                }
                // Setting returns
                structIndex = 0;
                int paramIndex = 1;
                for (int i = 0; i < procBodyNode.returnsExpr.size(); i++, paramIndex++) {
                    if (procBodyNode.returnsExpr.get(i).value1 instanceof CallProcNode) {
                        structIndex++;
                        CallProcNode functionCall = (CallProcNode) procBodyNode.returnsExpr.get(i).value1;
                        try {
                            int dimension = rootTable.containsEntry(functionCall.id.value).outputParams.size();
                            for (int j = 0; j < dimension; j++) {
                                toWrite += "toReturn.param" + paramIndex + " = str_return_" + functionCall.id.accept(this) + "_" + structIndex + ".param" + (j+1) + ";\n";
                                if (j < dimension - 1) paramIndex++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    } else toWrite += "toReturn.param" + paramIndex + " = " + procBodyNode.returnsExpr.get(i).accept(this) + ";\n";
                }
                toWrite += "return toReturn;\n";
            }
        }
        else if (actualFunctionName.equalsIgnoreCase("main")) {
            toWrite += "\n//Return\n";
            toWrite += "return 0;\n";
        }

        return toWrite;
    }

    @Override
    public Object visit(TypeNode typeNode) {
        return "";
    }

    @Override
    public Object visit(ResultTypeNode resultTypeNode) {
        return "";
    }

    @Override
    public Object visit(VarDeclNode varDeclNode) {
        String toWrite = "";

        //type
        toWrite +=  getCTypeFromTypeNodeValue(varDeclNode.type.type) + "";

        //values
        for (int i = 0; i < varDeclNode.identifiers.size(); i ++){
            if(i < varDeclNode.identifiers.size() - 1) toWrite += varDeclNode.identifiers.get(i).accept(this) + ", ";
            else toWrite += varDeclNode.identifiers.get(i).accept(this) + ";";
        }

        return toWrite;
    }

    @Override
    public Object visit(ExprNode exprNode) {
        String toWrite = "";
        if (exprNode.value1 != null && exprNode.value2 != null) {
            // 2 exprs
            String toWriteValue1 = "" + ((ExprNode) exprNode.value1).accept(this);
            String toWriteValue2 = "" +((ExprNode) exprNode.value2).accept(this);
            String opSimble = getCBinaryOperationByOperationName(((ExprNode)exprNode).name);
            if (((ExprNode) exprNode.value1).value1 instanceof CallProcNode)
                toWriteValue1 += ".param1";
            if (((ExprNode) exprNode.value2).value1 instanceof CallProcNode)
                toWriteValue2 += ".param1";
            if(!opSimble.equals("")){
                if (((ExprNode) exprNode.value1).types.get(0) == SymbolTable.ValueType.String &&
                        ((ExprNode) exprNode.value2).types.get(0) == SymbolTable.ValueType.String) {
                    if (opSimble.equals("=="))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") == 0";
                    else if (opSimble.equals("!="))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") != 0";
                    else if (opSimble.equals(">"))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") > 0";
                    else if (opSimble.equals("<"))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") < 0";
                    else if (opSimble.equals(">="))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") >= 0";
                    else if (opSimble.equals("<="))
                        toWrite = "strcmp(" + toWriteValue1 + ", " + toWriteValue2 + ") <= 0";
                    else if (opSimble.equals("+"))
                        toWrite = "concat(" + toWriteValue1 + ", " + toWriteValue2 + ")";
                }
                else toWrite = toWriteValue1  + " " + opSimble + " " + toWriteValue2;
            }
            else{
                System.err.println("Generation Code Error - Binary Operation");
                System.exit(0);
            }
        } else if (exprNode.value1 != null) {
            // 1 expr
            //unary operation case
            if (((ExprNode)exprNode).name.equalsIgnoreCase("notop") ||
                    ((ExprNode)exprNode).name.equalsIgnoreCase("uminusop")) {
                String toWriteValue1 = "" + ((ExprNode) exprNode.value1).accept(this);
                if (((ExprNode) exprNode.value1).value1 instanceof CallProcNode)
                    toWriteValue1 += ".param1";
                String opSimble = getCUnaryOperationByOperationName(((ExprNode)exprNode).name);

                if(!opSimble.equals("")) toWrite =  opSimble + " (" + toWriteValue1 + ")";
                else{
                    System.err.println("Generation Code Error - Unary Operation");
                    System.exit(0);
                }
            } else {
                // leaf cases
                if (exprNode.value1 instanceof LeafNull){

                    toWrite += "" + ((LeafNull) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof LeafIntConst){

                    toWrite += "" +   ((LeafIntConst) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof LeafFloatConst){

                    toWrite += "" + ((LeafFloatConst) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof LeafStringConst){

                    toWrite += "" + ((LeafStringConst) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof LeafBool){

                    toWrite += "" + ((LeafBool) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof LeafID){

                    toWrite += ((LeafID) exprNode.value1).accept(this);

                } else if (exprNode.value1 instanceof CallProcNode){

                    toWrite += " " + ((CallProcNode) exprNode.value1).accept(this);

                }
            }
        }
        return toWrite;
    }

    @Override
    public Object visit(IdInitNode idInitNode) {

        String toWrite = "";
        //////////////////////////////////////
        //id name
        toWrite += "" + idInitNode.varName.accept(this);
        if(idInitNode.varName.type == SymbolTable.ValueType.String){
            toWrite += "[" + STRING_SIZE + "]";
        }
        //////////////////////////////////////
        //init value
        if(idInitNode.initValue != null){
            String cValueExpr = "" + idInitNode.initValue.accept(this);
            toWrite += " = " + cValueExpr;
        }

        return toWrite;
    }

    @Override
    public Object visit(ParDeclNode parDeclNode) {
        String toWrite = "";
        String paramType =  getCTypeFromTypeNodeValue(parDeclNode.type.type);

        if(parDeclNode.identifiers != null && parDeclNode.identifiers.size() > 0){
            for(int i = 0; i < parDeclNode.identifiers.size(); i++){
                String id = "" + parDeclNode.identifiers.get(i).accept(this);

                if(i < parDeclNode.identifiers.size() - 1) {
                   if(paramType.equals("char ")) toWrite += paramType +  id + "[" + STRING_SIZE + "]" + ", ";
                   else toWrite += paramType +  id + ", ";
                }else{
                    if(paramType.equals("char ")) toWrite += paramType +  id + "[" + STRING_SIZE + "]";
                    else  toWrite += paramType +  id;
                }
            }
        }
        return toWrite;
    }


    @Override
    public Object visit(StatListNode statListNode) {
        String toWrite = "";
        if (statListNode.slist != null && statListNode.slist.size() > 0) {
            for( int i = 0; i < statListNode.slist.size(); i++ ) {
                if (statListNode.slist.get(i) instanceof CallProcNode) toWrite += "" + statListNode.slist.get(i).accept(this) + ";\n";
                else toWrite += statListNode.slist.get(i).accept(this);
            }
        }
        return toWrite;
    }

    @Override
    public Object visit(IfNode ifNode) {
        String toWrite = "";
        toWrite += "if (" +
                ((ifNode.condition.value1 instanceof CallProcNode) ? ifNode.condition.accept(this) + ".param1" : ifNode.condition.accept(this))
                + ") {\n";
        // IF BODY
        if (ifNode.ifBody != null && ifNode.ifBody.getSize() > 0) {
            toWrite += ifNode.ifBody.accept(this);
        }
        // End of if body
        toWrite += "}\n";
        // Elif list
        if (ifNode.elifList != null && ifNode.elifList.size() > 0)
            for (ElifNode elifnode: ifNode.elifList) toWrite += "" + elifnode.accept(this);
        // End of elif list
        // Else body
        if (ifNode.elseBody != null && ifNode.elseBody.getSize() > 0){
            toWrite += "else {\n";
            toWrite += ifNode.elseBody.accept(this);
            toWrite += "}\n";

        }

        // End of else
        return toWrite;
    }

    @Override
    public Object visit(ElifNode elifNode) {
        String toWrite = "";
        toWrite += "else if (" +
                ((elifNode.condition.value1 instanceof CallProcNode) ? elifNode.condition.accept(this) + ".param1" : elifNode.condition.accept(this))
                + ") {\n";
        // IF BODY
        if (elifNode.body != null && elifNode.body.getSize() > 0)
            toWrite += elifNode.body.accept(this);
        // End of if body
        toWrite += "}\n";
        return toWrite;
    }

    @Override
    public Object visit(WhileStatNode whileStatNode) {
        String toWrite = "";
        String preWhile = "";

        if (whileStatNode.preCondList != null && whileStatNode.preCondList.getSize() > 0) {
            preWhile += whileStatNode.preCondList.accept(this) + "\n";
            toWrite += preWhile;
        }
        toWrite += "while (" +
                ((whileStatNode.condition.value1 instanceof CallProcNode) ? whileStatNode.condition.accept(this) + ".param1" : whileStatNode.condition.accept(this))
                + ") {\n";
        if (whileStatNode.afterCondList != null && whileStatNode.afterCondList.getSize() > 0) {
            toWrite += whileStatNode.afterCondList.accept(this) + "\n";
        }
        if (whileStatNode.preCondList != null && whileStatNode.preCondList.getSize() > 0) {
            toWrite += preWhile;
        }
        toWrite += "}\n";

        return toWrite;
    }
    @Override
    public Object visit(WriteStatNode writeStatNode) {
        String toWrite = "";
        String printString = "";
        String variablesString = "";
        // Checking callproc in returns
        int structIndex = 0;
        int previous_stat_index = statementCounter;
        for (ExprNode enode: writeStatNode.elist) {
            if (enode.value1 instanceof CallProcNode) {
                structIndex++;
                statementCounter++;
                //struct struct_NAME str_return_NAME = callproc();
                toWrite += "struct struct_" + ((CallProcNode) enode.value1).id.accept(this) + " str_localtemp_" +
                        ((CallProcNode) enode.value1).id.accept(this) + "_" + structIndex + "_" + statementCounter + " =" + enode.accept(this) + ";\n";
            }
        }

        // Setting returns
        structIndex = 0;
        int paramIndex = 1;
        statementCounter = previous_stat_index;

        for (int i = 0; i < writeStatNode.elist.size(); i++,  paramIndex++) {
            if (writeStatNode.elist.get(i).value1 instanceof CallProcNode) {
                structIndex++;
                statementCounter++;
                CallProcNode functionCall = (CallProcNode) writeStatNode.elist.get(i).value1;
                try {
                    ArrayList <SymbolTable.ValueType> outputParams = rootTable.containsEntry(functionCall.id.value).outputParams;
                    for (int j = 0; j < outputParams.size(); j++) {

                        printString += getCScanPrintSymbolByToySemanticType(outputParams.get(j));
                        variablesString +=  "str_localtemp_" + functionCall.id.accept(this) +
                                "_" + structIndex + "_" + statementCounter + ".param" + (j+1);

                        printString += " ";
                        variablesString += ", ";

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            } else if (writeStatNode.elist.get(i).value1 instanceof LeafStringConst){
                printString +=  ((String)writeStatNode.elist.get(i).accept(this)).replace("\"", "");
            } else {
                printString += getCScanPrintSymbolByToySemanticType(writeStatNode.elist.get(i).types.get(0));
                variablesString += "("+writeStatNode.elist.get(i).accept(this) + ")";
                variablesString += ", ";

            }

        }

        if(variablesString.length() > 0) variablesString = variablesString.substring(0, variablesString.length() - 2);
        if (variablesString.equalsIgnoreCase("")) toWrite += "printf(\"" + printString + "\");";
        else toWrite += "printf(\"" + printString + "\", " + variablesString + ");";
        return toWrite;
    }

    @Override
    public Object visit(ReadStatNode readStatNode) {
        String toWrite = "";
        String tempVarForBool = "";
        String tempVarForBoolAssign = "";
        String readString = "";
        String variablesString = "";

        for(int i = 0; i < readStatNode.ilist.size(); i++){
            LeafID id = readStatNode.ilist.get(i);
            if(id.type == SymbolTable.ValueType.Boolean){
                scanBoolglobalCaunter ++;
                tempVarForBool += "int temp_bool" + scanBoolglobalCaunter + ";\n";
                tempVarForBoolAssign += id.accept(this) + " = temp_bool" + scanBoolglobalCaunter  + ";\n";
                variablesString += "&temp_bool" + scanBoolglobalCaunter;
                readString += "%d";
            } else {
                if(readStatNode.ilist.get(i).type != SymbolTable.ValueType.String) variablesString += "&" + id.accept(this);
                else variablesString +=  id.accept(this);
                readString += getCScanPrintSymbolByToySemanticType(id.type);
            }

            if(i < readStatNode.ilist.size() - 1 ){
                variablesString += ", ";
                readString += " ";
            }
        }
        toWrite += tempVarForBool;
        toWrite += "scanf(\"" + readString + "\", " + variablesString + ");";
        toWrite += tempVarForBoolAssign;

        return toWrite;
    }

    @Override
    public Object visit(AssignStatNode assignStatNode) {
        String toWrite = "";
        if (assignStatNode.idList != null && assignStatNode.idList.size() > 0) {

            // Checking callproc in returns
            int structIndex = 0;
            int previous_stat_index = statementCounter;
            for (ExprNode enode: assignStatNode.exprList) {
                if (enode.value1 instanceof CallProcNode) {
                    structIndex++;
                    statementCounter++;
                    //struct struct_NAME str_return_NAME = callproc();
                    toWrite += "struct struct_" + ((CallProcNode) enode.value1).id.accept(this) + " str_localtemp_" +
                            ((CallProcNode) enode.value1).id.accept(this) + "_" + structIndex + "_" + statementCounter + " =" + enode.accept(this) + ";\n";
                }
            }
            // after creating structs, we'll assign struct values to variables
            structIndex = 0;
            int paramIndex = 1;
            statementCounter = previous_stat_index;
            for (int i = 0, k = 0; i < assignStatNode.idList.size(); i++, k++, paramIndex++) {
                if (assignStatNode.exprList.get(k).value1 instanceof CallProcNode) {
                    structIndex++;
                    statementCounter++;
                    CallProcNode functionCall = (CallProcNode) assignStatNode.exprList.get(k).value1;
                    try {
                        int dimension = rootTable.containsEntry(functionCall.id.value).outputParams.size();
                        for (int j = 0; j < dimension; j++) {
                            toWrite += "" + assignStatNode.idList.get(i).accept(this) + " = str_localtemp_" + functionCall.id.accept(this) +
                                    "_" + structIndex + "_" + statementCounter + ".param" + (j+1) + ";\n";
                            if (j < dimension - 1) {
                                paramIndex++;
                                i++;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                } else {
                    if (assignStatNode.idList.get(i).type == SymbolTable.ValueType.String) {
                        toWrite += "strcpy(" + assignStatNode.idList.get(i).accept(this) + ", " + assignStatNode.exprList.get(k).accept(this) + ");\n";
                    }
                    else toWrite += "" + assignStatNode.idList.get(i).accept(this) + " = " + assignStatNode.exprList.get(k).accept(this) + ";\n";
                }
            }
        }
        return toWrite;
    }

    @Override
    public Object visit(CallProcNode callProcNode) {
        String toWrite = "";
        // myFunction(param1, param2, ...)
        toWrite = callProcNode.id.accept(this) + "(";
        if (callProcNode.exprList != null && callProcNode.exprList.size() > 0) {
            for(int i = 0; i < callProcNode.exprList.size(); i++) {
                if ( i < callProcNode.exprList.size() - 1 ) {
                    if (callProcNode.exprList.get(i).value1 instanceof CallProcNode) {
                        String name = (String) callProcNode.exprList.get(i).accept(this);
                        SymbolTable.SymbolTableEntry ste = null;
                        try {
                            ste = rootTable.containsEntry(((CallProcNode) callProcNode.exprList.get(i).value1).id.value);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                        for (int k = 0; k < ste.outputParams.size(); k++) {
                            toWrite += name + ".param" + (k+1) + ", ";
                        }
                    } else toWrite += callProcNode.exprList.get(i).accept(this) + ", ";
                } else {
                    if (callProcNode.exprList.get(i).value1 instanceof CallProcNode) {
                        String name = (String) callProcNode.exprList.get(i).accept(this);
                        SymbolTable.SymbolTableEntry ste = null;
                        try {
                            ste = rootTable.containsEntry(((CallProcNode) callProcNode.exprList.get(i).value1).id.value);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                        for (int k = 0; k < ste.outputParams.size(); k++) {
                            if (k < ste.outputParams.size() - 1) {
                                toWrite += name + ".param" + (k+1) + ", ";
                            } else {
                                toWrite += name + ".param" + (k+1);
                            }
                        }
                    } else toWrite += callProcNode.exprList.get(i).accept(this);
                }
            }
        }
        toWrite += ")";
        return toWrite;
    }

    @Override
    public Object visit(LeafBool leafBool) {
        return leafBool.value;
    }

    @Override
    public Object visit(LeafFloatConst leafFloatConst) {
        return leafFloatConst.value;
    }

    @Override
    public Object visit(LeafID leafID) {
        return leafID.value;
    }

    @Override
    public Object visit(LeafIntConst leafIntConst) {
        return leafIntConst.value;
    }

    @Override
    public Object visit(LeafNull leafNull) {
        return "NULL";
    }

    @Override
    public Object visit(LeafStringConst leafStringConst) {
        return "\"" + leafStringConst.value + "\"";
    }


    public String getCBinaryOperationByOperationName(String opName){
        if  (opName.equalsIgnoreCase("addop")) return "+";
        else if  (opName.equalsIgnoreCase("diffop")) return "-";
        else if (opName.equalsIgnoreCase("mulop")) return "*";
        else if (opName.equalsIgnoreCase("divop")) return "/";
        else if (opName.equalsIgnoreCase("andop")) return "&&";
        else if (opName.equalsIgnoreCase("orop")) return "||";
        else if (opName.equalsIgnoreCase("eqop")) return "==";
        else if (opName.equalsIgnoreCase("ltop")) return "<";
        else if (opName.equalsIgnoreCase("gtop")) return ">";
        else if (opName.equalsIgnoreCase("leop")) return "<=";
        else if (opName.equalsIgnoreCase("geop")) return ">=";
        else if (opName.equalsIgnoreCase("neop")) return "!=";
        else return "";
    }

    public String getCUnaryOperationByOperationName(String opName){
        if  (opName.equalsIgnoreCase("notop")) return "!";
        else if  (opName.equalsIgnoreCase("uminusop")) return "-";
        else return "";
    }

    public String getCTypeFromTypeNodeValue(String fromType){
        switch (fromType){
            case "INT":
               return  "int ";

            case "FLOAT":
                return "float ";

            case "STRING":
                return "char ";

            case "BOOL":
                return "bool ";

        }
        return "";
    }

    public String getCScanPrintSymbolByToySemanticType(SymbolTable.ValueType fromType){
        switch (fromType){
            case String:
                return  "%s";

            case Integer: case Boolean:
                return "%d";

            case Float:
                return "%f";

        }
        return "";
    }
}
