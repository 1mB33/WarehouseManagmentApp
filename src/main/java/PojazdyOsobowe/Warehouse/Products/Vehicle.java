package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.CsvManager.ICsvField;

public class Vehicle extends Product
{
    public Vehicle()
    {
        super();

        this.engineType     = "UNKNOWN";
        this.horsePower     = -1.;
        this.topSpeed       = -1.;
        this.torque         = -1.;
        this.color          = "UNKNOWN";
        this.features       = "UNKNOWN";
    }

    @IProductConstructor()
    public Vehicle(final Product p) 
    {
        super(p);

        this.setCategory("Vehicle");

        this.engineType     = "UNKNOWN";
        this.horsePower     = -1.;
        this.topSpeed       = -1.;
        this.torque         = -1.;
        this.color          = "UNKNOWN";
        this.features       = "UNKNOWN";
    }

    public Vehicle(final GregorianDate     reciptDate,
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
                   final String            features) 
    {
        super("Vehicle",
              reciptDate,
              price,
              amount,
              manufacturer,
              model,
              manufacturingDate);

        this.engineType     = new String(engineType);
        this.horsePower     = horsePower;
        this.topSpeed       = topSpeed;
        this.torque         = torque;
        this.color          = new String(color);
        this.features       = new String(features);
    }

    /**
     * @param other Pojazd do skopiowania
     */
    public Vehicle(final Vehicle other)
    {
        super(other);

        this.engineType     = new String(other.engineType);
        this.horsePower     = other.horsePower;
        this.topSpeed       = other.topSpeed;
        this.torque         = other.torque;
        this.color          = new String(other.color);
        this.features       = new String(other.features);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    public String getEngineType()
    { return this.engineType; }

// --------------------------------------------------------------------------------------------------------------------
    public double getHorsePower()
    { return this.horsePower; }

// --------------------------------------------------------------------------------------------------------------------
    public double getTopSpeed()
    { return this.topSpeed; }

// --------------------------------------------------------------------------------------------------------------------
    public double getTorque()
    { return this.torque; }

// --------------------------------------------------------------------------------------------------------------------
    public String getColor()
    { return this.color; }

// --------------------------------------------------------------------------------------------------------------------
    public String getFeatures()
    { return this.features; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    public void setHorsePower(double horsePower)
    { this.horsePower = horsePower; }

// --------------------------------------------------------------------------------------------------------------------
    public void setTopSpeed(double topSpeed)
    { this.topSpeed = topSpeed; }

// --------------------------------------------------------------------------------------------------------------------
    public void setTorque(double torque)
    { this.torque = torque; }

// --------------------------------------------------------------------------------------------------------------------
    public void setColor(String color)
    { this.color = color; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       Pojazd: [" +
                                    "typSilnika: " + this.engineType +
                                    ", " +
                                    "iloscKoniMechanicznych: " + this.horsePower +
                                    ", " +
                                    "predkoscMaksymalna: " + this.topSpeed +
                                    ", " +
                                    "momentObrotowy: " + this.torque +
                                    ", " +
                                    "kolor: " + this.color +
                                    ", " +
                                    "wyposarzenie: " + this.features +
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

        Vehicle v = (Vehicle)other;

        if (this.engineType.equals(v.engineType) &&
            this.horsePower == v.horsePower &&
            this.topSpeed == v.topSpeed &&
            this.torque == v.torque &&
            this.color.equals(v.color) &&
            this.features.equals(v.features))
        {
            return super.equals(other);
        }
    
        return false;
    }

    @Override
    public Vehicle clone()
    { return new Vehicle(this); }

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 1)
    private String engineType;
    @ICsvField(column = 2)
    private double horsePower;
    @ICsvField(column = 3)
    private double topSpeed;
    @ICsvField(column = 4)
    private double torque;
    @ICsvField(column = 5)
    private String color;
    @ICsvField(column = 6)
    private String features;

}
