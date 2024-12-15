import java.util.List;

import lexer.*;
import parser.*;
import symbol.SymbolTable;
import ast.ASTNode;

public class App {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer();

        String sourceCode = """
                MODELO begin
                    var integer x_1 := 10;
                    var integer x_2 := 20;
                    var integer x_3, x_4;

                    def soma (x_1, x_2): integer do
                        var integer x := 10;
                        return x_1 + x_2;
                    end

                    while ((x_1 + x_2) < 20) do
                        x_1 := x_1 + 1;
                    end
                end
                """;
        List<Token> tokens = lexer.tokenize(sourceCode);

        for (Token token : tokens) {
            System.out.println(token);
        }

        // Exibe a tabela de símbolos
        System.out.println("\n");
        SymbolTable symbolTable = lexer.getSymbolTable();
        //symbolTable.printTable();

        Parser parser = new Parser(tokens);
        ASTNode programa = parser.parsePrograma();

        System.out.println(programa.toString());

    }
}
