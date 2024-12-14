package ast;
import java.util.List;

public class ComandoEnquanto extends Comando {
    private List<Expressao> expressao;
    private List<Comando> comando;

    public ComandoEnquanto(List<Expressao> expressao, List<Comando> comando) {
        this.expressao = expressao;
        this.comando = comando;
    }

    public List<Expressao> getExpressao() {
        return expressao;
    }

    public void setExpressao(List<Expressao> expressao) {
        this.expressao = expressao;
    }

    public List<Comando> getComando() {
        return comando;
    }

    public void setComando(List<Comando> comando) {
        this.comando = comando;
    }

    @Override
    public String toString() {
        return "ComandoEnquanto{" +
                "expressao=" + expressao +
                ", comando=" + comando +
                '}';
    }
}