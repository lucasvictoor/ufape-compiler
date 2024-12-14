package ast;
import java.util.List;

public class DeclaracaoSubRotina extends ASTNode {
    private String nome;
    private Bloco bloco;
    private List<String> parametros;

    public DeclaracaoSubRotina(String nome, Bloco bloco, List<String> parametros) {
        this.nome = nome;
        this.bloco = bloco;
        this.parametros = parametros;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public List<String> getParametros() {
        return parametros;
    }

    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }

    @Override
    public String toString() {
        return "DeclaracaoSubRotina{" +
                "nome='" + nome + '\'' +
                ", bloco=" + bloco +
                ", parametros=" + parametros +
                '}';
    }
}