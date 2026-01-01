package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.*;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

/**
 * Minimalny przyklad klasy ktora moze byc obslugiwana przez aplikacje
 *
 */
public class OnionDummyTestClass extends Product
{

    public OnionDummyTestClass()
    {
        super();

        this.setCategory("Food");
    }

    @IProductConstructor()
    public OnionDummyTestClass(final Product p)
    {
        super(p);

        this.setCategory("Food");
    }

    @ICsvField(column = 0)
    private String color;
    @ICsvField(column = 1)
    private String origin;
    @ICsvField(column = 2, canModify = false)
    private GregorianDate bestByDate;
    
}
