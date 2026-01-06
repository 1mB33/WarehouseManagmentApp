package PojazdyOsobowe.Warehouse.GUI.Controllers;

import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import PojazdyOsobowe.Warehouse.GUI.Views.*;
import PojazdyOsobowe.Warehouse.GUI.Models.*;
import PojazdyOsobowe.Warehouse.Products.*;

/**
 * Kontroluje formularz do tworzenia obiektow typu {@link Product}
 *
 */
public class ProductFormController 
{
    public ProductFormController()
    {
        this.view   = new ProductFormView();
        this.model  = new ProductCreator();
    }
    
// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public JComponent getComponent()
    { return this.view.getComponent(); }

    public List<String> getParametersNames()
    { return this.model.getParametersNames(); }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Ustawia formularz pod dana klase
     *
     * @param c Klasa ktora chcemy ustawic
     */
    public void setUpFormForClass(Class<?> c)
    { 
        this.model.loadClass(c);
        this.view.setForm(this.model.getParametersNames()); 
    }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
  
    /**
     * Wczytuje obiekt do formularza
     *
     * @param o Wczytywany obiekt
     * @throws IllegalArgumentException Jesli obiekt nie jest tej samej klasy pod ktora ustawiony jest formularz
     */
    public void loadObject(Object o)
        throws IllegalArgumentException
    {
        if (o.getClass() != this.model.getTargetedClass()) {
            throw new IllegalArgumentException("Object isn't the same class as targeted class");
        }

        this.view.setFields(this.model.getObjectValues(o));
    }

    /**
     * Probuje stworzyc nowy obiekt z danych zamieszczonych w formularzu
     *
     * @return Nowo stworozny obiekt
     * @throws Exception Jesli cokolwiek pojdzie nie tak
     */
    public Product tryToCreateClass()
        throws Exception
    {
        return (Product)this.model.tryToCreatoFromParams(this.view.getProductParameters(), 
                                                         this.view.getCustomParameters());
    }

    public Product tryToCreateCustomMaps(HashMap<String, String> productParams, HashMap<String, String> customParams)
        throws Exception
    { return (Product)this.model.tryToCreatoFromParams(productParams, customParams); }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private ProductFormView     view;
    private ProductCreator      model;
    
}
