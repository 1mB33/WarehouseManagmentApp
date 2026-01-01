package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Product;

public class ManufacturerComparator implements Comparator<Product> 
{
    @Override
    public int compare(Product a, Product  b) 
    { return a.getManufacturer().compareTo(b.getManufacturer()); }
}

