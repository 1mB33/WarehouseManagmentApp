package PojazdyOsobowe.CsvManager;

/**
 * Wyjatek wywolywany kiedy wystepuje blad spowodowany przez plik CSV
 *
 */
public class CsvException extends Exception
{
    public CsvException()
    { super(); }

    public CsvException(final String what)
    { super(what); }

    public CsvException(final Exception e)
    { super(e); }

    public CsvException(final String what, Exception e)
    { super(what, e); }
}
