package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Product;

public class PriceComparator implements Comparator<Product> 
{
    @Override
    public int compare(Product a, Product  b) 
    { return Double.compare(a.getPrice(), b.getPrice()); }
}

