package ast;
import java.util.List;

public class ExpressaoFatorChamadaFunction extends Expressao {
    private String nome;
    private List<Argumento> argumentos;

    public ExpressaoFatorChamadaFunction(String nome, List<Argumento> argumentos) {
        this.nome = nome;
        this.argumentos = argumentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Argumento> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(List<Argumento> argumentos) {
        this.argumentos = argumentos;
    }

    @Override
    public String toString() {
        return "ExpressaoChamadaFunction{" +
                "nome='" + nome + '\'' +
                ", argumentos=" + argumentos +
                '}';
    }
}