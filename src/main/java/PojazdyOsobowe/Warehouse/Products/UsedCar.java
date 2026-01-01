package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

public class UsedCar extends Car
{
    public UsedCar()
    {
        super();

        this.setCategory("Used Car");

        this.carCondition = "UNKNOWN";
    }

    @IProductConstructor()
    public UsedCar(final Product p)
    {
        super(p);

        this.setCategory("Used Car");

        this.carCondition = "UNKNOWN";
    }

    public UsedCar(final GregorianDate     reciptDate,
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
                   final int               doorCount,
                   final String            carType,
                   final String            carCondition)
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
              features,
              doorCount,
              carType);

        this.setCategory("Used Car");

        this.carCondition = carCondition;
    }

    /**
     * @param other Samochod uzywany do skopiowania do skopiowania
     */
    public UsedCar(final UsedCar other)
    {
        super(other);

        this.setCategory("Used Car");

        this.carCondition = new String(other.carCondition);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
  
    public String getCarCondition()
    { return this.carCondition; } 

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void setCarCondition(String carCondition)
    { this.carCondition = carCondition; } 

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       UsedCar: [" +
                                    "stanPojazdu: " + this.carCondition +
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

        UsedCar uc = (UsedCar)other;

        if (this.carCondition.equals(uc.carCondition))
        {
            return super.equals(other);
        }
    
        return false;
    }

    @Override
    public UsedCar clone()
    { return new UsedCar(this); } 

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 0)
    private String carCondition;

}
