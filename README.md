# Compilador - UFAPE 2024.2

Este é um projeto de compilador desenvolvido para a disciplina de Compiladores. O compilador é capaz de analisar, tokenizar e parsear um código fonte escrito em uma linguagem específica, gerando uma Árvore de Sintaxe Abstrata (AST) e uma Tabela de Símbolos.

## Estrutura do Projeto

O projeto está organizado da seguinte forma:
```
compilador-projeto/ 
├── src/
│ ├── ast/ 
│ ├── lexer/ 
│ │ ├── Lexer.java 
│ │ ├── Token.java 
│ │ ├── TokenType.java 
│ ├── parser/ 
│ │ ├── Parser.java 
│ │ ├── ASTNode.java 
│ ├── symbol/ 
│ │ ├── SymbolTable.java 
│ │ ├── SymbolEntry.java 
│ ├── main/ 
│ │ ├── App.java 
├── README.md
```

## Descrição dos Componentes

### Lexer

O lexer é responsável por analisar o código fonte e gerar uma lista de tokens. Ele também constrói a Tabela de Símbolos.

- **Lexer.java**: Implementa a lógica de tokenização e construção da Tabela de Símbolos.
- **Token.java**: Representa um token com tipo, valor, linha e coluna.
- **TokenType.java**: Define os tipos de tokens, incluindo palavras reservadas, operadores e símbolos.

### Parser

O parser é responsável por analisar a lista de tokens gerada pelo lexer e construir a Árvore de Sintaxe Abstrata (AST).

- **Parser.java**: Implementa a lógica de parsing e construção da AST.
- **ASTNode.java**: Define as classes que representam os diferentes nós da AST.

### Symbol Table

A Tabela de Símbolos armazena informações sobre variáveis, funções e procedimentos definidos no código fonte.

- **SymbolTable.java**: Implementa a Tabela de Símbolos.
- **SymbolEntry.java**: Representa uma entrada na Tabela de Símbolos.

### Main

O ponto de entrada do programa.

- **App.java**: Contém o método `main` que inicializa o lexer, parser e exibe a Tabela de Símbolos e a AST.

## Como Executar

1. **Compilar o Projeto**:
   Navegue até o diretório do projeto e execute o seguinte comando para compilar todos os arquivos Java:

   ```sh
   javac -d . src/lexer/*.java src/parser/*.java src/symbol/*.java src/main/*.java
   ```

2. **Executar o Projeto**: 
    Execute o seguinte comando para iniciar o programa:

    ```sh
    java main.App
    ```

### Exemplo de Código Fonte
Aqui está um exemplo de código fonte que pode ser analisado pelo compilador:

```markdown
var integer x_1 := 10;
var integer x_2 := 20;
var integer x_3, x_4;
var boolean x_5 := true;

def soma (x_1, x_2): integer do
    var integer x;
    x := x_1 + x_2;
    return x;
end

def menos (x_1, x_2): void do
    var integer x;
    x := x_1 - x_2;
    x_5 := false;
    print(x);
end
```
