package PojazdyOsobowe.Date;

/**
 * Zwracane kiedy zinterpretowana data nie robi sensu
 *
 */
public class DateLogicException extends Exception
{
    public DateLogicException()
    { super(); }

    public DateLogicException(final String what)
    { super(what); }

    public DateLogicException(final Exception e)
    { super(e); }

    public DateLogicException(final String what, Exception e)
    { super(what, e); }
}
