package parser;

import java.util.ArrayList;
import java.util.List;

import lexer.Token;
import lexer.TokenType;
import lexer.TokenType.TokenOperator;

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
            //System.out.println("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo());
            //throw new SyntaxError("Expected " + tipo + " but found " + currToken.getTipo());
        }
        //advance();   
    }

    private void expectIn(List<Enum> tipos){
        if (!tipos.contains(currToken.getTipo())) {
            //System.out.println("Erro de sintaxe: esperado " + tipos + " mas encontrado " + currToken.getTipo());
        }
    }

    public Programa parsePrograma() {
        // <programa> ::= <identificador> begin <bloco> end
        //System.out.println("Token Atual, primeiro token: " + currToken);
        expect(TokenType.TokenSimple.ID);
        String nome = currToken.getValor();
        advance();

        expect(TokenType.TokenReserved.BEGIN);
        advance();
        Bloco bloco = parseBloco();
        expect(TokenType.TokenReserved.END);

        return new Programa(nome, bloco);
    }

    private Bloco parseBloco(){
        //<bloco> ::= [<etapa de declaração de variáveis>][<etapa de declaração de sub-rotinas>]<comandos>
        //System.out.println("Token Atual, parse Bloco: " + currToken);
        List<DeclaracaoVariavel> declaracaoVariavel = new ArrayList<>();
        List<DeclaracaoSubRotina> declaracaoSubRotina = new ArrayList<>();
        List<Comando> comando = new ArrayList<>();

        if (currToken.getTipo() == TokenType.TokenReserved.VAR) {
            declaracaoVariavel = parseEtapaDeclaracaoVariavel();
        }

        while (currToken.getTipo() == TokenType.TokenReserved.DEF) {
            declaracaoSubRotina.add(parseDeclaracaoSubRotina());            
        }

        //while (currToken.getTipo() != TokenType.TokenReserved.END) {
            //comando.add(parseComandos());            
        //}

        return new Bloco(declaracaoVariavel, declaracaoSubRotina, comando);
    }

    private List<DeclaracaoVariavel> parseEtapaDeclaracaoVariavel(){
        //<etapa de declaração de variáveis> ::= var <lista de declarações de variáveis> ;
        //System.out.println("Token Atual, parse Etapa Declaracao Variavel: " + currToken);
        List<DeclaracaoVariavel> variaveis = new ArrayList<>();
        expect(TokenType.TokenReserved.VAR);
        advance();

        do {
            //System.out.println("Token Atual, parse Etapa Declaracao Variavel DOWHILE: " + currToken);
            variaveis.add(parseDeclaracaoVariavel());
            if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                advance();
            }
        } while (TokenType.TokenSymbol.PONTO_E_VIRGULA != currToken.getTipo());

        //System.out.println("Token Atual, parse Etapa Declaracao Variavel DOWHILE FIM: " + currToken);
        expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

        advance();
        return variaveis;
    }

    private DeclaracaoVariavel parseDeclaracaoVariavel(){
        //<declaração de variáveis> ::= <tipo> <identificador> {, <identificador> } {, <atribuição declaração de variáveis> } ; 
        //System.out.println("Token Atual, parse Declaracao Variavel: " + currToken);
        List<Enum> tipos = new ArrayList<>();
        tipos.add(TokenType.TokenReserved.INTEGER);
        tipos.add(TokenType.TokenReserved.BOOLEAN);
        expectIn(tipos);

        String tipo = currToken.getValor();
        advance();

        expect(TokenType.TokenSimple.ID);
        String nomeVariavel = currToken.getValor();
        advance();

        Expressao inicializacao = null;
        if (currToken.getTipo() == TokenType.TokenOperator.ATRIBUICAO) {
            advance();
            inicializacao = parseExpressao();
        }

        //System.out.println("Token Atual, parse Declaracao Variavel FIM: " + currToken);
        return new DeclaracaoVariavel(tipo, nomeVariavel, inicializacao);
    }

    private DeclaracaoSubRotina parseDeclaracaoSubRotina(){
        //<etapa de declaração de sub-rotinas> ::= ::= (<declaração de procedimento>; | <declaração de função>;)
        return new DeclaracaoSubRotina(null, null, null);
    }

    private Comando parseComandos(){
        //<comandos> ::= <comando>
        // <comando> ::= (<atribuição_chprocedimento>|<comando condicional> |<comando enquanto> |<comando leitura> |<comandos>)
        
        if (currToken.getTipo() == TokenType.TokenReserved.IF) {
            return new ComandoCondicional(null, null, null);
        }

        if (currToken.getTipo() == TokenType.TokenOperator.ATRIBUICAO) {
            return new ComandoAtribuicao(null, null);
        }
        
        return null;
    }

    public Expressao parseExpressao(){
        advance();
        return null;
    }
}
