package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

public class Car extends Vehicle
{
    public Car()
    {
        super();

        this.setCategory("Car");

        this.doorCount  = -1;
        this.carType    = "UNKNOWN";
    }

    @IProductConstructor()
    public Car(final Product p)
    {
        super(p);

        this.setCategory("Car");

        this.doorCount  = -1;
        this.carType    = "UNKNOWN";
    }

    public Car(final GregorianDate     reciptDate,
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
               final String            carType)
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

        this.setCategory("Car");

        this.doorCount  = doorCount;
        this.carType    = new String(carType);
    }

    /**
     * @param other Samochod do skopiowania
     */
    public Car(final Car other) 
    {
        super(other);

        this.setCategory("Car");

        this.doorCount  = other.doorCount;
        this.carType    = new String(other.carType);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @return Ilosc drzwi w samochodzie
     */
    public int getDoorCount()
    { return this.doorCount; }

    /**
     * @return Typ samochodu 
     */
    public String getCarType()
    { return this.carType; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param doorCount Ilosc drzwi ktora checemy ustawic
     */
    public void setDoorCount(final int doorCount)
    { this.doorCount = doorCount; }

    /**
     * @param carType Typ samochodu ktory chemy ustawic
     */
    public void setCarType(final String carType)
    { this.carType = carType; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       Car: [" +
                                    "iloscDrzwi: " + this.doorCount +
                                    ", " +
                                    "typSamochodu: " + this.carType +
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

        Car c = (Car)other;

        if (this.doorCount == c.doorCount &&
            this.carType.equals(c.carType))
        {
            return super.equals(other);
        }
    
        return false;
    }

    @Override
    public Car clone()
    { return new Car(this); }

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 0)
    private int doorCount;
    @ICsvField(column = 1)
    private String carType;

}
