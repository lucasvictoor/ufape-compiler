package semantic;

import ast.*;
import symbol.SymbolTable;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;
    private int currentScopeLevel;
    private int currentFunctionLevel;
    private int currentProcedureLevel;
    private int currentLine;
    private int currentColumn;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.currentScopeLevel = 0;
        this.currentFunctionLevel = 0;
        this.currentProcedureLevel = 0;
        this.currentLine = 0;
        this.currentColumn = 0;
    }

    public void analyze(Programa programa) {
        // Implementar a lógica de análise semântica aqui
        analyzeBloco(programa.getBloco());
    }   

    public void analyzeBloco(Bloco bloco) {
        // Implementar a lógica de análise semântica para blocos aqui
        
        for (DeclaracaoVariavel variavel : bloco.getDeclaracaoVariavel()) {
            analyzeDeclaracaoVariavel(variavel);
        }

        for (DeclaracaoSubRotina subRoutineDeclaration : bloco.getDeclaracaoSubRotina()) {
            analyzeDeclaracaoSubRotina(subRoutineDeclaration);
        }

        for (Comando cmd : bloco.getComandos()) {
            analyzeComando(cmd);
        }
    }

    public void analyzeDeclaracaoVariavel(DeclaracaoVariavel declaracao) {
        // Implementar a lógica de análise semântica para declarações de variáveis aqui

        // Validar se quando há atribuição, o tipo da variável é compatível com o valor atribuído
        if (declaracao.getValorInicializado().getClass() != ExpressaoNula.class) {
            //declaracao.getValorInicializado(): ExpressaoFatorAtributo{atributo=true}
            String tipoVariavel = declaracao.getTipo();
            String tipoValorInicializado = declaracao.getValorInicializado().toString().matches("\\w+\\{atributo=\\d+\\}") ? "integer" : "boolean";
            if (!tipoVariavel.equals(tipoValorInicializado)) {
                System.err.println("Erro semântico: Tipo incompatível na linha ");
            }
        }
    }

    public void analyzeDeclaracaoSubRotina(DeclaracaoSubRotina declaracao) {
        // Implementar a lógica de análise semântica para declarações de sub-rotinas aqui
    }

    public void analyzeComando(Comando comando) {
        // Implementar a lógica de análise semântica para comandos aqui
    }

    public void analyzeExpressao(Expressao expressao) {
        // Implementar a lógica de análise semântica para expressões aqui
    }
    
}
