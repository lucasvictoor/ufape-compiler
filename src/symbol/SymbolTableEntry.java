package symbol;

public class SymbolTableEntry {

    private static int nextId = 1;

    private int id;
    private String identificador;
    private String tipo;
    private String category;
    private String scope; 
    private Object parametros;
    private int linha;
    private int coluna;

    public SymbolTableEntry(String name, String type, String category, String scope, Object value, int linha, int coluna) {
        this.id = nextId++;
        this.identificador = name;
        this.tipo = type;
        this.category = category;
        this.scope = scope;
        this.parametros = value;
        this.linha = linha;
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "SymbolTableEntry{" +
                "id=" + id +
                ", nome='" + identificador + '\'' +
                ", tipo='" + tipo + '\'' +
                ", categoria='" + category + '\'' +
                ", escopo='" + scope + '\'' +
                ", parametros=" + parametros +
                ", linha=" + linha +
                ", coluna=" + coluna +
                '}';
    }

    // Getters e Setters
    public String getIdentificador() { return identificador; }
    public String getTipo() { return tipo; }
    public String getCategory() { return category; }
    public String getScope() { return scope; }
    public Object getParametros() { return parametros; }
    public int getLinha() { return linha; }
    public int getId() { return id; }
    public int getColuna() { return coluna; }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setParametros(Object value) {
        this.parametros = value;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
}