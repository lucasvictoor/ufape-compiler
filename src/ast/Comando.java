package ast;

public class Comando extends ASTNode {
    private String comando;

    public Comando() {
        // Construtor padrão
        this.comando = "default";
    }

    public Comando(String comando) {
        this.comando = comando;
    }

    @Override
    public String toString() {
        return "Comando{" +
                "comando='" + comando + '\'' +
                '}';
    }
}
