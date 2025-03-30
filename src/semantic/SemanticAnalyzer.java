package semantic;

import java.util.List;

import ast.*;
import symbol.SymbolTable;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;
    private String currentScope = "global";
    private boolean inLoop = false;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void analyze(Programa programa) {
        // Implementar a lógica de análise semântica aqui
        analyzeBloco(programa.getBloco());
        System.out.println("Análise semântica concluída com sucesso. Nenhum erro encontrado.");
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
            checkTypeCompatibility(declaracao.getIdentificador(), tipoVariavel, tipoValorInicializado);
        }
    }

    private void checkTypeCompatibility(String identificador, String tipoVariavel, String tipoValorAtribuido) {
        if (!tipoVariavel.toLowerCase().equals(tipoValorAtribuido.toLowerCase())) {
            throw new SemanticError("Tipo da variável " + identificador + " não é compatível com o valor inicializado. Esperado: " + tipoVariavel + ", encontrado: " + tipoValorAtribuido);
        }
    }

    private void analyzeDeclaracaoSubRotina(DeclaracaoSubRotina declaracao) {
        // Implementar a lógica de análise semântica para declarações de sub-rotinas aqui
        currentScope = "local";
        analyzeBloco(declaracao.getBloco());
        currentScope = "global";
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
        if (!inLoop) {
            throw new SemanticError("'continue' utilizado fora de um laço.");
        }
    }

    private void analyzeComandoBreak(ComandoBreak comando) {
        
    }

    private void analyzeComandoLeitura(ComandoLeitura comando) {
        
    }

    private void analyzeComandoChamadaProcedure(ComandoChamadaProcedure comando) {
    
    }

    private void analyzeComandoEnquanto(ComandoEnquanto comando) {
        boolean previousInLoop = inLoop;
        inLoop = true;
    
        for (Comando cmd : comando.getComando()) {
            analyzeComando(cmd);
        }
    
        inLoop = previousInLoop;
    }

    private void analyzeComandoCondicional(ComandoCondicional comando) {
    
    }

    private void analyzeComandoAtribuicao(ComandoAtribuicao comando) {
        // Implementar a lógica de análise semântica para atribuições aqui
        String identificadorAtribuicao = comando.getIdentificador();
        if (!symbolTable.contains(identificadorAtribuicao)) {
            throw new SemanticError("Variável " + identificadorAtribuicao + " não declarada.");
        }

        String tipoIdenficadorAtribuicao = symbolTable.getEntry(identificadorAtribuicao).getTipo();
        
        for (Expressao expressao : comando.getExpressao()) {
            if (expressao instanceof ExpressaoFatorVariavel) {
                ExpressaoFatorVariavel fatorVariavel = (ExpressaoFatorVariavel) expressao;
                
                String tipoVariavelAtribuida = symbolTable.getEntry(fatorVariavel.getIdentificador()).getTipo();
                checkTypeCompatibility(fatorVariavel.getIdentificador(), tipoIdenficadorAtribuicao, tipoVariavelAtribuida);
            } else if (expressao instanceof ExpressaoFatorAtributo) {
                ExpressaoFatorAtributo fatorAtributo = (ExpressaoFatorAtributo) expressao;
                
                String tipoAtributoAtribuido = fatorAtributo.getAtributo().getClass().getSimpleName();
                checkTypeCompatibility(fatorAtributo.getAtributo().toString(), tipoIdenficadorAtribuicao, tipoAtributoAtribuido);
            } else if (expressao instanceof ExpressaoFatorChamadaFunction) {
                ExpressaoFatorChamadaFunction fatorChamadaFunction = (ExpressaoFatorChamadaFunction) expressao;

                String tipoFuncaoAtribuida = symbolTable.getEntry(fatorChamadaFunction.getNome()).getTipo();
                checkTypeCompatibility(identificadorAtribuicao, tipoIdenficadorAtribuicao, tipoFuncaoAtribuida);
            } else if (expressao instanceof ExpressaoCompleta) {
                analyzeExpressao(expressao);
            }
        }
        
        //analise expressão [ExpressaoChamadaFunction{nome='', argumentos=''}]

        //analise expressão [ExpressaoCompleta{esquerda='', direita='', operador=''}]
             
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
