package parser;

import java.util.ArrayList;
import java.util.List;

import ast.*;
import lexer.Token;
import lexer.TokenType;
import symbol.SymbolTable;
import symbol.SymbolTableEntry;

public class Parser {
    private List<Token> tokens;
    private Token currToken;
    private Token proxToken;
    private int posicao;
    private SymbolTable symbolTable;
    private String escopo_identificador;

    public Parser(List<Token> tokens, SymbolTable symbolTable) {
        this.tokens = tokens;
        this.currToken = tokens.get(0);
        this.posicao = 0;
        this.symbolTable = symbolTable;
    }

    private void advance() {
        if (posicao + 1 < tokens.size()) {
            posicao++;
            currToken = tokens.get(posicao);
        } else {
            // Evita ultrapassar o limite da lista de tokens
            currToken = new Token(TokenType.TokenReserved.EOF, "EOF", currToken.getLinha(), currToken.getColuna());
        }
    }
    

    private void expect(Enum tipo){
        if (currToken.getTipo() != tipo) {
            throw new SyntaxError("Erro de sintaxe: esperado " + tipo + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
        }
    }

    private void expectIn(List<Enum> tipos){
        if (!tipos.contains(currToken.getTipo())) {
            throw new SyntaxError("Erro de sintaxe: esperado " + tipos + " mas encontrado " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
        }
    }

    public SymbolTable getSymbolTable() {
        symbolTable.cleanTableByScopeNull();
        return symbolTable;
    }

    private void updateSymbolTableEntry(String nome, String tipo, String categoria, String escopo, Object valor, int linha, int coluna) {
        SymbolTableEntry table = symbolTable.getEntry(nome, linha, coluna);
        if (table != null) {
            symbolTable.updateEntry(table.getId(), nome, tipo, categoria, escopo, valor, linha, coluna);
        }
    }

    public Programa parsePrograma() {
        // <programa> ::= <identificador> begin <bloco> end

        escopo_identificador = "global";
        expect(TokenType.TokenSimple.ID);
        updateSymbolTableEntry(currToken.getValor(), null, "identificador_programa", escopo_identificador, null, currToken.getLinha(), currToken.getColuna());
        String nome = currToken.getValor();
        advance();

        expect(TokenType.TokenReserved.BEGIN);
        advance();
        Bloco bloco = parseBloco();
        expect(TokenType.TokenReserved.END);

        advance();
        expect(TokenType.TokenReserved.EOF);

        System.out.println("Análise sintática concluída com sucesso. Nenhum erro encontrado.");

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
        int linhaVariavel = currToken.getLinha();
        int colunaVariavel = currToken.getColuna();
        advance();

        Expressao inicializacao = null;
        if (currToken.getTipo() == TokenType.TokenOperator.ATRIBUICAO) {
            expect(TokenType.TokenOperator.ATRIBUICAO);
            advance();
            inicializacao = parseExpressao();
            advance();
        }

        updateSymbolTableEntry(nomeVariavel, tipo, "variavel_teste", escopo_identificador, inicializacao, linhaVariavel, colunaVariavel);

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
        int parenteses = 0;

        while (TokenType.TokenSymbol.DOIS_PONTOS != currToken.getTipo()) {
            if (TokenType.TokenSymbol.ABRE_PARENTESE == currToken.getTipo()) {
                parenteses++;
                advance();
                while (TokenType.TokenSymbol.FECHA_PARENTESE != currToken.getTipo()) {
                    if (currToken.getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
                        parenteses++;                        
                    }
                    expect(TokenType.TokenSimple.ID);
                    updateSymbolTableEntry(currToken.getValor(), currToken.getTipo().toString(), "variavel_outro", "local", null, currToken.getLinha(), currToken.getColuna());
                    parametros.add(currToken.getValor());
                    if (currToken.getTipo() == TokenType.TokenSymbol.FECHA_PARENTESE) {
                        parenteses--;                        
                    }
                    advance();
                    if (TokenType.TokenSymbol.VIRGULA == currToken.getTipo()) {
                        advance();
                        expect(TokenType.TokenSimple.ID);
                        updateSymbolTableEntry(currToken.getValor(), currToken.getTipo().toString(), "variavel_outro_teste", "local", null, currToken.getLinha(), currToken.getColuna());
                    }
                }
                expect(TokenType.TokenSymbol.FECHA_PARENTESE);
                advance();
                if (parenteses != 0) {
                    break;
                }
            }
        }
        expect(TokenType.TokenSymbol.DOIS_PONTOS);
        advance();

        expectIn(List.of(TokenType.TokenReserved.INTEGER, TokenType.TokenReserved.BOOLEAN, TokenType.TokenReserved.VOID));

        String categoria = currToken.getTipo() == TokenType.TokenReserved.VOID ? "procedimento" : "funcao";
        updateSymbolTableEntry(nome, currToken.getTipo().toString(), categoria, "global", parametros, currToken.getLinha(), currToken.getColuna());
        
        escopo_identificador = "local";
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

        return new DeclaracaoSubRotina(nome, bloco, parametros);
    }

    private Comando parseComandos(){
        //<comandos> ::= <comando>
        // <comando> ::= (<atribuição_chprocedimento>|<comando condicional> |<comando enquanto> |<comando leitura> |<comandos>)
        
        if (currToken.getTipo() == TokenType.TokenReserved.IF) {
            return parseComandoCondicional();
        }

        if (currToken.getTipo() == TokenType.TokenReserved.WHILE) {
            return parseComandoEnquanto();
        }

        if (currToken.getTipo() == TokenType.TokenReserved.PRINT) {
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();
            Expressao expressao = parseExpressao();
            advance();
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            advance();
            expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

            return new ComandoLeitura(expressao);
        }

        if (currToken.getTipo() == TokenType.TokenReserved.PROCEDURE) {
            //<argumento> ::= (<variável> | <número> | <chamada de função> | | true | false)
            List<Enum> tiposArgumentos = new ArrayList<>();
            tiposArgumentos.add(TokenType.TokenSimple.ID);
            tiposArgumentos.add(TokenType.TokenSimple.NUMERO);
            tiposArgumentos.add(TokenType.TokenReserved.FUNCTION);
            tiposArgumentos.add(TokenType.TokenReserved.TRUE);
            tiposArgumentos.add(TokenType.TokenReserved.FALSE);
            advance();
            expect(TokenType.TokenSimple.ID);
            String nome = currToken.getValor();
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();
            int parenteses = 1;
            List<Expressao> expressao = new ArrayList<>();
            while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
                if (currToken.getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
                    parenteses++;                    
                }
                expressao.add(parseExpressao());
                advance();
                if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                    advance();
                    expectIn(tiposArgumentos);
                }
                if (currToken.getTipo() == TokenType.TokenSymbol.FECHA_PARENTESE) {
                    parenteses--;                                        
                }
            }
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            if (parenteses != 0) {
                throw new SyntaxError(parenteses + " Erro de sintaxe: parenteses não fechados na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
            }
            
            advance();
            expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);
            return new ComandoChamadaProcedure(nome, expressao);
        }

        if (currToken.getTipo() == TokenType.TokenSimple.ID) {
            return parseComandoAtribuicao();
        }

        if (currToken.getTipo() == TokenType.TokenReserved.FUNCTION) {
            expect(TokenType.TokenReserved.PROCEDURE);            
        }

        if (currToken.getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
            return null;
        }
        
        throw new RuntimeException("Erro de sintaxe: comando inválido na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna() + ". Encontrado: " + currToken.getTipo());
        //return null;
    }

    public ComandoEnquanto parseComandoEnquanto(){
        //<comando enquanto> ::= while <expressão> do <comando> {<comando desvio condicional> } end
    
        expect(TokenType.TokenReserved.WHILE);
        advance();
    
        expect(TokenType.TokenSymbol.ABRE_PARENTESE);
        advance();
    
        List<Expressao> expressao = new ArrayList<>();
        
        int parenteses = 1;
        while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
                parenteses++;
            }
            expressao.add(parseExpressao());
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenSymbol.FECHA_PARENTESE) {
                parenteses--;                
            }
        }
    
        expect(TokenType.TokenSymbol.FECHA_PARENTESE);
        parenteses--;
        if (parenteses != 0) {
            throw new SyntaxError(parenteses + " Erro de sintaxe: parenteses não fechados na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
        }
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
            advance();
            return new ExpressaoCompleta(expressaoSimples, expressaoSimples2, operador);
        }
    
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

        return termo;
    }

    public Expressao parseTermo(){
        // <termo> ::= <fator> { ( * | / ) <fator> }
        Expressao fator = parseFator();
        
        List<TokenType.TokenOperator> operadoresEscala = new ArrayList<>();
        operadoresEscala.add(TokenType.TokenOperator.VEZES);
        operadoresEscala.add(TokenType.TokenOperator.DIVIDIDO);

        while (operadoresEscala.contains(tokens.get(tokens.indexOf(currToken) + 1).getTipo())) {
            advance();
            String operador = currToken.getValor();
            advance();
            fator = new ExpressaoCompleta(fator, parseExpressaoSimples(), currToken.getValor());
        }

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
            List<Enum> tiposArgumentos = new ArrayList<>();
            tiposArgumentos.add(TokenType.TokenSimple.ID);
            tiposArgumentos.add(TokenType.TokenSimple.NUMERO);
            tiposArgumentos.add(TokenType.TokenReserved.FUNCTION);
            tiposArgumentos.add(TokenType.TokenReserved.TRUE);
            tiposArgumentos.add(TokenType.TokenReserved.FALSE);
            advance();
            expect(TokenType.TokenSimple.ID);
            advance();
            expect(TokenType.TokenSymbol.ABRE_PARENTESE);
            advance();
            int parenteses = 1;

            List<Argumento> argumentos = new ArrayList<>();
            while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
                if (TokenType.TokenSymbol.ABRE_PARENTESE == currToken.getTipo()) {
                    parenteses++;                    
                }
                // <argumento> ::= (<variável> | <número> | <chamada de função> | | true | false)
                argumentos.add(new Argumento(parseExpressao()));
                advance();
                if (currToken.getTipo() == TokenType.TokenSymbol.VIRGULA) {
                    advance();
                    expectIn(tiposArgumentos);
                }
                if (TokenType.TokenSymbol.FECHA_PARENTESE == currToken.getTipo()) {
                    parenteses--;                                        
                }
            }

            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            if (parenteses != 0) {
                throw new SyntaxError(parenteses + " Erro de sintaxe: parenteses não fechados na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
            }
            return new ExpressaoFatorChamadaFunction(currToken.getValor(), argumentos);
            
        }

        if (tiposBooleanos.contains(currToken.getTipo())) {
            return new ExpressaoFatorAtributo(Boolean.parseBoolean(currToken.getValor()));
        }

        if (currToken.getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
            advance();
            Expressao expressao = parseExpressao();
            expect(TokenType.TokenSymbol.FECHA_PARENTESE);
            return expressao;
        }

        throw new RuntimeException("Esperado uma expressão. Encontrado: " + currToken.getTipo() + " na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
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
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
                advance();
                break;
            }
            if (currToken.getTipo() == TokenType.TokenReserved.TRUE || currToken.getTipo() == TokenType.TokenReserved.FALSE) {
                advance();                
            }
            if (currToken.getLinha() < tokens.get(tokens.indexOf(currToken) + 1).getLinha()) {
                break;                
            }
        }
        expect(TokenType.TokenSymbol.PONTO_E_VIRGULA);

        return new ComandoAtribuicao(variavel, expressao);
    }

    private Comando parseComandoCondicional() {
        // <comando condicional> ::= if <expressão> do <comando> [else <comando>] end
    
        expect(TokenType.TokenReserved.IF);
        advance();
    
        expect(TokenType.TokenSymbol.ABRE_PARENTESE);
        advance();
    
        List<Expressao> condicao = new ArrayList<>();
        int parenteses = 1;
        while (currToken.getTipo() != TokenType.TokenSymbol.FECHA_PARENTESE) {
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenSymbol.ABRE_PARENTESE) {
                parenteses++;
            }
            condicao.add(parseExpressao());
            if (tokens.get(tokens.indexOf(currToken) + 1).getTipo() == TokenType.TokenSymbol.FECHA_PARENTESE) {
                parenteses--;                
            }
        }
    
        expect(TokenType.TokenSymbol.FECHA_PARENTESE);
        parenteses--;
        if (parenteses != 0) {
            throw new SyntaxError(parenteses + " Erro de sintaxe: parenteses não fechados na linha " + currToken.getLinha() + " e coluna " + currToken.getColuna());
        }
        advance();
    
        expect(TokenType.TokenReserved.DO);
        advance();
    
        List<Comando> comandosIf = new ArrayList<>();
        while (currToken.getTipo() != TokenType.TokenReserved.END &&
               currToken.getTipo() != TokenType.TokenReserved.ELSE &&
               currToken.getTipo() != TokenType.TokenReserved.EOF) {
            comandosIf.add(parseComandos());
    
            if (currToken.getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
                advance();
            }
        }
    
        List<Comando> comandosElse = new ArrayList<>();
        if (currToken.getTipo() == TokenType.TokenReserved.ELSE) {
            advance();
    
            while (currToken.getTipo() != TokenType.TokenReserved.END &&
                   currToken.getTipo() != TokenType.TokenReserved.EOF) {
                comandosElse.add(parseComandos());
    
                if (currToken.getTipo() == TokenType.TokenSymbol.PONTO_E_VIRGULA) {
                    advance();
                }
            }
        }
    
        expect(TokenType.TokenReserved.END);
    
        if (posicao + 1 < tokens.size()) {
            advance();
        } else {
            throw new SyntaxError("Erro: esperado END, mas alcançado o fim do arquivo.");
        }
    
        return new ComandoCondicional(condicao, comandosIf, comandosElse);
    }      
    
}