package PojazdyOsobowe.Warehouse.GUI.Controllers;

import javax.swing.*;
import java.util.Optional;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

import PojazdyOsobowe.Warehouse.Products.Product;

import PojazdyOsobowe.Warehouse.GUI.Views.DatabaseView;
import PojazdyOsobowe.Warehouse.GUI.Models.Database;

/**
 * Kontroluje baze danych i jej wyswietlanie
 *
 */
public class DatabaseController <STORABLE_TYPE extends Cloneable>
{
    public DatabaseController()
    { this(null); }

    public DatabaseController(Class<?> filteredClass)
    {
        this.model          = new Database<>();
        this.filteredClass  = filteredClass;
        this.view           = new DatabaseView<>(this.model, this.filteredClass);
    }

    public DatabaseController(Database<STORABLE_TYPE> db, Class<?> filteredClass)
        throws ClassCastException
    {
        this.model          = db;
        this.filteredClass  = filteredClass;
        this.view           = new DatabaseView<>(this.model, this.filteredClass);
    }

    public DatabaseController(DatabaseController<STORABLE_TYPE> other, Class<?> filteredClass)
        throws ClassCastException
    {
        this.model          = other.model;
        this.filteredClass  = filteredClass;
        this.view           = new DatabaseView<STORABLE_TYPE>(other.view, this.filteredClass);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.view.getComponent(); }

    /**
     * @return Bazdanych obslugiwana przez kontroler
     */
    public Database<STORABLE_TYPE> getDatabase()
    { return this.model; }

    /**
     * @return Wszystkie zaznaczone obiekty
     */
    public List<STORABLE_TYPE> getSelected()
    { return this.view.getSelected(); }

    /**
     * @return Klasa ktora jest widoczna w tabeli
     */
    public Class<?> getHandledClass()
    { return this.filteredClass; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void add(final STORABLE_TYPE p)
    {
        model.addProduct(p);
        view.addProduct(p);
    }

    public void remove(STORABLE_TYPE p)
    {
        model.removeProduct(p);
        view.removeProduct(p);
    }

    public void increaseAmount(final STORABLE_TYPE p, final int byAmount)
    {
        Optional<STORABLE_TYPE> item = model.getList().stream()
                                      .filter(product -> product.equals(p))
                                      .findFirst();

        if (item.isEmpty()) 
            return;

        ((Product)item.get()).increaseAmount(byAmount);

        if (((Product)item.get()).getAmount() <= 0) {
            this.remove((STORABLE_TYPE)item.get());
        }

        this.view.refresh();
    }

    public void decreaseAmount(STORABLE_TYPE p, int byAmount)
    { this.increaseAmount(p, -byAmount); }

    public void save(final String path)
    { model.saveFile(path); }

    public void scrollDown()
    { this.view.scrollDown(); }

    public void select(Object o)
    { this.view.select(o); }

    public void loadDatabaseFromFile(final String path)
        throws FileNotFoundException
    {
        this.model.loadFile(path);
        this.view = new DatabaseView<>(this.model, this.filteredClass);
    }

    public void reload()
    { this.view.reloadDatabase(); }

    public void refresh()
    { this.view.refresh(); }

    @SuppressWarnings("all")
    public void addComparator(final String fieldName, Class<?> forClass, Comparator comparator)
    { this.view.addComparator(fieldName, forClass, comparator); }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private Database<STORABLE_TYPE>         model;
    private DatabaseView<STORABLE_TYPE>     view;
    private Class<?>                        filteredClass;

}
