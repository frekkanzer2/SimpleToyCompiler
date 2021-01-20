/* JFlex example: part of Java language lexer specification */
import java_cup.runtime.*;

%%

//directive
%public
%class Lexer

%unicode
%line
%column

%cup

//user code
%{
    String lexem = "";
%}

//reg-exp
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
NoZeroNumber = [1-9]
ZeroNumber = [0-9]
ID = [a-zA-Z_][a-zA-Z0-9_]*
FLOAT_CONST = ({NoZeroNumber}{ZeroNumber}*|0)(\.({ZeroNumber}*{NoZeroNumber}+|0))
INT_CONST = ({NoZeroNumber}{ZeroNumber}*|0)


//state
%state STRING
%state COMMENT

%%

//recognition - action

/* keywords */
<YYINITIAL> ";" { return new Symbol(sym.SEMI); }
<YYINITIAL> "," { return new Symbol(sym.COMMA); }
<YYINITIAL> "int" { return new Symbol(sym.INT); }
<YYINITIAL> "string" { return new Symbol(sym.STRING); }
<YYINITIAL> "float" { return new Symbol(sym.FLOAT); }
<YYINITIAL> "bool" { return new Symbol(sym.BOOL); }
<YYINITIAL> "proc" { return new Symbol(sym.PROC); }
<YYINITIAL> "(" { return new Symbol(sym.LPAR); }
<YYINITIAL> ")" { return new Symbol(sym.RPAR); }
<YYINITIAL> ":" { return new Symbol(sym.COLON); }
<YYINITIAL> "corp" { return new Symbol(sym.CORP); }
<YYINITIAL> "void" { return new Symbol(sym.VOID); }
<YYINITIAL> "if" { return new Symbol(sym.IF); }
<YYINITIAL> "then" { return new Symbol(sym.THEN); }
<YYINITIAL> "elif" { return new Symbol(sym.ELIF); }
<YYINITIAL> "fi" { return new Symbol(sym.FI); }
<YYINITIAL> "else" { return new Symbol(sym.ELSE); }
<YYINITIAL> "while" { return new Symbol(sym.WHILE); }
<YYINITIAL> "do" { return new Symbol(sym.DO); }
<YYINITIAL> "od" { return new Symbol(sym.OD); }
<YYINITIAL> "readln" { return new Symbol(sym.READ); }
<YYINITIAL> "write" { return new Symbol(sym.WRITE); }
<YYINITIAL> "+" { return new Symbol(sym.PLUS); }
<YYINITIAL> "-" { return new Symbol(sym.MINUS); }
<YYINITIAL> "*" { return new Symbol(sym.TIMES); }
<YYINITIAL> "/" { return new Symbol(sym.DIV); }
<YYINITIAL> "=" { return new Symbol(sym.EQ); }
<YYINITIAL> "<>" { return new Symbol(sym.NE); }
<YYINITIAL> "<" { return new Symbol(sym.LT); }
<YYINITIAL> "<=" { return new Symbol(sym.LE); }
<YYINITIAL> ">" { return new Symbol(sym.GT); }
<YYINITIAL> ">=" { return new Symbol(sym.GE); }
<YYINITIAL> "&&" { return new Symbol(sym.AND); }
<YYINITIAL> "||" { return new Symbol(sym.OR); }
<YYINITIAL> "!" { return new Symbol(sym.NOT); }
<YYINITIAL> "null" { return new Symbol(sym.NULL); }
<YYINITIAL> "true" { return new Symbol(sym.TRUE); }
<YYINITIAL> "false" { return new Symbol(sym.FALSE); }
<YYINITIAL> ":=" { return new Symbol(sym.ASSIGN); }
<YYINITIAL> "->" { return new Symbol(sym.ARROW_RETURN); }

<YYINITIAL> {
    {WhiteSpace}       { /* do nothing */ }
    {ID}               {
                            return new Symbol(sym.ID, yytext());
                        }
    {FLOAT_CONST}      {
                            try {
                                return new Symbol(sym.FLOAT_CONST, Float.parseFloat(yytext()));
                                }
                            catch (Exception e) {
                                return new Symbol(sym.error, "FLOAT CONVERSION ERROR");
                            }
                        }
    {INT_CONST}        {
                           try {
                               return new Symbol(sym.INT_CONST, Integer.parseInt(yytext()));
                               }
                           catch (Exception e) {
                               return new Symbol(sym.error, "INTEGER CONVERSION ERROR");
                           }
                        }
    \"                 {
                            yybegin(STRING);
                        }
    "/*"               {
                            yybegin(COMMENT);
                        }
}

<STRING> {
     <<EOF>>           {
                            return new Symbol(sym.error, "STRING NOT CLOSED");
                        }
     "\""                {
                            yybegin(YYINITIAL);
                            String temp_str = lexem;
                            lexem = "";
                            return new Symbol(sym.STRING_CONST, temp_str);
                        }
     [^\"]             { lexem += "" + yytext(); }
}

<COMMENT> {
    <<EOF>>            {
                            return new Symbol(sym.error, "COMMENT NOT CLOSED");
                        }
    "*/"               {
                            yybegin(YYINITIAL);
                        }
    "*"                { }
    [^"*/"]            { }
}

[^]                   {
                            return new Symbol(sym.error, yytext());
                      }