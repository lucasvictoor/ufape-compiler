package lexer;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymbolTableEntry> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    public void addEntry(String name, String type, String category, String scope, Object value) {
        if (table.containsKey(name)) {
            throw new RuntimeException("Erro: Identificador '" + name + "' já declarado no escopo " + scope);
        }
        table.put(name, new SymbolTableEntry(name, type, category, scope, value));
    }

    public SymbolTableEntry getEntry(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tabela de Simbolos:\n");
        for (SymbolTableEntry entry : table.values()) {
            sb.append(entry).append("\n");
        }
        return sb.toString();
    }
}