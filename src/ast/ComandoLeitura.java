package ast;

public class ComandoLeitura extends Comando {
    private Expressao expressao;

    public ComandoLeitura(Expressao expressao) {
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
        return "ComandoLeitura{" +
                "expressao=" + expressao +
                '}';
    }
}