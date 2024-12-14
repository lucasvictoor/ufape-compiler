package ast;

public class ComandoCondicional extends Comando {
    private Expressao expressao;
    private Comando comando;
    private Comando comandoElse;

    public ComandoCondicional(Expressao expressao, Comando comando, Comando comandoElse) {
        this.expressao = expressao;
        this.comando = comando;
        this.comandoElse = comandoElse;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando comando) {
        this.comando = comando;
    }

    public Comando getComandoElse() {
        return comandoElse;
    }

    public void setComandoElse(Comando comandoElse) {
        this.comandoElse = comandoElse;
    }

    @Override
    public String toString() {
        return "ComandoCondicional{" +
                "expressao=" + expressao +
                ", comando=" + comando +
                ", comandoElse=" + comandoElse +
                '}';
    }
}