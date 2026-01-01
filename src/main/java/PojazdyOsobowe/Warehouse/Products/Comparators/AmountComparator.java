package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Product;

public class AmountComparator implements Comparator<Product> 
{
    @Override
    public int compare(Product a, Product  b) 
    { return Integer.compare(a.getAmount(), b.getAmount()); }
}


