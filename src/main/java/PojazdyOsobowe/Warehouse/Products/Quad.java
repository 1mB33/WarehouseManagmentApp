package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

public class Quad extends Vehicle
{
    public Quad()
    {
        super();

        this.setCategory("Quad");

        this.terrainType = "UNKNOWN";
    }

    @IProductConstructor()
    public Quad(final Product p)
    {
        super(p);

        this.setCategory("Quad");

        this.terrainType = "UNKNOWN";
    }

    public Quad(final GregorianDate     reciptDate,
                final double            price,
                final int               amount,
                final String            manufacturer,
                final String            model,
                final GregorianDate     manufacturingDate,
                final String            engineType,
                final double            horsePower,
                final double            topSpeed,
                final double            torque,
                final String            color,
                final String            features,
                final String            terrainType)
    {
        super(reciptDate,
              price,
              amount,             
              manufacturer,
              model,
              manufacturingDate,
              engineType,
              horsePower,
              topSpeed,
              torque,
              color,
              features);

        this.setCategory("Quad");

        this.terrainType = terrainType;
    }

    /**
     * @param other Motor do skopiowania
     */
    public Quad(final Quad other)
    {
        super(other);

        this.setCategory("Quad");

        this.terrainType = new String(other.terrainType);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public String getTerrainType()
    { return this.terrainType; } 

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
   
    public void setTerrainType(String terrainType)
    { this.terrainType = terrainType; } 

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       Car: [" +
                                    "typPowierznchni: " + this.terrainType +
               "]";
    }

    @Override
    public boolean equals(final Object other)
    { 
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;

        Quad q = (Quad)other;

        if (this.terrainType.equals(q.terrainType))
        {
            return super.equals(other);
        }
    
        return false;
    }

    @Override
    public Quad clone()
    { return new Quad(this); } 

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 0)
    private String terrainType;

}
