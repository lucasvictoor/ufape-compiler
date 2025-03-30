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

    private void analyzeBloco(Bloco bloco) {
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

    private void analyzeDeclaracaoVariavel(DeclaracaoVariavel declaracao) {
        // Implementar a lógica de análise semântica para declarações de variáveis aqui

        // Validar se quando há atribuição, o tipo da variável é compatível com o valor atribuído
        if (declaracao.getValorInicializado().getClass() == ExpressaoFatorAtributo.class) {
            String tipoVariavel = declaracao.getTipo();
            String tipoValorInicializado = declaracao.getValorInicializado().toString().matches("\\w+\\{atributo=\\d+\\}") ? "integer" : "boolean";
            if (!tipoVariavel.equals(tipoValorInicializado)) {
                throw new SemanticError("Tipo da variável " + declaracao.getIdentificador() + " não é compatível com o valor inicializado. Esperado: " + tipoVariavel + ", encontrado: " + tipoValorInicializado);
            }
        }
    }

    private void analyzeDeclaracaoSubRotina(DeclaracaoSubRotina declaracao) {
        // Implementar a lógica de análise semântica para declarações de sub-rotinas aqui
        analyzeBloco(declaracao.getBloco());
    }

    private void analyzeComando(Comando comando) {
        // Implementar a lógica de análise semântica para comandos aqui

        if (comando instanceof ComandoAtribuicao) {
            analyzeComandoAtribuicao((ComandoAtribuicao) comando);
        } else if (comando instanceof ComandoCondicional) {
            analyzeComandoCondicional((ComandoCondicional) comando);
        } else if (comando instanceof ComandoEnquanto) {
            analyzeComandoEnquanto((ComandoEnquanto) comando);
        } else if (comando instanceof ComandoChamadaProcedure) {
            analyzeComandoChamadaProcedure((ComandoChamadaProcedure) comando);
        } else if (comando instanceof ComandoLeitura) {
            analyzeComandoLeitura((ComandoLeitura) comando);
        } else if (comando instanceof ComandoBreak) {
            analyzeComandoBreak((ComandoBreak) comando);
        } else if (comando instanceof ComandoContinue) {
            analyzeComandoContinue((ComandoContinue) comando);
        }

    }

    private void analyzeComandoContinue(ComandoContinue comando) {
        
    }

    private void analyzeComandoBreak(ComandoBreak comando) {
        
    }

    private void analyzeComandoLeitura(ComandoLeitura comando) {
        
    }

    private void analyzeComandoChamadaProcedure(ComandoChamadaProcedure comando) {
    
    }

    private void analyzeComandoEnquanto(ComandoEnquanto comando) {
        
    }

    private void analyzeComandoCondicional(ComandoCondicional comando) {
    
    }

    private void analyzeComandoAtribuicao(ComandoAtribuicao comando) {
    
    }

    private void analyzeExpressao(Expressao expressao) {
        // Implementar a lógica de análise semântica para expressões aqui
    }

    private void analyzeTermo(Expressao expressao) {
        // Implementar a lógica de análise semântica para expressões aqui
    }

    private void analyzeFator(Expressao expressao) {
        // Implementar a lógica de análise semântica para expressões aqui
    }
}
