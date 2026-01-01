package PojazdyOsobowe.Warehouse.GUI.Controllers;

import javax.swing.*;

import PojazdyOsobowe.Warehouse.GUI.Views.*;
import PojazdyOsobowe.Warehouse.Products.*;

/**
 * Kontroluje element GUI, ktory odpowiada za stworzenie obiektu, ktory moze byc
 * przechowywany w bazie danych.
 *
 */
public class CreatorController 
{
    public CreatorController()
    {
        this.creator    = new ProductFormController();
        this.view       = new CreatorView(this.creator.getComponent());
    }
    
// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public JComponent getComponent()
    { return this.view.getComponent(); }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void setFormClass(Class<?> c) 
    { this.creator.setUpFormForClass(c); }

    public void setForm(Object o) 
    { this.creator.setUpFormForClass(o.getClass()); }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public void addForm(final String formName, final Class<?> forClass) 
    { this.view.addToProductSelector(formName, () -> this.creator.setUpFormForClass(forClass)); }

    public void loadItem(Product item)
    { this.creator.loadObject(item); }

    public Product tryToCreate()
        throws Exception
    { return this.creator.tryToCreateClass(); }
    
// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private CreatorView             view;
    private ProductFormController   creator;

}
