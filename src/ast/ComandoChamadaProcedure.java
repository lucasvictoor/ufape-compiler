package ast;
import java.util.List;

public class ComandoChamadaProcedure extends Comando {
    private String nome;
    private List<Expressao> parametros;

    public ComandoChamadaProcedure(String nome, List<Expressao> parametros) {
        this.nome = nome;
        this.parametros = parametros;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Expressao> getParametros() {
        return parametros;
    }

    public void setParametros(List<Expressao> parametros) {
        this.parametros = parametros;
    }

    @Override
    public String toString() {
        return "ComandoChamadaProcedure{" +
                "nome='" + nome + '\'' +
                ", parametros=" + parametros +
                '}';
    }
}