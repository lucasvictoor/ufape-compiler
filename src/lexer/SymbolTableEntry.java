package lexer;

public class SymbolTableEntry {
    private String name;
    private String type;
    private String category;
    private String scope; 
    private Object value;

    public SymbolTableEntry(String name, String type, String category, String scope, Object value) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.scope = scope;
        this.value = value;
    }

    // Getters e Setters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getScope() { return scope; }
    public Object getValue() { return value; }

    @Override
    public String toString() {
        return "SymbolTableEntry{" +
               "name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", category='" + category + '\'' +
               ", scope='" + scope + '\'' +
               ", value=" + value +
               '}';
    }
}