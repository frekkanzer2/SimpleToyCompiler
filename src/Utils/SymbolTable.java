package Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable extends HashMap<String, SymbolTable.SymbolTableEntry> {

    // Class stats

    public SymbolTable pointerToFather;
    public String symbolTableName;

    public void setFatherSymTab(SymbolTable st){
        this.pointerToFather = st;
    }

    public boolean hasFatherSymTab(){
        if (pointerToFather != null) return true; else return false;
    }

    public SymbolTable getFatherSymTab(){
        return pointerToFather;
    }

    public void createEntry_variable(String id, String type) throws Exception {
        if (super.containsKey(id)) throw new Exception("Semantic error in " + symbolTableName + ": identifier '" + id + "' already declared in the actual scope");
        else super.put(id, new SymbolTableEntry(id, StringToType(type)));
    }

    public void createEntry_function(String id, ArrayList<ValueType> inputParams, ArrayList<ValueType> outputParams) throws Exception {
        if (super.containsKey(id)) throw new Exception("Semantic error in " + symbolTableName + ": identifier '" + id + "' already declared in the actual scope");
        if (outputParams.size() > 1 && outputParams.contains(ValueType.Void)) {
            System.err.println("Semantic error: void keyword is now allowed when multiple return types are declared");
            System.exit(0);
        }
        super.put(id, new SymbolTableEntry(id, inputParams, outputParams));
    }

    public SymbolTableEntry containsEntry(String id) throws Exception {
        SymbolTableEntry _ste = null;
        if (super.containsKey(id)) {
            _ste = super.get(id);
        } else if (hasFatherSymTab()) {
            _ste = getFatherSymTab().containsEntry(id);
        } else {
            System.err.println("Semantic error: variable " + id + " not declared");
            System.exit(0);
        }
        return _ste;
    }

    public Boolean containsKey(String id) throws Exception {
        if (super.containsKey(id)) {
            return true;
        } else if (hasFatherSymTab()) {
            return getFatherSymTab().containsKey(id);
        } else {
            System.err.println("Semantic error: variable " + id + " not declared");
            System.exit(0);
        }
        return null;
    }

    public ArrayList<SymbolTableEntry> getFunctions() {
        ArrayList<SymbolTableEntry> listOfEntries = new ArrayList<>();
        for (SymbolTableEntry ste: super.values())
            if (!ste.isVariable() && !ste.id.equalsIgnoreCase("main"))
                listOfEntries.add(ste);
        if (listOfEntries.size() == 0) return null; else return listOfEntries;
    }

    @Override
    public String toString() {
        String str = symbolTableName + "\n";
        for (SymbolTableEntry ste : super.values())
            str += (ste.toString() + "\n");
        return str;
    }

    // End of class stats

    /*
    *
    *   Static enumerators
    *
    * */

    public static enum Type {
        Variable,
        Function,
    }

    public static enum ValueType {
        Integer,
        String,
        Float,
        Boolean,
        Null,
        Void,
    }

    // End of static enumerators section

    /*
     *
     *   Static functions
     *
     * */

    public static ValueType StringToType(String type) throws Exception {
        if (type.equalsIgnoreCase("int")) return ValueType.Integer;
        if (type.equalsIgnoreCase("string")) return ValueType.String;
        if (type.equalsIgnoreCase("float")) return ValueType.Float;
        if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) return ValueType.Boolean;
        if (type.equalsIgnoreCase("null")) return ValueType.Null;
        if (type.equalsIgnoreCase("void")) return ValueType.Void;
        throw new Exception("Semantic error: type " + type + " does not exists");
    }

    // End of static functions section

    /*
    *
    *   SymbolTableEntry : class for sym tab entries
    *
    * */

    public class SymbolTableEntry {

        public String id;
        public ValueType vtype;
        public Type type;
        public ArrayList<ValueType> inputParams, outputParams;

        public SymbolTableEntry(String id, ValueType vtype) {
            this.type = Type.Variable;
            this.id = id;
            this.vtype = vtype;
        }

        public SymbolTableEntry(String id, ArrayList<ValueType> inputParams, ArrayList<ValueType> outputParams) {
            this.type = Type.Function;
            this.id = id;
            this.inputParams = inputParams;
            this.outputParams = outputParams;
        }

        public boolean isVariable() {
            if (this.type == Type.Variable) return true; else return false;
        }

        @Override
        public String toString() {
            if (isVariable())
                return "Entry of type Variable :: " + id + " | " + vtype;
            else  {
                String inputs = "";
                for (int i = 0; i < inputParams.size(); i++)
                    if (i == inputParams.size()-1)
                        inputs += (inputParams.get(i));
                    else inputs += (inputParams.get(i) + ", ");
                String outputs = "";
                for (int i = 0; i < outputParams.size(); i++) {
                    if (i == outputParams.size() - 1)
                        outputs += (outputParams.get(i));
                    else outputs += (outputParams.get(i) + ", ");
                }
                return "Entry of type Function :: " + id + "(" + inputs + ") -> " + outputs;
            }
        }
    }

}
