package ast;
import java.util.List;

class DeclaracaoFunction extends DeclaracaoSubRotina {
    private String tipoRetorno;
    private Expressao expressao;

    public DeclaracaoFunction(String nome, Bloco bloco, List<String> parametros, String tipoRetorno, Expressao expressao) {
        super(nome, bloco, parametros);
        this.tipoRetorno = tipoRetorno;
        this.expressao = expressao;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "DeclaracaoFunction{" +
                "nome='" + getNome() + '\'' +
                ", bloco=" + getBloco() +
                ", parametros=" + getParametros() +
                ", tipoRetorno='" + tipoRetorno + '\'' +
                ", expressao=" + expressao +
                '}';
    }
}