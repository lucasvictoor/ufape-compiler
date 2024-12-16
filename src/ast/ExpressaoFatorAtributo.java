package ast;

public class ExpressaoFatorAtributo extends Expressao {
    private Object atributo;

    // Construtor genérico para qualquer tipo de atributo
    public ExpressaoFatorAtributo(Object atributo) {
        this.atributo = atributo;
    }

    // Construtor para int
    public ExpressaoFatorAtributo(int atributo) { 
        this.atributo = atributo;
    }

    // Construtor para boolean
    public ExpressaoFatorAtributo(boolean atributo) {
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
                "atributo=" + atributo +
                '}';
    }
}