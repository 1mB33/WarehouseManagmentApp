package PojazdyOsobowe.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Dates.
 */
public class DateTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DateTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(DateTest.class);
    }

    public void testParsing()
    {
        Date d = new Date();
        EDateFormat[] df = {
            EDateFormat.Year,
            EDateFormat.Day,
            EDateFormat.Month
        };

        try {
            d.parse("2000.01.10", df, '.');
            assertTrue(d.getDay() == 1);
            assertTrue(d.getMonth() == 10);
            assertTrue(d.getYear() == 2000);
        }
        catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testSimpleDateToString()
    {
        Date d = new Date();
        assertTrue(d.toString().equals("01.01.0001"));
    }

    public void testGregorianDateToString()
    {
        Date d = new Date(new GregorianResolver());
        assertTrue(d.toString().equals("01.01.0001"));
    }
}

