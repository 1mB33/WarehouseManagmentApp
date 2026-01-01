package PojazdyOsobowe.Warehouse.Products.Comparators;

import java.util.Comparator;

import PojazdyOsobowe.Warehouse.Products.Cargo;

public class IdComparator implements Comparator<Cargo> 
{
    @Override
    public int compare(Cargo a, Cargo  b) 
    { return a.getId().compareTo(b.getId()); }
}


