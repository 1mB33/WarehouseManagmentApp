package PojazdyOsobowe.Warehouse.Products;

import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.CsvManager.ICsvField;

public class Product extends Cargo 
{
    public Product()
    {
        super();

        this.reciptDate = new GregorianDate();
        this.price      = -1;
        this.amount     = -1;

        this.category           = "UNKNOWN";
        this.manufacturer       = "UNKNOWN";
        this.model              = "UNKNOWN";
        this.manufacturingDate  = new GregorianDate();
    }

    public Product(final String            category,
                   final GregorianDate     reciptDate,
                   final double            price,
                   final int               amount,
                   final String            manufacturer,
                   final String            model,
                   final GregorianDate     manufacturingDate)
    {
        super((manufacturer +
               "_" + model + 
               "_" + manufacturingDate.toString())
                    .replaceAll("[\\.\\-]", "")
                    .replaceAll("[\\s]", "_"));

        this.reciptDate = new GregorianDate(reciptDate);
        this.price      = price;
        this.amount     = 0;

        this.increaseAmount(amount);

        this.category           = new String(category);
        this.manufacturer       = new String(manufacturer);
        this.model              = new String(model);
        this.manufacturingDate  = new GregorianDate(manufacturingDate);
    }

    /**
     * @param other Produkt ktory chcemy skopiowac
     */
    public Product(final Product other) 
    {
        super(other);
        
        this.reciptDate         = new GregorianDate(other.reciptDate);
        this.price              = other.price;
        this.amount             = other.amount;

        this.category           = other.category;
        this.manufacturer       = other.manufacturer;
        this.model              = other.model;
        this.manufacturingDate  = new GregorianDate(other.manufacturingDate);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    /**
     * @return data przyjecia towaru
     */
    public GregorianDate getReciptDate()
    { return this.reciptDate; }

    /**
     * @return cena towaru
     */
    public double getPrice()
    { return this.price; }

    /**
     * @return ilosc towaru
     */
    public int getAmount()
    { return this.amount; }
    
    /**
     * @return Kategoria produktu
     */
    public String getCategory()
    { return this.category; }

    public String getManufacturer()
    { return this.manufacturer; }

    public String getModel()
    { return this.model; }

    public GregorianDate getManufacturingDate()
    { return this.manufacturingDate; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param price cena ktora chcemy ustawic
     */
    public void setPrice(final double price)
    { this.price = price; }

    /**
     * @param amount ilosc ktora chcemy ustawic
     */
    public void setAmount(final int amount)
    { this.amount = amount; }


    public void setCategory(final String category)
    { this.category = new String(category); }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Dodaje ilosc towaru, nie pozwala przekroczyc wartosci maskymalnej 
     * z {@link PojazdyOsobowe.Warehouse.Products.CargoLimits} i zera
     *
     * @param amount ilosc towaru do dodania 
     * @throws ArithmeticException jesli nastepuje przepelnienie
     */
    public void increaseAmount(final int amount) 
        throws ArithmeticException
    {
        // We are adding a positive number
        if (amount > 0 && 
        // We overflowed
            this.amount > CargoLimits.MaxAmount - amount) 
        {
            throw new ArithmeticException("Couldn't increase the amount above the CargoLimits.MaxAmount limit");
        }

        if (amount < 0 &&
            -amount > this.amount) 
        {
            this.amount = 0;
            return;
        }

        this.amount += amount;
    }

    /**
     * Odejmuej ilosc towaru, nie pozwala przekroczyc wartosci maskymalnej 
     * z {@link PojazdyOsobowe.Warehouse.Products.CargoLimits} i zera
     *
     * @param amount ilosc towaru do odjecia 
     * @throws ArithmeticException jesli nastepuje przepelnienie
     */
    public void decreaseAmount(final int amount) 
        throws ArithmeticException
    { this.increaseAmount(-amount); }

    @Override
    public String toString()
    {
        return super.toString() + 
               ":\n       Produkt: [" +
                                    "kategoria: " + this.category +
                                    ", " +
                                    "producent: " + this.manufacturer +
                                    ", " +
                                    "model: " + this.model +
                                    ", " +
                                    "dataProduckcji: " + this.manufacturingDate +
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

        Product p = (Product)other;
        if (this.category.equals(p.category) && 
            this.manufacturer.equals(p.manufacturer) &&
            this.model.equals(p.model) &&
            this.manufacturingDate.equals(p.manufacturingDate) &&
            this.getPrice() == p.getPrice() &&
            this.getReciptDate().equals(p.getReciptDate())) 
        {
            return true;
        }

        return false;
    }

    @Override
    public int compareTo(final Cargo other)
    { 
        if (!(other instanceof Product))
            return super.compareTo(other);

        Product otherProduct = (Product)other;

        int result;

        result = this.getReciptDate().compareTo(otherProduct.getReciptDate());
        if (result != 0) 
            return result;

        result = (int)((this.getPrice() - otherProduct.getPrice()) * 100.0);
        if (result != 0) 
            return result;

        result = this.getAmount() - otherProduct.getAmount();
        if (result != 0) 
            return result;

        return super.compareTo(other);
    }

    @Override
    public Product clone()
    { return new Product(this); }

// Members // ---------------------------------------------------------------------------------------------------------

    @ICsvField(column = 6)
    private String category;
    @ICsvField(column = 3, canModify = false)
    private final String manufacturer;
    @ICsvField(column = 4, canModify = false)
    private final String model;
    @ICsvField(column = 5, canModify = false)
    private final GregorianDate manufacturingDate; 
    @ICsvField(column = 0, canModify = false)
    private final GregorianDate reciptDate;
    @ICsvField(column = 1)
    private double price;
    @ICsvField(column = 2)
    private int amount;

}
