package PojazdyOsobowe.Utils;

public class ObjectIsNullException extends Exception
{
    public ObjectIsNullException()
    { super(); }

    public ObjectIsNullException(final String what)
    { super(what); }

    public ObjectIsNullException(final Exception e)
    { super(e); }

    public ObjectIsNullException(final String what, Exception e)
    { super(what, e); }
}
