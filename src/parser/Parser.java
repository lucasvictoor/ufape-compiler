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
            System.out.println("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
            //throw new SyntaxError("Expected " + tipo + " but found " + currToken.getTipo());
        }
        //advance();   
    }

    private void expectIn(List<Enum> tipos){
        if (!tipos.contains(currToken.getTipo())) {
            System.out.println("Erro de sintaxe: esperado " + tipos + " mas encontrado " + currToken.getTipo());
        }
    }

    public Programa parsePrograma() {
        // <programa> ::= <identificador> begin <bloco> end

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

        List<DeclaracaoVariavel> declaracaoVariavel = new ArrayList<>();
        List<DeclaracaoSubRotina> declaracaoSubRotina = new ArrayList<>();
        List<Comando> comando = new ArrayList<>();

        while (currToken.getTipo() == TokenType.TokenReserved.VAR) {
            declaracaoVariavel.addAll(parseEtapaDeclaracaoVariavel());
        }

        while (currToken.getTipo() == TokenType.TokenReserved.DEF) {
            declaracaoSubRotina.add(parseDeclaracaoSubRotina());            
        }

        while (currToken.getTipo() != TokenType.TokenReserved.END) {
            comando.add(parseComandos());            
        }

        return new Bloco(declaracaoVariavel, declaracaoSubRotina, comando);
    }

    private List<DeclaracaoVariavel> parseEtapaDeclaracaoVariavel(){
        //<etapa de declaração de variáveis> ::= var <lista de declarações de variáveis> ;

        List<DeclaracaoVariavel> variaveis = new ArrayList<>();
        expect(TokenType.TokenReserved.VAR);
        advance();

        do {
            variaveis.add(parseDeclaracaoVariavel());
            if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                advance();
            }
        } while (TokenType.TokenSymbol.PONTO_E_VIRGULA != currToken.getTipo());


        expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

        advance();
        return variaveis;
    }

    private DeclaracaoVariavel parseDeclaracaoVariavel(){
        //<declaração de variáveis> ::= <tipo> <identificador> {, <identificador> } {, <atribuição declaração de variáveis> } ; 

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

        if (currToken.getTipo() == TokenType.TokenReserved.WHILE) {
            return parseComandoEnquanto();
            //return new ComandoEnquanto(null, null);
        }

        if (currToken.getTipo() == TokenType.TokenReserved.PRINT) {
            return new ComandoLeitura(null);
        }

        if (currToken.getTipo() == TokenType.TokenReserved.CONTINUE) {
            return new ComandoContinue();
        }

        if (currToken.getTipo() == TokenType.TokenReserved.BREAK) {
            return new ComandoBreak();
        }

        if (currToken.getTipo() == TokenType.TokenReserved.PROCEDURE) {
            return new ComandoChamadaProcedure(null, null);
        }

        if (currToken.getTipo() == TokenType.TokenSimple.ID) {
            return parseComandoAtribuicao();
            //return new ComandoAtribuicao(null, null);
        }
        
        return new ComandoAtribuicao(null, null);
    }

    public ComandoEnquanto parseComandoEnquanto(){
        //<comando enquanto> ::= while <expressão> do <comando> {<comando desvio condicional> } end


        expect(TokenType.TokenReserved.WHILE);
        advance();

        expect(TokenType.TokenSymbol.ABRE_PARENTESE);
        advance();

        List<Expressao> expressao = new ArrayList<>();
        
        while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
            expressao.add(parseExpressao());
        }


        expect(TokenType.TokenSymbol.FECHA_PARENTESE);
        advance();

        expect(TokenType.TokenReserved.DO);
        advance();

        List<Comando> comando = new ArrayList<>();

        while (currToken.getTipo() != TokenType.TokenReserved.END) {
            comando.add(parseComandos());
            if (currToken.getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
                advance();
            }
        }


        expect(TokenType.TokenReserved.END);
        advance();

        return new ComandoEnquanto(expressao, comando);
    }

    public Expressao parseExpressao(){
        //<expressão>::= <expressão simples> [<operador relacional><expressão simples>];


        Expressao expressaoSimples = parseExpressaoSimples();
        System.out.println("Expressao simples: " + expressaoSimples + " " + currToken.getLinha());


        List<Enum> operadorRelacional = new ArrayList<>();
        operadorRelacional.add(TokenType.TokenOperator.MAIOR);
        operadorRelacional.add(TokenType.TokenOperator.MENOR);
        operadorRelacional.add(TokenType.TokenOperator.MAIORIGUAL);
        operadorRelacional.add(TokenType.TokenOperator.MENORIGUAL);
        operadorRelacional.add(TokenType.TokenOperator.IGUAL);
        operadorRelacional.add(TokenType.TokenOperator.DIFERENTE);

        if (operadorRelacional.contains(currToken.getTipo())) {
            String operador = currToken.getValor();
            advance();
            Expressao expressaoSimples2 = parseExpressaoSimples();
            return new ExpressaoCompleta(expressaoSimples, expressaoSimples2, operador);
        }

        advance();
        return new ExpressaoCompleta(expressaoSimples, null, null);
    }

    public Expressao parseExpressaoSimples(){
        // <expressão simples> ::= [ + | - ] <termo> {( + | - ) <termo> }
        //if (currToken.getTipo() == TokenType.TokenOperator.MAIS || currToken.getTipo() == TokenType.TokenOperator.MENOS) {
        //    advance();
        //}

        Expressao termo = parseTermo();
        return termo;
    }

    public Expressao parseTermo(){
        // <termo> ::= <fator> { ( * | / ) <fator> }
        Expressao fator = parseFator();


        return fator;
    }

    public Expressao parseFator(){

        // <fator> ::= (<variável> | <número> | <chamada de função> | (<expressão>) | true | false)
        List<Enum> tiposBooleanos = new ArrayList<>();
        tiposBooleanos.add(TokenType.TokenReserved.TRUE);
        tiposBooleanos.add(TokenType.TokenReserved.FALSE);

        if (currToken.getTipo() == TokenType.TokenSimple.ID) {
            return new ExpressaoFatorVariavel(currToken.getValor());
        }

        if (currToken.getTipo() == TokenType.TokenSimple.NUMERO) {
            return new ExpressaoFatorAtributo(Integer.parseInt(currToken.getValor()));
        }

        if (currToken.getTipo() == TokenType.TokenReserved.FUNCTION) {
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();

            List<Argumento> argumentos = new ArrayList<>();
            while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
                argumentos.add(new Argumento(parseExpressao()));
                if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                    advance();
                }
            }

            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            return new ExpressaoFatorChamadaFunction(currToken.getValor(), argumentos);
            
        }

        if (tiposBooleanos.contains(currToken.getTipo())) {
            return new ExpressaoFatorAtributo(Boolean.parseBoolean(currToken.getValor()));
        }

        if (currToken.getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
            advance();
            Expressao expressao = parseExpressao();
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            advance();
            return expressao;
        }

        System.out.println("NULL " + currToken.getTipo() + " " + currToken.getValor());
        return null;
    }

    public ComandoAtribuicao parseComandoAtribuicao(){
        // <comando atribuição> ::= <identificador> := <expressão>;

        expect(TokenType.TokenSimple.ID);
        String variavel = currToken.getValor();
        advance();

        expect(TokenType.TokenOperator.ATRIBUICAO);
        advance();

        List <Expressao> expressao = new ArrayList<>();
        while (currToken.getTipo() != TokenType.TokenSymbol.PONTO_E_VIRGULA) {
            expressao.add(parseExpressao());            
        }
        expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

        return new ComandoAtribuicao(variavel, expressao);
    }
}
