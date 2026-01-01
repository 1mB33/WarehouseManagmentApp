package PojazdyOsobowe.Date;

/**
 * Data wedlug systemu uproszczonego 
 * 30 dni w kazdym miesiacu
 * Ilosc dni w roku to 12 * 30 dni
 *
 */
public class SimplifiedDate extends Date
{
    public SimplifiedDate()
    { super(new SimpleResolver()); }

    public SimplifiedDate(final int day, final int month, final int year)
    { super(new SimpleResolver(), day, month, year); }

    public SimplifiedDate(final String date, final EDateFormat[] fmt, final char delimiter)
        throws DateParsingException
             , DateLogicException
    { super(new SimpleResolver(), date, fmt, delimiter); }

    public SimplifiedDate(SimplifiedDate other)
    { super(other); }
}
