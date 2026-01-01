package PojazdyOsobowe.CsvManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import PojazdyOsobowe.Warehouse.Products.*;
import PojazdyOsobowe.Date.*;

/**
 * Unit test for Dates.
 */
public class CsvFactoryTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CsvFactoryTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(CsvFactoryTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testCsvLineCreation()
    {
        Car c = new Car(new GregorianDate(20, 10, 2010), 
                        200000,
                        10,
                        "Micubiszi",
                        "sx4",
                        new GregorianDate(20, 10, 2009),
                        "hybryda",
                        1000,
                        400,
                        10000,
                        "fiolkowy",
                        "premium+",
                        7,
                        "suv");

        try {
            CsvFactory.toCsvRow(c, ';');
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(false);
        }
    }

    public void testObjectCreation()
    {
        Car c = new Car(new GregorianDate(20, 10, 2010), 
                        200000,
                        10,
                        "Micubiszi",
                        "sx4",
                        new GregorianDate(20, 10, 2009),
                        "hybryda",
                        1000,
                        400,
                        10000,
                        "fiolkowy",
                        "premium+",
                        7,
                        "suv");

        String line = new String();
        try {
            line = CsvFactory.toCsvRow(c, ';');
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(false);
        }
    
        Product p = null;
        try {
            p = (Product)CsvFactory.fromCsvRow(line, ';');
        } catch (Exception e) {
            System.out.println(e);
            assertTrue(false);
        }
        
        assertTrue(c.getCategory().equals(p.getCategory()));
        assertTrue(c.getManufacturer().equals(p.getManufacturer()));
        assertTrue(c.getModel().equals(p.getModel()));
        assertTrue(c.getReciptDate().equals(p.getReciptDate()));
    }
}


