package PojazdyOsobowe.Date;

/**
 * Enumerator posiadajacy czesci daty
 */
public enum EDateFormat 
{
    Year,
    Month,
    Day;

    @Override
    public String toString()
    {
        switch (this) {
            case Year:  
                return "Year";
            case Month: 
                return "Month";
            case Day:   
                return "Day";
            default:    
                assert false : "EDateFormat, index isn't expected in toString()";
                return super.toString();
        }
    }
}
