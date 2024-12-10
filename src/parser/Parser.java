package parser;

import java.util.List;

import lexer.Token;
import lexer.TokenType;

public class Parser {
    private List<Token> tokens;
    private Token currToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currToken = tokens.get(0);
    }

    private void advance(){
        currToken = tokens.get(tokens.indexOf(currToken) + 1);
    }

    private void expect(Enum tipo){
        if (currToken.getTipo() != tipo) {
            System.out.println("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo());
            //throw new SyntaxError("Expected " + tipo + " but found " + currToken.getTipo());
        }
        //advance();   
    }

    public Programa parsePrograma() {
        System.out.println("Token Atual, primeiro token: " + currToken);
        expect(TokenType.TokenSimple.ID);
        String nome = currToken.getValor();
        advance();

        expect(TokenType.TokenReserved.BEGIN);
        Bloco bloco = parseBloco();
        //expect(TokenType.TokenReserved.END);

        return new Programa(nome, bloco);
    }

    private Bloco parseBloco(){

        return new Bloco(null, null, null);
    }
}
