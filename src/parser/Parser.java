package parser;

import java.util.ArrayList;
import java.util.List;

import ast.*;
import lexer.Token;
import lexer.TokenType;
import lexer.TokenType.TokenOperator;

public class Parser {
    private List<Token> tokens;
    private Token currToken;
    private Token proxToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currToken = tokens.get(0);
    }

    private void advance(){
        currToken = tokens.get(tokens.indexOf(currToken) + 1);
    }

    private void expect(Enum tipo){
        if (currToken.getTipo() != tipo) {
            //System.out.println("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
            throw new SyntaxError("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
        }
        //advance();   
    }

    private void expectIn(List<Enum> tipos){
        if (!tipos.contains(currToken.getTipo())) {
            //System.out.println("Erro de sintaxe: esperado " + tipos + " mas encontrado " + currToken.getTipo());
            throw new SyntaxError("Erro de sintaxe: esperado " + tipos + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
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
        Token proxToken = tokens.get(tokens.indexOf(currToken) + 1);

        while (currToken.getTipo() == TokenType.TokenReserved.VAR) {
            declaracaoVariavel.addAll(parseEtapaDeclaracaoVariavel());
        }

        while (currToken.getTipo() == TokenType.TokenReserved.DEF) {
            declaracaoSubRotina.add(parseDeclaracaoSubRotina());
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenReserved.DEF) {
                advance();                
            }
        }

        while (currToken.getTipo() != TokenType.TokenReserved.END) {
            if (currToken.getTipo() == TokenType.TokenReserved.RETURN) {
                break;
            }
            comando.add(parseComandos());
            if (currToken.getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
                advance();
            }
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
            //if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
            //    System.out.println("VIRGULA");
            //    advance();
            //}
        } while (TokenType.TokenSymbol.PONTO_E_VIRGULA != currToken.getTipo() && TokenType.TokenReserved.VAR != currToken.getTipo());

        expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

        advance();
        return variaveis;
    }

    private DeclaracaoVariavel parseDeclaracaoVariavel(){
        //<declaração de variáveis> ::= <tipo> <identificador> {, <identificador> } {, <atribuição declaração de variáveis> } ; 

        List<Enum> tipos = new ArrayList<>();
        tipos.add(TokenType.TokenReserved.INTEGER);
        tipos.add(TokenType.TokenReserved.BOOLEAN);
        String tipo = "";

        if (currToken.getTipo() != TokenType.TokenSymbol.VIRGULA) {
            expectIn(tipos);
            tipo = currToken.getValor();
        }

        advance();
        expect(TokenType.TokenSimple.ID);
        String nomeVariavel = currToken.getValor();
        advance();

        Expressao inicializacao = null;
        if (currToken.getTipo() == TokenType.TokenOperator.ATRIBUICAO) {
            advance();
            inicializacao = parseExpressao();
            advance();
        }

        return new DeclaracaoVariavel(tipo, nomeVariavel, inicializacao);
    }

    private DeclaracaoSubRotina parseDeclaracaoSubRotina(){
        //<etapa de declaração de sub-rotinas> ::= ::= (<declaração de procedimento>; | <declaração de função>;)
        expect(TokenType.TokenReserved.DEF);
        advance();

        expect(TokenType.TokenSimple.ID);
        String nome = currToken.getValor();
        advance();

        List<String> parametros = new ArrayList<>();
        Bloco bloco = null;

        String tipoRetorno = "";
        Expressao retorno = null;

        while (TokenType.TokenSymbol.DOIS_PONTOS != currToken.getTipo()) {
            if (TokenType.TokenSymbol.ABRE_PARENTESE == currToken.getTipo()) {
                advance();
                while (TokenType.TokenSymbol.FECHA_PARENTESE != currToken.getTipo()) {
                    expect(TokenType.TokenSimple.ID);
                    parametros.add(currToken.getValor());
                    advance();
                    if (TokenType.TokenSymbol.VIRGULA == currToken.getTipo()) {
                        advance();
                    }
                }
                expect(TokenType.TokenSymbol.FECHA_PARENTESE);
                advance();
            }
        }
        expect(TokenType.TokenSymbol.DOIS_PONTOS);
        advance();

        if (currToken.getTipo() == TokenType.TokenReserved.INTEGER || currToken.getTipo() == TokenType.TokenReserved.BOOLEAN) {
            tipoRetorno = currToken.getValor();
            advance();
            expect(TokenType.TokenReserved.DO);
            advance();
            bloco = parseBloco();
            expect(TokenType.TokenReserved.RETURN);
            advance();
            retorno = parseExpressao();
            advance();
            expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);
            advance();
            expect(TokenType.TokenReserved.END);
            return new DeclaracaoFunction(nome, bloco, parametros, tipoRetorno, retorno);       
        }

        if (currToken.getTipo() == TokenType.TokenReserved.VOID) {
            advance();
            expect(TokenType.TokenReserved.DO);
            advance();
            bloco = parseBloco();
            expect(TokenType.TokenReserved.END);
            advance();
            return new DeclaracaoProcedure(nome, bloco, parametros);
        }

        return new DeclaracaoSubRotina(nome, null, parametros);
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
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();
            Expressao expressao = parseExpressao();
            advance();
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            advance();

            return new ComandoLeitura(expressao);
        }

        if (currToken.getTipo() == TokenType.TokenReserved.PROCEDURE) {
            advance();
            expect(TokenType.TokenSimple.ID);
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();
            List<Expressao> expressao = new ArrayList<>();
            while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
                expressao.add(parseExpressao());
                advance();
                if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                    advance();
                }
            }
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            advance();
            return new ComandoChamadaProcedure(null, null);
        }

        if (currToken.getTipo() == TokenType.TokenSimple.ID) {
            return parseComandoAtribuicao();
            //return new ComandoAtribuicao(null, null);
        }
        
        return null;
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
            if (currToken.getTipo() == TokenType.TokenReserved.CONTINUE) {
                advance();
                Comando comandoContinue = new ComandoContinue();
                comando.add(comandoContinue);
            }
    
            if (currToken.getTipo() == TokenType.TokenReserved.BREAK) {
                advance();
                Comando comandoBreak = new ComandoBreak();
                comando.add(comandoBreak);
            }
        }


        expect(TokenType.TokenReserved.END);
        advance();

        return new ComandoEnquanto(expressao, comando);
    }

    public Expressao parseExpressao(){
        //<expressão>::= <expressão simples> [<operador relacional><expressão simples>];

        Expressao expressaoSimples = parseExpressaoSimples();

        List<Enum> operadorRelacional = new ArrayList<>();
        operadorRelacional.add(TokenType.TokenOperator.MAIOR);
        operadorRelacional.add(TokenType.TokenOperator.MENOR);
        operadorRelacional.add(TokenType.TokenOperator.MAIORIGUAL);
        operadorRelacional.add(TokenType.TokenOperator.MENORIGUAL);
        operadorRelacional.add(TokenType.TokenOperator.IGUAL);
        operadorRelacional.add(TokenType.TokenOperator.DIFERENTE);

        Token proxToken = tokens.get(tokens.indexOf(currToken) + 1);

        if (operadorRelacional.contains(proxToken.getTipo())) {
            advance();           
        }

        if (operadorRelacional.contains(currToken.getTipo())) {
            String operador = currToken.getValor();
            advance();
            Expressao expressaoSimples2 = parseExpressaoSimples();
            return new ExpressaoCompleta(expressaoSimples, expressaoSimples2, operador);
        }

        //advance();
        return expressaoSimples;
    }

    public Expressao parseExpressaoSimples(){
        // <expressão simples> ::= [ + | - ] <termo> {( + | - ) <termo> }

        List<Enum> operadorDiferenca = new ArrayList<>();
        operadorDiferenca.add(TokenType.TokenOperator.MAIS);
        operadorDiferenca.add(TokenType.TokenOperator.MENOS);

        boolean temSinal = operadorDiferenca.contains(currToken.getTipo());
        Token sinal = temSinal ? currToken : null;

        Token proxToken = tokens.get(tokens.indexOf(currToken) + 1);

        if (temSinal) {
            advance();
        }
        
        Expressao termo = parseTermo();

        if (sinal != null) {
            termo = new ExpressaoCompleta(termo, termo, sinal.getValor());
            advance();
        }

        if (operadorDiferenca.contains(proxToken.getTipo())) {
            advance();
            termo = new ExpressaoCompleta(termo, parseExpressaoSimples(), currToken.getValor());
        }

        //Expressao termo = parseTermo();
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
            advance();
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            return expressao;
        }

        throw new RuntimeException("Expect expression.");
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