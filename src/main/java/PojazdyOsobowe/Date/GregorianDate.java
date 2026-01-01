package PojazdyOsobowe.Date;

/**
 * Data wedlug systemu gregorianskiego
 *
 */
public class GregorianDate extends Date
{
    public GregorianDate()
    { super(new GregorianResolver()); }

    public GregorianDate(final int day, final int month, final int year)
    { super(new GregorianResolver(), day, month, year); }

    public GregorianDate(final String date, final EDateFormat[] fmt, final char delimiter)
        throws DateParsingException
             , DateLogicException
    { super(new GregorianResolver(), date, fmt, delimiter); }

    public GregorianDate(GregorianDate other)
    { super(other); }
}
