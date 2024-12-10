package parser;

import java.util.List;

public abstract class ASTNode {
    @Override
    public abstract String toString();
}

class Programa extends ASTNode {
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

class Bloco extends ASTNode {
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

    public void setComando(List<Comando> comando) {
        this.comandos = comando;
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

class DeclaracaoVariavel extends ASTNode {
    private String tipo;
    private String identificador;
    private Expressao valorInicializado;

    public DeclaracaoVariavel(String tipo, String identificador, Expressao valorInicializado) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.valorInicializado = valorInicializado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Expressao getValorInicializado() {
        return valorInicializado;
    }

    public void setValorInicializado(Expressao valorInicializado) {
        this.valorInicializado = valorInicializado;
    }

    @Override
    public String toString() {
        return "DeclaracaoVariavel{" +
                "tipo='" + tipo + '\'' +
                ", identificador='" + identificador + '\'' +
                ", valorInicializado=" + valorInicializado +
                '}';
    }
}

class DeclaracaoSubRotina extends ASTNode {
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

class DeclaracaoFunction extends DeclaracaoSubRotina {
    private String tipoRetorno;
    private Expressao expressao;

    public DeclaracaoFunction(String nome, Bloco bloco, List<String> parametros, String tipoRetorno, Expressao expressao) {
        super(nome, bloco, parametros);
        this.tipoRetorno = tipoRetorno;
        this.expressao = expressao;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "DeclaracaoFunction{" +
                "nome='" + getNome() + '\'' +
                ", bloco=" + getBloco() +
                ", parametros=" + getParametros() +
                ", tipoRetorno='" + tipoRetorno + '\'' +
                ", expressao=" + expressao +
                '}';
    }
}

abstract class Comando extends ASTNode {}

class ComandoChamadaProcedure extends Comando {
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

class ComandoAtribuicao extends Comando {
    private String identificador;
    private Expressao expressao;

    public ComandoAtribuicao(String identificador, Expressao expressao) {
        this.identificador = identificador;
        this.expressao = expressao;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "ComandoAtribuicao{" +
                "identificador='" + identificador + '\'' +
                ", expressao=" + expressao +
                '}';
    }
}

class ComandoCondicional extends Comando {
    private Expressao expressao;
    private Comando comando;
    private Comando comandoElse;

    public ComandoCondicional(Expressao expressao, Comando comando, Comando comandoElse) {
        this.expressao = expressao;
        this.comando = comando;
        this.comandoElse = comandoElse;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando comando) {
        this.comando = comando;
    }

    public Comando getComandoElse() {
        return comandoElse;
    }

    public void setComandoElse(Comando comandoElse) {
        this.comandoElse = comandoElse;
    }

    @Override
    public String toString() {
        return "ComandoCondicional{" +
                "expressao=" + expressao +
                ", comando=" + comando +
                ", comandoElse=" + comandoElse +
                '}';
    }
}

class ComandoEnquanto extends Comando {
    private Expressao expressao;
    private Comando comando;

    public ComandoEnquanto(Expressao expressao, Comando comando) {
        this.expressao = expressao;
        this.comando = comando;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando comando) {
        this.comando = comando;
    }

    @Override
    public String toString() {
        return "ComandoEnquanto{" +
                "expressao=" + expressao +
                ", comando=" + comando +
                '}';
    }
}

class ComandoLeitura extends Comando {
    private Expressao expressao;

    public ComandoLeitura(Expressao expressao) {
        this.expressao = expressao;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "ComandoLeitura{" +
                "expressao=" + expressao +
                '}';
    }
}

class ComandoBreak extends Comando {
    @Override
    public String toString() {
        return "ComandoBreak{}";
    }
}

class ComandoContinue extends Comando {
    @Override
    public String toString() {
        return "ComandoContinue{}";
    }
}

abstract class Expressao extends ASTNode {}

class ExpressaoLiteral extends Expressao {
    private Object valor;

    public ExpressaoLiteral(Object valor) {
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "ExpressaoLiteral{" +
                "valor=" + valor +
                '}';
    }
}

class ExpressaoVariavel extends Expressao {
    private String identificador;

    public ExpressaoVariavel(String identificador) {
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "ExpressaoVariavel{" +
                "identificador='" + identificador + '\'' +
                '}';
    }
}

class ExpressaoBinaria extends Expressao {
    private Expressao esquerda;
    private Expressao direita;
    private String operador;

    public ExpressaoBinaria(Expressao esquerda, Expressao direita, String operador) {
        this.esquerda = esquerda;
        this.direita = direita;
        this.operador = operador;
    }

    public Expressao getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(Expressao esquerda) {
        this.esquerda = esquerda;
    }

    public Expressao getDireita() {
        return direita;
    }

    public void setDireita(Expressao direita) {
        this.direita = direita;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    @Override
    public String toString() {
        return "ExpressaoBinaria{" +
                "esquerda=" + esquerda +
                ", direita=" + direita +
                ", operador='" + operador + '\'' +
                '}';
    }
}

class ExpressaoChamadaFunction extends Expressao {
    private String nome;
    private List<Expressao> argumentos;

    public ExpressaoChamadaFunction(String nome, List<Expressao> argumentos) {
        this.nome = nome;
        this.argumentos = argumentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Expressao> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(List<Expressao> argumentos) {
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

class Parametro extends ASTNode {
    private String tipo;
    private String identificador;

    public Parametro(String tipo, String identificador) {
        this.tipo = tipo;
        this.identificador = identificador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "Parametro{" +
                "tipo='" + tipo + '\'' +
                ", identificador='" + identificador + '\'' +
                '}';
    }
}

class Argumento extends ASTNode {
    private Expressao expressao;

    public Argumento(Expressao expressao) {
        this.expressao = expressao;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public String toString() {
        return "Argumento{" +
                "expressao=" + expressao +
                '}';
    }
}