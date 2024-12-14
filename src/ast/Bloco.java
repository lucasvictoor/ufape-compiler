package ast;
import java.util.List;

public class Bloco extends ASTNode {
    private List<DeclaracaoVariavel> declaracaoVariavel;
    private List<DeclaracaoSubRotina> declaracaoSubRotina;
    private List<Comando> comandos;

    public Bloco(List<DeclaracaoVariavel> declaracaoVariavel, List<DeclaracaoSubRotina> declaracaoSubRotina, List<Comando> comandos) {
        this.declaracaoVariavel = declaracaoVariavel;
        this.declaracaoSubRotina = declaracaoSubRotina;
        this.comandos = comandos;
    }

    public List<DeclaracaoVariavel> getDeclaracaoVariavel() {
        return declaracaoVariavel;
    }

    public void setDeclaracaoVariavel(List<DeclaracaoVariavel> declaracaoVariavel) {
        this.declaracaoVariavel = declaracaoVariavel;
    }

    public List<DeclaracaoSubRotina> getDeclaracaoSubRotina() {
        return declaracaoSubRotina;
    }

    public void setDeclaracaoSubRotina(List<DeclaracaoSubRotina> declaracaoSubRotina) {
        this.declaracaoSubRotina = declaracaoSubRotina;
    }

    public List<Comando> getComandos() {
        return comandos;
    }

    public void setComandos(List<Comando> comandos) {
        this.comandos = comandos;
    }

    @Override
    public String toString() {
        return "Bloco{" +
                "declaracaoVariavel=" + declaracaoVariavel +
                ", declaracaoSubRotina=" + declaracaoSubRotina +
                ", comando=" + comandos +
                '}';
    }
}