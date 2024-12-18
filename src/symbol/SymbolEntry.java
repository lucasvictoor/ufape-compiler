package symbol;

public class SymbolEntry {
    private String nome;
    private String tipo;
    private String escopo;
    private int linha;
    private int coluna;

    public SymbolEntry(String nome, String tipo, String escopo, int linha, int coluna) {
        this.nome = nome;
        this.tipo = tipo;
        this.escopo = escopo;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getEscopo() { return escopo; }
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
}