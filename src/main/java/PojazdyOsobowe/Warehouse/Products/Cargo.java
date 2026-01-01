package PojazdyOsobowe.Warehouse.Products;

import java.util.Random;

import PojazdyOsobowe.CsvManager.ICsvField;

/**
 * Podstawowy obiekt bazy danych, mozna przetwarzac go do formatu csv, klonowac i porownywac z innymi {@link Cargo}
 */
public class Cargo implements Comparable<Cargo>
                            , Cloneable
{
    /**
     * Podstawowy konstruktor, obiekty stworzone w ten sposob, sa defaltowo w stanie niewlasciwym.
     * Posiadaja niewlasciwy identyfikator.
     */
    public Cargo()
    {
        this.id = "INVALID";
    }

    public Cargo(final String id)
    { 
        this.id = new String(id + this.generateId());
    }

    /**
     * Konstruktor kopiujacy {@link Cargo} 
     *
     * @param other Towar ktory kopiujemy
     */
    public Cargo(final Cargo other)
    {
        this.id = other.id;
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @return identyfikator
     */
    public String getId()
    { return this.id; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    

    @Override
    public boolean equals(final Object other)
    { 
        if (this == other)
            return true;
        if (other == null) 
            return false;
        if (this.getClass() != other.getClass()) 
            return false;

        Cargo c = (Cargo)other;
        return this.id.equals(c.id); 
    }

    @Override
    public int compareTo(final Cargo other)
    { 
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString()
    { return "Towar: [" +
                    "id: " + id + 
                "]"; }

    @Override
    public Cargo clone()
    { return new Cargo(this); }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private String generateId()
    {
        Random random = new Random(System.currentTimeMillis());
        int min = 10000;
        int max = 99999;

        return Integer.toString(random.nextInt(max - min + 1) + min);
    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    @ICsvField(column = 0, canModify = false)
    private final String id;
    
}
