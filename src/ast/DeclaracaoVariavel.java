package ast;

public class DeclaracaoVariavel extends ASTNode {
    private String tipo;
    private String identificador;
    private Expressao valorInicializado;

    public DeclaracaoVariavel(String tipo, String identificador, Expressao valorInicializado) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.valorInicializado = valorInicializado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Expressao getValorInicializado() {
        return valorInicializado;
    }

    public void setValorInicializado(Expressao valorInicializado) {
        this.valorInicializado = valorInicializado;
    }

    @Override
    public String toString() {
        return "DeclaracaoVariavel{" +
                "tipo='" + tipo + '\'' +
                ", identificador='" + identificador + '\'' +
                ", valorInicializado=" + valorInicializado +
                '}';
    }
}