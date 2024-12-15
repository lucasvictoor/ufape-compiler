package ast;

public class ExpressaoFatorVariavel extends Expressao {
    private String identificador;

    public ExpressaoFatorVariavel(String identificador) {
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "ExpressaoVariavel{" +
                "valor='" + getValor() + '\'' +
                "identificador='" + identificador + '\'' +
                '}';
    }
}