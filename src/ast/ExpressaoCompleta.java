package ast;

public class ExpressaoCompleta extends Expressao {
    private Expressao esquerda;
    private Expressao direita;
    private String operador;

    public ExpressaoCompleta(String valor, Expressao esquerda, Expressao direita, String operador) {
        super(valor);
        this.esquerda = esquerda;
        this.direita = direita;
        this.operador = operador;
    }

    public Expressao getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(Expressao esquerda) {
        this.esquerda = esquerda;
    }

    public Expressao getDireita() {
        return direita;
    }

    public void setDireita(Expressao direita) {
        this.direita = direita;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    @Override
    public String toString() {
        return "ExpressaoCompleta{" +
                "valor='" + getValor() + '\'' +
                "esquerda=" + esquerda +
                ", direita=" + direita +
                ", operador='" + operador + '\'' +
                '}';
    }
}