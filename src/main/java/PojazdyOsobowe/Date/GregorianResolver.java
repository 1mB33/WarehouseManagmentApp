package PojazdyOsobowe.Date;

/**
 * Data wedlug systemu gregorianskiego
 *
 */
public class GregorianResolver implements IDateResolver
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

        while (!fixOverflowInternal(date, date.getSize() - 1))
            ;

        return this;
    }

    public int inDays(final DateContainer date)
    {   
        final int[] monthsNotLeap = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };

        int sum = date.get(EDateFormat.Day);

        int currentMonth = date.get(EDateFormat.Month);
        int monthsInDays = monthsNotLeap[currentMonth - 1];
        int currentYear  = date.get(EDateFormat.Year);

        sum += currentMonth > 2 && isLeap(currentYear) ? monthsInDays + 1 : monthsInDays;
    
        --currentYear;
        sum += currentYear * 365;
        sum += currentYear / 4 - currentYear / 100 + currentYear / 400;

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
                final int currentYear       = date.get(EDateFormat.Year);
                final int currentMonthFloor = floorMonthInternal(date.get(EDateFormat.Month));

                if (currentMonthFloor == 2) {
                    return isLeap(currentYear) ? 29 : 28;
                }
                else if (currentMonthFloor <= 7) {
                    return currentMonthFloor % 2 == 0 ? 30 : 31;
                }
                else {
                    return currentMonthFloor % 2 == 0 ? 31 : 30;
                }
            default:
                assert false : "LookUpOverflowInternal(), index isn't expceted";
                return 0;
        }
    } 

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private boolean fixOverflowInternal(DateContainer date, int partIndex)
    {
        // Fix higher date part first
        if (partIndex == 0)
            return true;

        while (!fixOverflowInternal(date, partIndex - 1)) 
            ;

        int overflowForDays = getDateFormatLimit(date.getDateFormat(partIndex), date); 

        int p = date.get(partIndex);
        if (p > 0 && p <= overflowForDays) 
            return true;
        
        p = p > 0 ? 1 : -1;

        date.increase(partIndex - 1, p);

        // For negative numbers we have to udate the "higher" parts of the date 
        // and look up new overflow
        if (p < 0) {
            while (!fixOverflowInternal(date, partIndex - 1)) 
                ;

            overflowForDays = getDateFormatLimit(date.getDateFormat(partIndex), date);
        }

        date.increase(partIndex, -overflowForDays * p);

        return false;
    } 

    private boolean isLeap(int year)
    { return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0); }

    private int floorMonthInternal(int month)
    { return (month - 1) % 12 + 1; }
}
