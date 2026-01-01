package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Product;

public class ModelComparator implements Comparator<Product> 
{
    @Override
    public int compare(Product a, Product  b) 
    { return a.getModel().compareTo(b.getModel()); }
}
