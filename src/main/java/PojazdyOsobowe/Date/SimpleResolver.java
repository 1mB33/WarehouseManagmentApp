package PojazdyOsobowe.Date;

/**
 * Data wedlug systemu uproszczonego 
 * 30 dni w kazdym miesiacu
 * Ilosc dni w roku to 12 * 30 dni
 *
 */
public class SimpleResolver implements IDateResolver
{
    public IDateResolver resolveContainer(DateContainer date, final int days) 
    {
        date.increase(EDateFormat.Day, days);

        if (date.get(EDateFormat.Year) <= 0) {
            date.set(EDateFormat.Year, 1);
        }
        if (date.get(EDateFormat.Month) <= 0) {
            date.set(EDateFormat.Month, 1);
        }

        int partIndex = date.getSize();
        while (--partIndex > 0) {
            fixOverflowInternal(date, partIndex);
        }
        
        return this;
    }

    public int inDays(final DateContainer date)
    {
        int sum = date.get(EDateFormat.Day);
        
        int monthsInDays = (date.get(EDateFormat.Month) - 1) * 30;

        sum += monthsInDays;
        sum += (date.get(EDateFormat.Year) - 1) * 12 * 30;

        return sum;
    }
    
    public int getDateFormatLimit(final EDateFormat part, final DateContainer date)
    {
        switch (part) {
            case Year:
                return Integer.MAX_VALUE;
            case Month:
                return 12;
            case Day:
                return 31;
            default:
                assert false : "LookUpOverflowInternal(), index isn't expceted";
                return 0;
        }
    } 

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private void fixOverflowInternal(DateContainer date, int partIndex)
    {
        final int overflowForPart = lookUpOverflowInternal(date.getDateFormat(partIndex));

        for (;;) {
            int d = date.get(partIndex);

            if (d > 0 && d <= overflowForPart) 
                return;
        
            d = d > 0 ? 1 : -1;
            date.increase(partIndex - 1, d);
            date.increase(partIndex, -overflowForPart * d);
        }
    }

    private int lookUpOverflowInternal(EDateFormat part)
    {
        switch (part) {
            case Year:
                return Integer.MAX_VALUE;
            case Month:
                return 12;
            case Day:
                return 30;
            default:
                assert false : "LookUpOverflowInternal(), index isn't expceted";
                return 0;
        }
    } 
}
