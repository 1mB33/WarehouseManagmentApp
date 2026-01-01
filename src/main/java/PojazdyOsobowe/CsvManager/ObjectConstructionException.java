package PojazdyOsobowe.CsvManager;

/**
 * Opisuje wyjatki zwiazane z tworzeniem obiektu, podczas czytania wiersza csv
 *
 */
public class ObjectConstructionException extends Exception
{
    public ObjectConstructionException()
    { super(); }

    public ObjectConstructionException(final String what)
    { super(what); }

    public ObjectConstructionException(final Exception e)
    { super(e); }

    public ObjectConstructionException(final String what, Exception e)
    { super(what, e); }
}
