package parser;

import java.util.List;

public abstract class ASTNode {
    
}

class Programa extends ASTNode {
    public Bloco bloco;
    public String nome;

    public Programa(String nome, Bloco bloco) {
        this.nome = nome;
        this.bloco = bloco;
    }
}

class Bloco extends ASTNode {
    public DeclaracaoVariavel declaracaoVariavel;
    public DeclaracaoSubRotina declaracaoSubRotina;
    public Comando comando;

    public Bloco(DeclaracaoVariavel declaracaoVariavel, DeclaracaoSubRotina declaracaoSubRotina, Comando comando) {
        this.declaracaoVariavel = declaracaoVariavel;
        this.declaracaoSubRotina = declaracaoSubRotina;
        this.comando = comando;
    }
}

class DeclaracaoVariavel extends ASTNode {
    public String tipo;
    public String identificador;

    public DeclaracaoVariavel(String tipo, String identificador) {
        this.tipo = tipo;
        this.identificador = identificador;
    }
}

class DeclaracaoSubRotina extends ASTNode {
    public String nome;
    public Bloco bloco;
    public List<String> parametros;

    public DeclaracaoSubRotina(String nome, Bloco bloco, List<String> parametros) {
        this.nome = nome;
        this.bloco = bloco;
        this.parametros = parametros;
    }
}

class DeclaracaoProcedure extends DeclaracaoSubRotina {
    public DeclaracaoProcedure(String nome, Bloco bloco, List<String> parametros) {
        super(nome, bloco, parametros);
    }
}

class DeclaracaoFunction extends DeclaracaoSubRotina {
    public String tipoRetorno;
    public Expressao expressao;

    public DeclaracaoFunction(String nome, Bloco bloco, List<String> parametros, String tipoRetorno, Expressao expressao) {
        super(nome, bloco, parametros);
        this.tipoRetorno = tipoRetorno;
        this.expressao = expressao;
    }

}

abstract class Comando extends ASTNode {

}

class ComandoChamadaProcedure extends Comando {
    public String nome;
    public List<Expressao> parametros;

    public ComandoChamadaProcedure(String nome, List<Expressao> parametros) {
        this.nome = nome;
        this.parametros = parametros;
    }

}

class ComandoAtribuicao extends Comando {
    public String identificador;
    public Expressao expressao;

    public ComandoAtribuicao(String identificador, Expressao expressao) {
        this.identificador = identificador;
        this.expressao = expressao;
    }    
}

class ComandoCondicional extends Comando {
    public Expressao expressao;
    public Comando comando;
    public Comando comandoElse;

    public ComandoCondicional(Expressao expressao, Comando comando, Comando comandoElse) {
        this.expressao = expressao;
        this.comando = comando;
        this.comandoElse = comandoElse;
    }
    
}

class ComandoEnquanto extends Comando {
    public Expressao expressao;
    public Comando comando;

    public ComandoEnquanto(Expressao expressao, Comando comando) {
        this.expressao = expressao;
        this.comando = comando;
    }    
}

class ComandoLeitura extends Comando {
    public Expressao expressao;

    public ComandoLeitura(Expressao expressao) {
        this.expressao = expressao;
    }
    
}

class ComandoBreak extends Comando {}
class ComandoContinue extends Comando {}

abstract class Expressao extends ASTNode {}

class ExpressaoLiteral extends Expressao {
    public Object valor;

    public ExpressaoLiteral(Object valor) {
        this.valor = valor;
    }
}

class ExpressaoVariavel extends Expressao {
    public String identificador;

    public ExpressaoVariavel(String identificador) {
        this.identificador = identificador;
    }
}

class ExpressaoBinaria extends Expressao {
    public Expressao esquerda;
    public Expressao direita;
    public String operador;

    public ExpressaoBinaria(Expressao esquerda, Expressao direita, String operador) {
        this.esquerda = esquerda;
        this.direita = direita;
        this.operador = operador;
    }
}

class ExpressaoChamadaFunction extends Expressao {
    public String nome;
    public List<Expressao> argumentos;

    public ExpressaoChamadaFunction(String nome, List<Expressao> parametros) {
        this.nome = nome;
        this.argumentos = parametros;
    }
}

class Parametro extends ASTNode {
    public String tipo;
    public String identificador;

    public Parametro(String tipo, String identificador) {
        this.tipo = tipo;
        this.identificador = identificador;
    }    
}

class Argumento extends ASTNode {
    public Expressao expressao;

    public Argumento(Expressao expressao) {
        this.expressao = expressao;
    }
}