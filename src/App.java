import java.util.List;

import lexer.*;
import parser.*;
import semantic.SemanticAnalyzer;
import symbol.SymbolTable;
import ast.Programa;

public class App {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer();

        String sourceCode = """
                MODELO begin
                    var integer x_1 := 10;
                    var integer x_2 := 20;
                    var integer x_3, x_4;
                    var boolean x_5 := true;

                    def soma (x_1, x_2): integer do
                        var integer x;
                        x := x_1 + x_2;
                        return x;
                    end

                    def menos (x_1, x_2): void do
                        var integer x;
                        x := x_1 - x_2;
                        x_5 := false;
                        print(x);
                    end

                    while ((x_1 + x_2) < 20) do
                        x_1 := x_1 + 1 - x_2;
                        x_4 := x_2 * 2 / 3;

                        break;
                    end
                    
                    if ((x_1 + x_2) < 20) do    // Teste linha comentário
                        x_1 := x_1 + 1;
                    else
                        print(x_1);
                    end

                    /* Comentário
                    de bloco */
                    
                    x_2 := x_2 * 2 / 3000 + 1 - 2 * 3;
                    procedure#menos(x_1, x_2);
                    x_3 := function#soma(1, 2);
                end
                """;
        
        String sourceCode2 = """
                MODELO begin
                    var integer x_1 := 10;
                    var integer x_2 := 20;
                    var integer x_4;

                    while (x_1 < 20) do
                        if(x_1 < 15) do
                            x_1 := x_1 + 1;
                        else
                            x_1 := x_1 + 2;
                        end
                        x_4 := x_2 * 2 / 3;
                    end
                end
                """;
            
        List<Token> tokens = lexer.tokenize(sourceCode);

        /* for (Token token : tokens) {
            System.out.println(token);
        } */

        // Exibe a tabela de símbolos
        System.out.println("\n");
        SymbolTable symbolTable = lexer.getSymbolTable();
        //symbolTable.printTable();

        Parser parser = new Parser(tokens, symbolTable);
        Programa programa = parser.parsePrograma();
        symbolTable = parser.getSymbolTable();

        //symbolTable.printTable();

        //System.out.println(programa.toString());

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(symbolTable);
        semanticAnalyzer.analyze(programa);

    }
}
