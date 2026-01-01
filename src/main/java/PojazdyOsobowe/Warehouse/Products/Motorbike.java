package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

public class Motorbike extends Vehicle
{
    public Motorbike()
    {
        super();

        this.setCategory("Motorbike");

        this.bikeType = "UNKNOWN";
    }

    @IProductConstructor()
    public Motorbike(final Product p)
    {
        super(p);

        this.setCategory("Used Car");

        this.bikeType = "UNKNOWN";
    }


    public Motorbike(final GregorianDate     reciptDate,
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
                     final String            bikeType)
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

        this.setCategory("Motorbike");

        this.bikeType = new String(bikeType);
    }

    /**
     * @param other Motor do skopiowania
     */
    public Motorbike(final Motorbike other)
    {
        super(other);

        this.setCategory("Motorbike");

        this.bikeType = new String(other.bikeType);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    public String getBikeType()
    { return this.bikeType; } 

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    public void setBikeType(String bikeType)
    { this.bikeType = bikeType; } 

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       Car: [" +
                                    "typMotocyklu: " + this.bikeType +
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

        Motorbike m = (Motorbike)other;

        if (this.bikeType.equals(m.bikeType))
        {
            return super.equals(other);
        }
    
        return false;
    }

    @Override
    public Motorbike clone()
    { return new Motorbike(this); } 

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 0)
    private String bikeType;

}

