package ast;

public class Argumento extends ASTNode {
    private Expressao expressao;

    public Argumento(Expressao expressao) {
        this.expressao = expressao;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "Argumento{" +
                "expressao=" + expressao +
                '}';
    }
}