package lexer;

public class TokenType {

    public enum TokenReserved {
        BEGIN, END, VAR, INTEGER, BOOLEAN, DEF, DO, RETURN, IF, ELSE, WHILE, PRINT, FUNCTION, PROCEDURE, CONTINUE, BREAK, TRUE, FALSE, VOID, EOF;
    }

    public enum TokenSimple {
        ID, NUMERO;
    }

    public enum TokenOperator {
        MAIS, MENOS, VEZES, DIVIDIDO, IGUAL, ATRIBUICAO, DIFERENTE, MENOR, MENORIGUAL, MAIOR, MAIORIGUAL;
    }

    public enum TokenSymbol {
        ABRE_PARENTESE, FECHA_PARENTESE, PONTO_E_VIRGULA, DOIS_PONTOS, VIRGULA;
    }
}