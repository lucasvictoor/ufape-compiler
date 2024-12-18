package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import symbol.SymbolTable;
import symbol.SymbolEntry;

public class Lexer {

    private String codigo;
    private int linha;
    private int coluna;
    private int posicao;
    private SymbolTable symbolTable;
    private String escopoAtual = "global";

    public Lexer() {
        this.linha = 1;
        this.coluna = 1;
        this.posicao = 0;
        this.symbolTable = new SymbolTable();
    }

    private void atualizarEscopo(String novoEscopo) {
        escopoAtual = novoEscopo;
    }

    // Método para a tabela de símbolos
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    // Método que recebe um código fonte e retorna uma lista de tokens
    public List<Token> tokenize(String codigo) {
        this.codigo = codigo;
        List<Token> tokens = new ArrayList<>();

        while (posicao < codigo.length()) {
            char charAtual = codigo.charAt(posicao);

            if (Character.isWhitespace(charAtual)) {
                skipWhitespace();
            // Tratamento de comentários de linha: "//"
            } else if (charAtual == '/' && (posicao + 1 < codigo.length() && codigo.charAt(posicao + 1) == '/')) {
                skipComentarioLinha();
            // Tratamento de comentários de bloco: "/* ... */"
            } else if (charAtual == '/' && (posicao + 1 < codigo.length() && codigo.charAt(posicao + 1) == '*')) {
                skipComentarioBloco();
            } else if (isMatch("[a-zA-Z_]", charAtual)) {
                // System.out.println("Letra charAtual: " + charAtual);
                Token token = lexemaIdentificador();
                if (token.getValor().equals("while") || token.getValor().equals("if") || token.getValor().equals("def")) {
                    atualizarEscopo(token.getValor());
                }
                tokens.add(token);
            } else if (isMatch("\\d", charAtual)) {
                tokens.add(lexemaNumerico());
            } else {
                // System.out.println("Simbolo charAtual: " + charAtual);
                tokens.add(lexemaSimbolo());
            }
        }

        return tokens;
    }

    private Token lexemaSimbolo() {
        int inicioLinha = linha;
        int inicioColuna = coluna;
        char charAtual = codigo.charAt(posicao);
        posicao++;
        coluna++;

        switch (charAtual) {
            case '+':
                return new Token(TokenType.TokenOperator.MAIS, "+", inicioLinha, inicioColuna);
            case '-':
                return new Token(TokenType.TokenOperator.MENOS, "-", inicioLinha, inicioColuna);
            case '*':
                return new Token(TokenType.TokenOperator.VEZES, "*", inicioLinha, inicioColuna);
            case '/':
                return new Token(TokenType.TokenOperator.DIVIDIDO, "/", inicioLinha, inicioColuna);
            case '=':
                return new Token(TokenType.TokenOperator.IGUAL, "=", inicioLinha, inicioColuna);
            case '<':
                if (posicao < codigo.length() && codigo.charAt(posicao) == '=') {
                    posicao++;
                    coluna++;
                    return new Token(TokenType.TokenOperator.MENORIGUAL, "<=", inicioLinha, inicioColuna);
                } else {
                    return new Token(TokenType.TokenOperator.MENOR, "<", inicioLinha, inicioColuna);
                }
            case '>':
                if (posicao < codigo.length() && codigo.charAt(posicao) == '=') {
                    posicao++;
                    coluna++;
                    return new Token(TokenType.TokenOperator.MAIORIGUAL, ">=", inicioLinha, inicioColuna);
                } else {
                    return new Token(TokenType.TokenOperator.MAIOR, ">", inicioLinha, inicioColuna);
                }
            case '!':
                if (posicao < codigo.length() && codigo.charAt(posicao) == '=') {
                    posicao++;
                    coluna++;
                    return new Token(TokenType.TokenOperator.DIFERENTE, "!=", inicioLinha, inicioColuna);
                } else {
                    throw new RuntimeException("Caractere inválido: " + charAtual);
                }
            case ':':
                if (posicao < codigo.length() && codigo.charAt(posicao) == '=') {
                    posicao++;
                    coluna++;
                    return new Token(TokenType.TokenOperator.ATRIBUICAO, ":=", inicioLinha, inicioColuna);
                } else {
                    return new Token(TokenType.TokenSymbol.DOIS_PONTOS, ":", inicioLinha, inicioColuna);
                }
            case ';':
                return new Token(TokenType.TokenSymbol.PONTO_E_VIRGULA, ";", inicioLinha, inicioColuna);
            case ',':
                return new Token(TokenType.TokenSymbol.VIRGULA, ",", inicioLinha, inicioColuna);
            case '(':
                return new Token(TokenType.TokenSymbol.ABRE_PARENTESE, "(", inicioLinha, inicioColuna);
            case ')':
                return new Token(TokenType.TokenSymbol.FECHA_PARENTESE, ")", inicioLinha, inicioColuna);
            default:
                throw new RuntimeException("Caractere inválido: " + charAtual);
        }
    }

    private Token lexemaNumerico() {
        int inicioLinha = linha;
        int inicioColuna = coluna;
        StringBuilder numero = new StringBuilder();

        // codigo.substring(posicao).matches("\d+(?![a-zA-Z_])")
        while (posicao < codigo.length() && Character.isDigit(codigo.charAt(posicao))) {
            numero.append(codigo.charAt(posicao));
            posicao++;
            coluna++;
        }

        return new Token(TokenType.TokenSimple.NUMERO, numero.toString(), inicioLinha, inicioColuna);
    }

    private String tipoAtual = null;

    private Token lexemaIdentificador() {
        int inicioLinha = linha;
        int inicioColuna = coluna;
        StringBuilder identificador = new StringBuilder();
    
        while (posicao < codigo.length() && isMatch("[a-zA-Z_0-9]", codigo.charAt(posicao))) {
            identificador.append(codigo.charAt(posicao));
            posicao++;
            coluna++;
        }
    
        String identificadorString = identificador.toString();
    
        // Verifica se o identificador é uma palavra reservada (tipo)
        if (identificadorString.equals("integer") || identificadorString.equals("boolean")) {
            tipoAtual = identificadorString; // Atualiza o tipo atual
            return new Token(TokenType.TokenReserved.valueOf(identificadorString.toUpperCase()), identificadorString, inicioLinha, inicioColuna);
        }
    
        // Verifica se é uma palavra reservada (ex: var, def)
        boolean isReserved = false;
        for (TokenType.TokenReserved reserved : TokenType.TokenReserved.values()) {
            if (reserved.name().equals(identificadorString.toUpperCase())) {
                isReserved = true;
                break;
            }
        }
    
        if (!isReserved) {
            // Busca se o identificador já existe na tabela (em qualquer escopo)
            String tipoExistente = null;
            for (SymbolEntry entry : symbolTable.getEntries()) {
                if (entry.getNome().equals(identificadorString)) {
                    tipoExistente = entry.getTipo();
                    break;
                }
            }
    
            // Usa o tipo existente, ou o tipoAtual, ou "desconhecido"
            String tipoParaAdicionar = tipoExistente != null ? tipoExistente : (tipoAtual != null ? tipoAtual : "desconhecido");
    
            // Verifica se o identificador já existe no mesmo escopo
            boolean alreadyExists = symbolTable.getEntries().stream()
                .anyMatch(entry -> entry.getNome().equals(identificadorString) && entry.getEscopo().equals(escopoAtual));
    
            if (!alreadyExists) {
                symbolTable.addEntry(identificadorString, tipoParaAdicionar, escopoAtual, inicioLinha, inicioColuna);
            }
        }
    
        return new Token(TokenType.TokenSimple.ID, identificadorString, inicioLinha, inicioColuna);
    }
      
     
    
    
    // Método auxiliar para pegar o tipo da variável no contexto anterior
    private String getPreviousType() {
        int prevPos = posicao - 1;
        while (prevPos >= 0 && Character.isWhitespace(codigo.charAt(prevPos))) {
            prevPos--;
        }
    
        if (prevPos >= 6 && codigo.substring(prevPos - 6, prevPos + 1).equals("integer")) {
            return "integer";
        }
        if (prevPos >= 6 && codigo.substring(prevPos - 6, prevPos + 1).equals("boolean")) {
            return "boolean";
        }
    
        return null;
    }    
    

    private TokenType.TokenReserved getTokenReservado(String id) {
        try {
            return TokenType.TokenReserved.valueOf(id.toUpperCase().replace("#", ""));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void skipWhitespace() {
        while (posicao < codigo.length() && Character.isWhitespace(codigo.charAt(posicao))) {
            if (codigo.charAt(posicao) == '\n') {
                linha++;
                coluna = 1;
            } else {
                coluna++;
            }
            posicao++;
        }
    }

    private boolean isMatch(String regex, char charAtual) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(charAtual));
        return matcher.matches();
    }

    private void ignorarComentarios() {
        if (posicao < codigo.length() - 1) {
            // Detecta comentários de linha: "//"
            if (codigo.charAt(posicao) == '/' && codigo.charAt(posicao + 1) == '/') {
                posicao += 2;
                coluna += 2;
                while (posicao < codigo.length() && codigo.charAt(posicao) != '\n') {
                    posicao++;
                    coluna++;
                }
            }
            // Detecta comentários de bloco: "/* ... */"
            else if (codigo.charAt(posicao) == '/' && codigo.charAt(posicao + 1) == '*') {
                posicao += 2;
                coluna += 2;
                while (posicao < codigo.length() - 1) {
                    if (codigo.charAt(posicao) == '*' && codigo.charAt(posicao + 1) == '/') {
                        posicao += 2;
                        coluna += 2;
                        break;
                    }
                    if (codigo.charAt(posicao) == '\n') {
                        linha++;
                        coluna = 1;
                    } else {
                        coluna++;
                    }
                    posicao++;
                }
            }
        }
    }
    
    private void skipComentarioLinha() {
        posicao += 2;
        coluna += 2;
        while (posicao < codigo.length() && codigo.charAt(posicao) != '\n') {
            posicao++;
            coluna++;
        }
    }
    
    private void skipComentarioBloco() {
        posicao += 2;
        coluna += 2;
        while (posicao < codigo.length() - 1) {
            if (codigo.charAt(posicao) == '*' && codigo.charAt(posicao + 1) == '/') {
                posicao += 2;
                coluna += 2;
                break;
            }
            if (codigo.charAt(posicao) == '\n') {
                linha++;
                coluna = 1;
            } else {
                coluna++;
            }
            posicao++;
        }
    }
    
}
