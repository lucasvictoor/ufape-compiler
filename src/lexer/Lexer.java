package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import symbol.SymbolTable;
import symbol.SymbolTableEntry;

public class Lexer {

    private String codigo;
    private int linha;
    private int coluna;
    private int posicao;
    private SymbolTable symbolTable;

    public Lexer() {
        this.linha = 1;
        this.coluna = 1;
        this.posicao = 0;
        this.symbolTable = new SymbolTable();
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
            } else if (isMatch("[a-zA-Z_]", charAtual)) {
                // System.out.println("Letra charAtual: " + charAtual);
                tokens.add(lexemaIdentificador());
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

    private Token lexemaIdentificador() {
        int inicioLinha = linha;
        int inicioColuna = coluna;
        StringBuilder identificador = new StringBuilder();
    
        while (posicao < codigo.length() && isMatch("[a-zA-Z_0-9#]", codigo.charAt(posicao))) {
            identificador.append(codigo.charAt(posicao));
            posicao++;
            coluna++;

            if (identificador.toString().contains("#")) {
                break;
            }
        }
    
        String identificadorString = identificador.toString();
        TokenType.TokenReserved tokenReserved = getTokenReservado(identificadorString);
    
        if (tokenReserved != null) {
            return new Token(tokenReserved, identificadorString, inicioLinha, inicioColuna);
        } else {
            // Adiciona o identificador à tabela de símbolos
            // Verifica se há um tipo previamente identificado antes do identificador
            String tipo = getPreviousType();
    
            symbolTable.addEntry(identificadorString, tipo, "variavel", "global", null, inicioLinha, inicioColuna);
    
            return new Token(TokenType.TokenSimple.ID, identificadorString, inicioLinha, inicioColuna);
        }
    }
    
    // Método auxiliar para pegar o tipo da variável no contexto anterior
    private String getPreviousType() {
        int prevPos = posicao - 1;
        while (prevPos >= 0 && Character.isWhitespace(codigo.charAt(prevPos))) {
            prevPos--;
        }
    
        StringBuilder tipo = new StringBuilder();
        while (prevPos >= 0 && isMatch("[a-zA-Z]", codigo.charAt(prevPos))) {
            tipo.insert(0, codigo.charAt(prevPos));
            prevPos--;
        }
    
        String tipoString = tipo.toString();
        if (tipoString.equals("integer") || tipoString.equals("boolean")) {
            return tipoString;
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
}
