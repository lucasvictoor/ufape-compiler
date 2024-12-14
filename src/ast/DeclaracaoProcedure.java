package ast;
import java.util.List;

class DeclaracaoProcedure extends DeclaracaoSubRotina {
    public DeclaracaoProcedure(String nome, Bloco bloco, List<String> parametros) {
        super(nome, bloco, parametros);
    }

    @Override
    public String toString() {
        return "DeclaracaoProcedure{" +
                "nome='" + getNome() + '\'' +
                ", bloco=" + getBloco() +
                ", parametros=" + getParametros() +
                '}';
    }
}