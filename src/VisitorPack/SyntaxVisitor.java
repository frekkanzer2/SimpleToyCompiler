package VisitorPack;

import Support.Leafs.*;
import Support.Nodes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SyntaxVisitor implements Visitor{

    private Document xmlDocument;

    /**
     * Constructor and creator of XML-document
     */
    public SyntaxVisitor() {
        super();
        this.createDocument();
    }

    /**
     * Method to append the root of the tree
     * @param el is the root of the tree
     */
    public void appendRoot(Element el) {
        this.xmlDocument.appendChild(el);
    }

    /**
     * Method to create factory and builder for the XML-Document
     */
    public void createDocument() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.xmlDocument = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to print the XML
     */
    public void toXml() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(this.xmlDocument);
            StreamResult result = new StreamResult(new File("output.xml"));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Element visit(ProgramNode p) {
        Element el = this.xmlDocument.createElement("ProgramOP");

        if(p.varDeclList != null && p.varDeclList.size() > 0){
            Element eDeclList = this.xmlDocument.createElement("VarDeclList");
            for(VarDeclNode declNode: p.varDeclList) {
                Element node = (Element) declNode.accept(this);
                eDeclList.appendChild(node);
            }
            el.appendChild(eDeclList);
        }

        if(p.procList != null && p.procList.size() > 0){
            Element eProcList = this.xmlDocument.createElement("ProcList");
            el.appendChild(eProcList);

            for(ProcNode proc: p.procList)
                eProcList.appendChild((Node) proc.accept(this));

        }
        return el;
    }

    public Element visit(VarDeclNode varDeclNode){
        Element node = this.xmlDocument.createElement(varDeclNode.name);
        node.appendChild((Element) varDeclNode.type.accept(this));

        if(varDeclNode.identifiers != null && varDeclNode.identifiers.size() > 0){
            for(IdInitNode idInitNode : varDeclNode.identifiers){
                node.appendChild((Element) idInitNode.accept(this));
            }
        }

        return node;
    }

    @Override
    public Node visit(ReadStatNode readStatNode) {
        Element node = this.xmlDocument.createElement(readStatNode.name);
        if(readStatNode.ilist != null && readStatNode.ilist.size() > 0){
            Element listNode = this.xmlDocument.createElement("IdList");
            node.appendChild(listNode);
            for(LeafID idnode: readStatNode.ilist)
                listNode.appendChild((Node) idnode.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(CallProcNode callProcNode) {
        Element node = this.xmlDocument.createElement(callProcNode.name);
        node.appendChild((Node) callProcNode.id.accept(this));
        if (callProcNode.exprList != null && callProcNode.exprList.size() > 0) {
            Element enode = this.xmlDocument.createElement("ParamsList");
            node.appendChild(enode);
            for (ExprNode expr : callProcNode.exprList) {
                enode.appendChild((Node) expr.accept(this));
            }
        }
        return node;
    }

    @Override
    public Node visit(ElifNode elifNode) {
        Element node = this.xmlDocument.createElement(elifNode.name);
        Element condNode = this.xmlDocument.createElement("Condition");
        node.appendChild(condNode);
        condNode.appendChild((Node) elifNode.condition.accept(this));
        if (elifNode.body != null && elifNode.body.getSize() > 0) {
            Element bodyNode = this.xmlDocument.createElement("Body");
            node.appendChild(bodyNode);
            bodyNode.appendChild((Node) elifNode.body.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(ExprNode exprNode) {
        Element node = this.xmlDocument.createElement(exprNode.name);
        if (exprNode.value1 != null && exprNode.value2 != null) {
            node.appendChild((Node) ((ExprNode)exprNode.value1).accept(this));
            node.appendChild((Node) ((ExprNode)exprNode.value2).accept(this));
        } else if (exprNode.value1 != null) {
            if (exprNode.value1 instanceof LeafNull)
                node.appendChild((Node) ((LeafNull)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafIntConst)
                node.appendChild((Node) ((LeafIntConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafFloatConst)
                node.appendChild((Node) ((LeafFloatConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafStringConst)
                node.appendChild((Node) ((LeafStringConst)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafBool)
                node.appendChild((Node) ((LeafBool)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof LeafID)
                node.appendChild((Node) ((LeafID)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof CallProcNode)
                node.appendChild((Node) ((CallProcNode)exprNode.value1).accept(this));
            if (exprNode.value1 instanceof ExprNode)
                node.appendChild((Node) ((ExprNode)exprNode.value1).accept(this));
        }
        return node;
    }

    @Override
    public Node visit(IdInitNode idInitNode) {
        Element node = this.xmlDocument.createElement(idInitNode.name);
        node.appendChild((Element) idInitNode.varName.accept(this));
        if (idInitNode.initValue != null)
            node.appendChild((Element) idInitNode.initValue.accept(this));
        return node;
    }

    @Override
    public Node visit(IfNode ifNode) {
        Element node = this.xmlDocument.createElement(ifNode.name); //IfOp
        Element condNode = this.xmlDocument.createElement("Condition");
        node.appendChild(condNode);
        condNode.appendChild((Node) ifNode.condition.accept(this));
        if (ifNode.ifBody != null && ifNode.ifBody.getSize() > 0) {
            Element bodyNode = this.xmlDocument.createElement("Body");
            node.appendChild(bodyNode);
            bodyNode.appendChild((Node) ifNode.ifBody.accept(this));
        }
        if (ifNode.elifList != null && ifNode.elifList.size() > 0) {
            Element elifListNode = this.xmlDocument.createElement("ElifList");
            node.appendChild(elifListNode);
            for (ElifNode enode : ifNode.elifList)
                elifListNode.appendChild((Node) enode.accept(this));
        }
        if (ifNode.elseBody != null && ifNode.elseBody.getSize() > 0) {
            Element elseNode = this.xmlDocument.createElement("ElseBody");
            node.appendChild(elseNode);
            elseNode.appendChild((Node) ifNode.elseBody.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(ParDeclNode parDeclNode) {
        Element node = this.xmlDocument.createElement(parDeclNode.name);
        node.appendChild((Node) parDeclNode.type.accept(this));
        if (parDeclNode.identifiers != null && parDeclNode.identifiers.size() > 0) {
            Element identilist = this.xmlDocument.createElement("Identifiers");
            node.appendChild(identilist);
            for (LeafID lid : parDeclNode.identifiers) {
                identilist.appendChild((Node) lid.accept(this));
            }
        }
        return node;
    }

    @Override
    public Node visit(ProcBodyNode procBodyNode) {
        Element node = this.xmlDocument.createElement(procBodyNode.name);
        if (procBodyNode.varDeclList != null && procBodyNode.varDeclList.size() > 0) {
            Element varDeclNode = this.xmlDocument.createElement("VarDeclList");
            node.appendChild(varDeclNode);
            for (VarDeclNode vdl : procBodyNode.varDeclList)
                varDeclNode.appendChild((Node) vdl.accept(this));
        }
        if (procBodyNode.statListNode != null && procBodyNode.statListNode.getSize() > 0) {
            node.appendChild((Node) procBodyNode.statListNode.accept(this));
        }
        if (procBodyNode.returnsExpr != null && procBodyNode.returnsExpr.size() > 0) {
            Element returnExprsNode = this.xmlDocument.createElement("returnsExprList");
            node.appendChild(returnExprsNode);
            for (ExprNode enode : procBodyNode.returnsExpr)
                returnExprsNode.appendChild((Node) enode.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(ProcNode procNode) {
        Element node = this.xmlDocument.createElement(procNode.name);
        node.appendChild((Node) procNode.id.accept(this));
        if (procNode.paramDeclList != null && procNode.paramDeclList.size() > 0) {
            Element paramNode = this.xmlDocument.createElement("ParamsList");
            for (ParDeclNode pdn : procNode.paramDeclList)
                paramNode.appendChild((Node) pdn.accept(this));
            node.appendChild(paramNode);
        }
        if (procNode.resultTypeList != null && procNode.resultTypeList.size() > 0) {
            Element resultTypeNode = this.xmlDocument.createElement("ResultTypeList");
            for (ResultTypeNode rtn : procNode.resultTypeList)
                resultTypeNode.appendChild((Node) rtn.accept(this));
            node.appendChild(resultTypeNode);
        }
        node.appendChild((Node) procNode.procBody.accept(this));
        return node;
    }

    @Override
    public Node visit(ResultTypeNode resultTypeNode) {
        Element node = this.xmlDocument.createElement(resultTypeNode.name);
        if (resultTypeNode.isVoid) {
            Element voidNode = this.xmlDocument.createElement("Void");
            node.appendChild(voidNode);
        } else node.appendChild((Node) resultTypeNode.type.accept(this));
        return node;
    }

    @Override
    public Node visit(StatListNode statListNode) {
        Element node = this.xmlDocument.createElement(statListNode.name);
        for (StatNode snode : statListNode.slist) {
            if (snode instanceof IfNode) {
                IfNode ifNode = (IfNode) snode;
                node.appendChild((Node) ifNode.accept(this));
            } else if (snode instanceof WhileStatNode) {
                WhileStatNode wNode = (WhileStatNode) snode;
                node.appendChild((Node) wNode.accept(this));
            } else if (snode instanceof ReadStatNode) {
                ReadStatNode rNode = (ReadStatNode) snode;
                node.appendChild((Node) rNode.accept(this));
            } else if (snode instanceof WriteStatNode) {
                WriteStatNode wNode = (WriteStatNode) snode;
                node.appendChild((Node) wNode.accept(this));
            } else if (snode instanceof AssignStatNode) {
                AssignStatNode aNode = (AssignStatNode) snode;
                node.appendChild((Node) aNode.accept(this));
            } else if (snode instanceof CallProcNode) {
                CallProcNode cNode = (CallProcNode) snode;
                node.appendChild((Node) cNode.accept(this));
            }
        }
        return node;
    }

    @Override
    public Node visit(TypeNode typeNode) {
        Element tNode = this.xmlDocument.createElement(typeNode.name);
        tNode.setAttribute("type", typeNode.type);
        return tNode;
    }

    @Override
    public Node visit(WhileStatNode whileStatNode) {
        Element node = this.xmlDocument.createElement(whileStatNode.name);
        Element preStatsNode = this.xmlDocument.createElement("PreStats");
        Element afterStatsNode = this.xmlDocument.createElement("DoBodyStats");
        Element conditionNode = this.xmlDocument.createElement("Condition");
        // Pre stats
        if (whileStatNode.preCondList != null && whileStatNode.preCondList.getSize() > 0) {
            node.appendChild(preStatsNode);
            preStatsNode.appendChild((Node) whileStatNode.preCondList.accept(this));
        }
        // Expr
        node.appendChild(conditionNode);
        conditionNode.appendChild((Node) whileStatNode.condition.accept(this));
        // Do body stats
        if (whileStatNode.afterCondList != null && whileStatNode.afterCondList.getSize() > 0) {
            node.appendChild(afterStatsNode);
            afterStatsNode.appendChild((Node) whileStatNode.afterCondList.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(WriteStatNode writeStatNode) {
        Element node = this.xmlDocument.createElement(writeStatNode.name);
        if(writeStatNode.elist != null && writeStatNode.elist.size() > 0){
            Element listNode = this.xmlDocument.createElement("ExprList");
            node.appendChild(listNode);
            for(ExprNode enode: writeStatNode.elist)
                listNode.appendChild((Node) enode.accept(this));
        }
        return node;
    }

    @Override
    public Node visit(LeafBool leafBool) {
        Element node = this.xmlDocument.createElement(leafBool.name);
        node.setAttribute("value", String.valueOf(leafBool.value));
        return node;
    }

    @Override
    public Node visit(LeafFloatConst leafFloatConst) {
        Element node = this.xmlDocument.createElement(leafFloatConst.name);
        node.setAttribute("value", leafFloatConst.value.toString());
        return node;
    }

    @Override
    public Node visit(LeafID leafID) {
        Element node = this.xmlDocument.createElement(leafID.name);
        node.setAttribute("value", leafID.value);
        return node;
    }

    @Override
    public Node visit(LeafIntConst leafIntConst) {
        Element node = this.xmlDocument.createElement(leafIntConst.name);
        node.setAttribute("value", leafIntConst.value.toString());
        return node;
    }

    @Override
    public Node visit(LeafNull leafNull) {
        Element node = this.xmlDocument.createElement(leafNull.name);
        return node;
    }

    @Override
    public Node visit(LeafStringConst leafStringConst) {
        Element node = this.xmlDocument.createElement(leafStringConst.name);
        node.setAttribute("value", leafStringConst.value);
        return node;
    }

    @Override
    public Node visit(AssignStatNode assignStatNode) {
        Element node = this.xmlDocument.createElement(assignStatNode.name);
        Element idListNode = this.xmlDocument.createElement("IdList");
        Element valueListNode = this.xmlDocument.createElement("ValueList");
        if (assignStatNode.idList != null && assignStatNode.idList.size() > 0) {
            for (LeafID lid : assignStatNode.idList)
                idListNode.appendChild((Node) lid.accept(this));
            node.appendChild(idListNode);
        }
        if (assignStatNode.exprList != null && assignStatNode.exprList.size() > 0) {
            for (ExprNode enode : assignStatNode.exprList)
                valueListNode.appendChild((Node) enode.accept(this));
            node.appendChild(valueListNode);
        }
        return node;
    }

}
