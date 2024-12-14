package ast;

public class ExpressaoFatorAtributo extends Expressao {
    private Object atributo;

    // Construtor genérico para qualquer tipo de atributo
    public ExpressaoFatorAtributo(String valor, Object atributo) {
        super(valor);
        this.atributo = atributo;
    }

    // Construtor para int
    public ExpressaoFatorAtributo(String valor, int atributo) {
        super(valor); 
        this.atributo = atributo;
    }

    // Construtor para boolean
    public ExpressaoFatorAtributo(String valor, boolean atributo) {
        super(valor);
        this.atributo = atributo;
    }

    public Object getAtributo() {
        return atributo;
    }

    public void setAtributo(Object atributo) {
        this.atributo = atributo;
    }

    @Override
    public String toString() {
        return "ExpressaoFatorAtributo{" +
                "valor='" + getValor() + '\'' +
                ", atributo=" + atributo +
                '}';
    }
}