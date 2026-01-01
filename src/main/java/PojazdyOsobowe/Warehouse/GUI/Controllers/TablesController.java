package PojazdyOsobowe.Warehouse.GUI.Controllers;

import java.util.HashMap;

import javax.swing.*;

import PojazdyOsobowe.Warehouse.GUI.Views.TablesView;

/**
 * Odpowiada za wyswietlanie tabeli, i kontrole z listy ktora tabela aktualnie ma byc wyswietlana
 *
 */
public class TablesController <STORABLE_TYPE extends Cloneable>
{
    public TablesController()
    {
        this.view   = new TablesView();
        this.tables = new HashMap<>();
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.view.getComponent(); }

    /**
     * @return Aktywnie wyswietlana tabela
     */
    public final DatabaseController<STORABLE_TYPE> getActiveTable()
    { return this.tables.get(this.view.getActiveTableName()); }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    /**
     * @param table Tabela ktora checemy dodac do wyswietlania
     * @param tableName Nazwa tabeli
     */
    public void addTable(DatabaseController<STORABLE_TYPE> table, final String tableName)
    {
        if (!this.tables.values()
                        .stream()
                        .filter(t -> t.equals(table))
                        .toList()
                        .isEmpty()) 
        {
            return;
        }
        
        view.addTable(table.getComponent(), tableName);

        if (this.tables.isEmpty()) 
            this.view.setActivePanel(tableName);

        this.tables.put(tableName, table);
    }

    /**
     * Przeladowywuje kazda z tabel
     */
    public void reloadTables()
    { tables.forEach((s, t) -> t.reload()); }

    /**
     * Odswieza kazda z tabel
     */
    public void refreshTables()
    { tables.forEach((s, t) -> t.refresh()); }

    /**
     * Przeladowywuje tylko tabele ktore obsulguja dana klase
     *
     * @param c Klasa dla ktorej chcemy przeladowac tabele
     */
    public void reloadTablesThatHandles(Class<?> c) 
    {
        tables.forEach((s, t) -> {
            if (t.getHandledClass() == c) {
                t.reload();
            }
        });
    }

    /**
     * Usuwa wszystkie tabele z listy aktualnie obslugiwanych tabel
     */
    public void clear()
    { 
        this.tables.clear(); 
        this.view.clear();
    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
     
    private TablesView                                          view;
    private HashMap<String, DatabaseController<STORABLE_TYPE>>  tables;

}
