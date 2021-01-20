import Support.Nodes.ProgramNode;
import VisitorPack.CodeGenerationVisitor;
import VisitorPack.SemanticVisitor;
import VisitorPack.SyntaxVisitor;
import java_cup.runtime.Symbol;

import java.io.File;
import java.io.FileReader;

public class Manager {

    public static void main(String[] args) throws Exception {

        Lexer myLexer = new Lexer(new FileReader(args[0]));
        parser myParser = new parser(myLexer);

        Symbol s = myParser.parse();
        ProgramNode pnode = (ProgramNode) s.value;

        System.out.println("Generating XML for syntax visit");

        SyntaxVisitor sv = new SyntaxVisitor();
        sv.appendRoot(sv.visit(pnode));
        sv.toXml();

        SemanticVisitor semanticVisitor = new SemanticVisitor();
        semanticVisitor.visit(pnode);

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();

        String progName = args[0].replace("testfiles/", "testfiles/csources/").replace(".toy.txt", ".c");


        File file = codeGenerationVisitor.conversion(progName, pnode);
        System.out.println(System.getProperty("os.name"));

        String compileFile =  args[0].replace("testfiles/", "").replace(".toy.txt", "");
        if(System.getProperty("os.name").equalsIgnoreCase("Linux")){

            String absolutePath =  file.getAbsolutePath().replace(progName , "testfiles/csources");
            Runtime.getRuntime().exec(" xterm -e ./compile.sh " + compileFile, null, new File(absolutePath));

            System.out.println("Linux " + compileFile+ ".out generated");
        } else if (System.getProperty("os.name").contains("Windows")){
            Runtime.getRuntime().exec("cmd.exe /c start generate " + compileFile, null, new File("testfiles\\csources\\"));
            System.out.println("Windows " + compileFile + ".exe generated");
        }

    }

}
