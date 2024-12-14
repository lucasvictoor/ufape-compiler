package ast;
import java.util.List;

public class ComandoAtribuicao extends Comando {
    private String identificador;
    private List<Expressao> expressao;

    public ComandoAtribuicao(String identificador, List<Expressao> expressao) {
        this.identificador = identificador;
        this.expressao = expressao;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public List<Expressao> getExpressao() {
        return expressao;
    }

    public void setExpressao(List<Expressao> expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "ComandoAtribuicao{" +
                "identificador='" + identificador + '\'' +
                ", expressao=" + expressao +
                '}';
    }
}