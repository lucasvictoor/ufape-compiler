import java.util.List;

import lexer.Lexer;
import lexer.Token;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Lexer lexer = new Lexer();
        String sourceCode = """
                MODELO begin
                    var integer x_1 = 10;
                    def soma: void do 
                        x := x + x; 
                    end
                    procedure#soma(); 
                end
                """;
        List<Token> tokens = lexer.tokenize(sourceCode);

        for (Token token : tokens) {
            System.out.println(token);
        }

        // Exibe a tabela de símbolos
        System.out.println(lexer.getSymbolTable());
    }
}
