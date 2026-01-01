package PojazdyOsobowe.Date;

/**
 * Zwracane kiedy przerabianie daty z String nie powiodlo sie
 *
 */
public class DateParsingException extends Exception
{
    public DateParsingException()
    { super(); }

    public DateParsingException(final String what)
    { super(what); }

    public DateParsingException(final Exception e)
    { super(e); }

    public DateParsingException(final String what, Exception e)
    { super(what, e); }
}
