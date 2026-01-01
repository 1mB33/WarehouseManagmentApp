package PojazdyOsobowe.Warehouse.GUI.Models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import PojazdyOsobowe.Warehouse.Products.Cargo;
import PojazdyOsobowe.CsvManager.CsvException;
import PojazdyOsobowe.CsvManager.CsvFactory;
import PojazdyOsobowe.CsvManager.ObjectConstructionException;

/**
 * Przechowuje obiekty, wczytuje je z plikow w formacie csv i zapisuje do pliku w tym samym formacie
 *
 */
public class Database <STORABLE_TYPE extends Cloneable> implements Comparable<Database<?>>
{
    public Database()  
    { 
        this.products   = new ArrayList<>(); 
        this.delimiter  = ';';
    }

    public Database(final int size)  
    {
        this.products   = new ArrayList<>(size); 
        this.delimiter  = ';';
    }

    @SuppressWarnings("all")
    public Database(final Database<? extends STORABLE_TYPE> other)
    {
        this.products   = new ArrayList<>(); 
        this.delimiter  = ';';

        for (STORABLE_TYPE p : other.products)
            try {
                this.addProduct((STORABLE_TYPE)p.getClass().getMethod("clone").invoke(p));
            } catch (Exception e) {
                System.err.println("This shouldn't throw because PRODUCT extends Cloneable");
            }
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    /**
     * @return Liste wszystkich przedmiotow w bazie danych
     */
    public final List<STORABLE_TYPE> getList()
    { return this.products; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("all")
    public void loadFile(final String path)
        throws FileNotFoundException
    {
        BufferedReader br = null; 

        try {
            br = new BufferedReader(new FileReader(path));

            String l = null;
            while ((l = br.readLine()) != null)
            {
                STORABLE_TYPE n = null;

                try {
                    n = (STORABLE_TYPE)CsvFactory.fromCsvRow(l, this.delimiter);
                }
                catch (CsvException e) {
                    System.err.println(e);
                    continue;
                }
                catch (ObjectConstructionException e) {
                    System.err.println(e);
                    continue;
                }

                if (!(n instanceof Cargo))
                    continue;

                this.addProduct(n);
            }

            this.products.sort(null);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
            System.err.println(e);
        }

        if (br != null) 
            try {
                br.close();
            }
            catch (IOException ee) {
                System.err.println(ee);
            }
    }

    public void saveFile(final String path)
    {
        BufferedWriter bw = null; 

        try {
            bw = new BufferedWriter(new FileWriter(path));

            for (STORABLE_TYPE c : this.products)
                try {
                    bw.write(CsvFactory.toCsvRow(c, this.delimiter) + '\n');
                } catch (ObjectConstructionException e) {
                    e.printStackTrace();
                }
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
        }
        catch (IOException e) {
            System.err.println(e);
        }

        if (bw != null) 
            try {
                bw.close();
            }
            catch (IOException ee) {
                System.err.println(ee);
            }
    }

    /**
     * @param p Produkt ktory dodajemy do bazy
     */
    public void addProduct(final STORABLE_TYPE p)
    { this.products.add(p); }

    /**
     * @param p Produkt ktory usuwamy z bazy
     */
    public void removeProduct(STORABLE_TYPE p)
    { this.products.remove(p); }

    /**
     * @return Wielkosc bazy
     */
    public int size()
    { return this.products.size(); }

    /**
     * @return String ktory zawiera wszystkie obiekty z bazy, kazdy odzielony new line
     */
    public String stringifyContent()
    {
        return this.products.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("\n"));
    }

    /**
     * @return String ktory zawiera wszystkie obiekty z bazy, kazdy odzielony new line
     */
    @Override
    public String toString()
    { return this.stringifyContent(); }

    @Override
    public boolean equals(final Object other)
    { 
        if (this == other) 
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass()) 
            return false;

        return this.products.equals(((Database<?>)other).products);
    }

    @Override
    public int compareTo(final Database<?> other)
    { return this.products.size() - other.products.size(); }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private List<STORABLE_TYPE> products;
    private final char delimiter;

}

