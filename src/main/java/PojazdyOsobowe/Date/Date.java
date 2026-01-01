package PojazdyOsobowe.Date;

import java.util.Comparator;

/**
 * Klasa daty, ktora moze implementowac rozne rodzaje interpretacji daty
 *
 */
public class Date implements Comparable<Date>
                           , Comparator<Date>
{
    /**
     * Tworzy date ktora uzywa uproszczonej wersji interpretacji daty
     */
    public Date() 
    { 
        this.resolver   = new SimpleResolver();
        this.days       = 1;
    }

    /**
     * @param other Data ktora kopiujemy
     */
    public Date(final Date other)
    { 
        this.resolver   = other.resolver;
        this.days       = other.days;
    }

    /**
     * @param dri Uzywany sposob interpretacji daty
     */
    public Date(IDateResolver dri)
    {
        this.resolver   = dri;
        this.days       = 1;
    }

    /**
     * @param dri Uzywany sposob interpretacji daty
     * @param day Dzien, ktory ustawiamy
     * @param month Miesac, ktory ustawiamy
     * @param year Rok, ktory ustawiamy
     */
    public Date(IDateResolver dri, final int day, final int month, final int year)
    {
        this.resolver = dri;

        DateContainer container = new DateContainer(day, month, year);
        this.days = this.resolver.resolveContainer(container, 0).inDays(container);
    }

    /**
     * @param dri Uzywany sposob interpretacji daty
     * @param date String zawierajacy date
     * @param fmt Format ktory stosuje String
     * @param delimiter Znak ktory symbolizuje odstep miedzy polami
     * @throws DateParsingException Kiedy String zawiera niepoprawne dane
     * @throws DateLogicException Kiedy podana dana nie robi sensu w wybranym sposobie interpretacji daty
     */
    public Date(IDateResolver dri, final String date, final EDateFormat[] fmt, final char delimiter)
        throws DateParsingException
             , DateLogicException
    {
        this(dri);
        parse(date, fmt, delimiter);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @return Aktualny dzien
     */
    public long getDay() 
    { return resolveDatePartAndGetInternal(EDateFormat.Day); }

    /**
     * @return Aktualny miesiac
     */
    public long getMonth() 
    { return resolveDatePartAndGetInternal(EDateFormat.Month); }

    /**
     * @return Aktualny rok
     */
    public long getYear() 
    { return resolveDatePartAndGetInternal(EDateFormat.Year); }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param day Dzien ktory chcemy ustawic w dacie
     */
    public void setDay(final int day)
    { resolveDatePartAndSetInternal(EDateFormat.Day, day); }

    /**
     * @param month Miesiac ktory chcemy ustawic w dacie
     */
    public void setMonth(final int month)
    { resolveDatePartAndSetInternal(EDateFormat.Month, month); }

    /**
     * @param year Rok ktory chcemy ustawic w dacie
     */
    public void setYear(final int year)
    { resolveDatePartAndSetInternal(EDateFormat.Year, year); }

    /**
     * @param dri Sposob interpretacji daty ktory chcemy uzywac
     */
    public void setResolver(IDateResolver dri)
    { this.resolver = dri; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param days Ilosc dni o ile chcemy przesunac date
     * @return Przesuniata data
     */
    public Date shiftDays(final int days)
    {   
        Date           result       = new Date(this);
        DateContainer  container    = new DateContainer(result.days);

        result.days = result.resolver.resolveContainer(container, days).inDays(container);

        return result;
    }

    /**
     * @return Jutrzejsza data
     */
    public Date calcTomorrow()
    { return this.shiftDays(1); }

    /**
     * @return Wczorajsza date
     */
    public Date calcYesterday()
    { return this.shiftDays(-1); }

    /**
     * @return Data przesunieta o tydzien (7 dni) do przodu
     */
    public Date calcNextWeek()
    { return this.shiftDays(7); }

    /**
     * @return Data przesunieta o tydzien (7 dni) wstecz
     */
    public Date calcPastWeek()
    { return this.shiftDays(-7); }

    /**
     * @return Ilosc dni od daty 01.01.0001
     */
    public int inDays()
    { return this.days; };

    /**
     * Interpretuje String do wartosci daty
     *
     * @param date String zawierajacy date
     * @param fmt Tablica z enumeratorow {@link EDateFormat}, opisujaca format ktory stosuje String
     * @param delimiter Znak ktory symbolizuje odstep miedzy polami
     * @throws DateParsingException Kiedy String zawiera niepoprawne dane
     * @throws DateLogicException Kiedy podana dana nie robi sensu w wybranym sposobie interpretacji daty
     */
    public void parse(final String date, final EDateFormat[] fmt, final char delimiter) 
        throws DateParsingException,
               DateLogicException
    {
        DateContainer   container   = new DateContainer();
        int             start       = 0;
        int             dot         = start;
        int             fmtIndex    = 0;
        String          sanitized   = date.replaceAll("\\s", "");

        while (dot != -1 && 
               fmtIndex < fmt.length && 
               dot < sanitized.length()) 
        {
            dot = sanitized.indexOf(delimiter, start);
            if (dot == -1) 
                dot = sanitized.length();

            container.set(fmt[fmtIndex], Integer.parseInt(sanitized.substring(start, dot)));

            start = dot + 1;
            ++fmtIndex;
        }

        if (fmtIndex != fmt.length && dot != -1)
            throw new DateParsingException("Date string doesn't match the requested format [" + sanitized + "]");

        int partValue;
        EDateFormat partFormat;
        for (int i = container.getSize() - 1; i >= 0; --i) 
        {
            partValue   = container.get(i);
            partFormat  = container.getDateFormat(i);
            if (partValue > 0 && partValue <= this.resolver.getDateFormatLimit(partFormat, container))
                continue;

            throw new DateLogicException("Date string contains invalid values [" + partValue + "] in [" + partFormat + "]");
        }

        this.days = this.resolver.resolveContainer(container, 0).inDays(container);
    }

    /**
     * @param year Rok w ktorym liczymy wielkanoc
     * @return Data wielkanocy w danym roku
     */
    static public Date calcEasterDate(final int year)
    {
        final int calcAsGregrianSince = 1582;

        return year <= calcAsGregrianSince 
            ? calcEasterDateJulianInternal(year) 
            : calcEasterDateGregorianInternal(year);
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
    
        return this.resolver.getClass() == ((Date)other).resolver.getClass() && 
               this.days == ((Date)other).days;
    }

    @Override
    public int compareTo(final Date other)
    { 
        if (this.resolver.getClass() != this.resolver.getClass())
            System.err.println("[Warining]: Uwaga obliczanie roznicy miedzy (" + this + ") i (" + other + "), " +
                                "ktore sa obslugiwane przez rozne systemy datowe");

        return this.days - other.days; 
    }

    @Override
    public int compare(final Date a, final Date b)
    { 
        if (a.resolver.getClass() != b.resolver.getClass())
            System.err.println("[Warining]: Uwaga obliczanie roznicy miedzy (" + a + ") i (" + b + "), " +
                                "ktore sa obslugiwane przez rozne systemy datowe");

        return a.days - b.days; 
    }

    @Override
    public String toString()
    { 
        DateContainer dc = new DateContainer(this.days);
        this.resolver.resolveContainer(dc, 0);
        return dc.toString(); 
    }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    private int resolveDatePartAndGetInternal(final EDateFormat part)
    {
        DateContainer container = new DateContainer(this.days);
        this.resolver.resolveContainer(container, 0);
        return container.get(part); 
    }

// --------------------------------------------------------------------------------------------------------------------
    private void resolveDatePartAndSetInternal(final EDateFormat part, final int value)
    {
        DateContainer container = new DateContainer(this.days);
        this.resolver.resolveContainer(container, 0);
        container.set(part, value);
        this.days = this.resolver.resolveContainer(container, 0).inDays(container);
    }

// --------------------------------------------------------------------------------------------------------------------
    static private Date calcEasterDateJulianInternal(final int year)
    { return calcEasterDateInternal(new GregorianResolver(), year, 15, 6); }

// --------------------------------------------------------------------------------------------------------------------
    static private Date calcEasterDateGregorianInternal(final int year)
    {
        final int[][] AB = { 
            { 22, 2 }, // 0
            { 23, 3 }, // 1
            { 23, 3 }, // 1.5
            { 23, 4 }, // 2
            { 24, 5 }, // 3
            { 24, 5 }, // 3.5
            { 24, 6 }, // 4
            { 25, 0 }, // 5
            { 26, 1 }, // ...
            { 25, 1 },
            { 26, 2 },
            { 27, 3 },
            { 27, 4 },
            { 28, 5 },
        };
        
        return calcEasterDateInternal(new GregorianResolver(),
                                      year, 
                                      AB[year / 100 - 15][0],
                                      AB[year / 100 - 15][1]);
    }

// --------------------------------------------------------------------------------------------------------------------
    static private Date calcEasterDateInternal(IDateResolver dri, final int year, final int A, final int B)
    {
        final int a = year % 19;
        final int b = year % 4;
        final int c = year % 7;
        final int d = (a * 19 + A) % 30;
        final int e = (2 * b + 4 * c + 6 * d + B) % 7;

        if (d == 29 && e == 6)
            return new Date(dri, 19, 4, year);
        if (d == 28 && e == 6 && a > 10)
            return new Date(dri, 18, 4, year);

        return (d + e) < 10 
                ? new Date(dri, 1, 3, year).shiftDays(d + e + 21) 
                : new Date(dri, 1, 4, year).shiftDays(d + e - 10);
    }

// Members // ---------------------------------------------------------------------------------------------------------

    private IDateResolver resolver;
    private int days;

}
