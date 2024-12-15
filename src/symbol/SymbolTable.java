package symbol;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    private List<SymbolTableEntry> table;

    public SymbolTable() {
        this.table = new ArrayList<>();
    }

    public void addEntry(String name, String type, String category, String scope, Object value, int linha, int coluna) {
        table.add(new SymbolTableEntry(name, type, category, scope, value, linha, coluna));
    }

    public SymbolTableEntry getEntry(String name) {
        for (SymbolTableEntry entry : table) {
            if (entry.getIdentificador().equals(name)) {
                return entry;
            }
        }
        return null;
    }

    public boolean contains(String name) {
        for (SymbolTableEntry entry : table) {
            if (entry.getIdentificador().equals(name)) {
                return true;
            }
        }
        return false;
    }


    public List<SymbolTableEntry> getTable() {
        return table;
    }

    public void printTable() {
        System.out.println("Tabela de Simbolos:");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-10s | %-15s | %-5s | %-5s |\n", 
            "ID", "Nome", "Tipo", "Categoria", "Escopo", "Parametros", "Linha", "Coluna");
        System.out.println("-------------------------------------------------------------------------------");
    
        for (SymbolTableEntry entry : table) {
            System.out.println(entry.toString());
        }
        System.out.println("-------------------------------------------------------------------------------");
    }
    
}