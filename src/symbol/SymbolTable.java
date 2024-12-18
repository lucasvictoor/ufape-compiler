package symbol;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private List<SymbolEntry> entries;

    public SymbolTable() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String nome, String tipo, String escopo, int linha, int coluna) {
        SymbolEntry entry = new SymbolEntry(nome, tipo, escopo, linha, coluna);
        entries.add(entry);
    }

    public void printTable() {
        System.out.println("\nTabela de Simbolos:");
        System.out.printf("%-15s | %-10s | %-10s | %-5s | %-5s\n", 
                "Identificador", "Tipo", "Escopo", "Linha", "Coluna");
        System.out.println("-------------------------------------------------------------");
    
        for (SymbolEntry entry : entries) {
            System.out.printf("%-15s | %-10s | %-10s | %-5d | %-5d\n",
                    entry.getNome(), 
                    entry.getTipo() != null ? entry.getTipo() : "null", 
                    entry.getEscopo(), 
                    entry.getLinha(), 
                    entry.getColuna());
            System.out.println("-------------------------------------------------------------");
        }
    }    

    public List<SymbolEntry> getEntries() {
        return entries;
    }
}
