package ast;

import java.util.List;

public class ComandoCondicional extends Comando {
    private List<Expressao> expressao;    
    private List<Comando> comandosIf;   
    private List<Comando> comandosElse; 

    public ComandoCondicional(List<Expressao> expressao, List<Comando> comandosIf, List<Comando> comandosElse) {
        this.expressao = expressao;
        this.comandosIf = comandosIf;
        this.comandosElse = comandosElse;
    }

    public List<Expressao> getExpressao() {
        return expressao;
    }

    public void setExpressao(List<Expressao> expressao) {
        this.expressao = expressao;
    }

    public List<Comando> getComandosIf() {
        return comandosIf;
    }

    public void setComandosIf(List<Comando> comandosIf) {
        this.comandosIf = comandosIf;
    }

    public List<Comando> getComandosElse() {
        return comandosElse;
    }

    public void setComandosElse(List<Comando> comandosElse) {
        this.comandosElse = comandosElse;
    }

    @Override
    public String toString() {
        return "ComandoCondicional{" +
                "expressao=" + expressao +
                ", comandosIf=" + comandosIf +
                ", comandosElse=" + (comandosElse.isEmpty() ? "null" : comandosElse) +
                '}';
    }
}