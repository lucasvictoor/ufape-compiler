package ast;

public class Expressao extends ASTNode {
    private String valor;

    public Expressao(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Expressao{" +
                "valor='" + valor + '\'' +
                '}';
    }
}