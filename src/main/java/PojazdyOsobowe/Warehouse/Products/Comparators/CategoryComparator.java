package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Product;

public class CategoryComparator implements Comparator<Product> 
{
    @Override
    public int compare(Product a, Product  b) 
    { return a.getCategory().compareTo(b.getCategory()); }
}

