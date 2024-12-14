package ast;

class Parametro extends ASTNode {
    private String tipo;
    private String identificador;

    public Parametro(String tipo, String identificador) {
        this.tipo = tipo;
        this.identificador = identificador;
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

    @Override
    public String toString() {
        return "Parametro{" +
                "tipo='" + tipo + '\'' +
                ", identificador='" + identificador + '\'' +
                '}';
    }
}