package ast;

public class Programa extends ASTNode {
    private Bloco bloco;
    private String nome;

    public Programa(String nome, Bloco bloco) {
        this.nome = nome;
        this.bloco = bloco;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Programa{" +
                "nome='" + nome + '\'' +
                ", bloco=" + bloco +
                '}';
    }
}