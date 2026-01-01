package PojazdyOsobowe.Utils;

/**
 * Laczy dwa obiekty w pare
 *
 */
public class Pair <First, Second> 
{
    public Pair()
    {
        this.objFirst = null;
        this.objSecond = null;
    }

    public Pair(First objFirst, Second objSecond)
    {
        this.objFirst = objFirst;
        this.objSecond = objSecond;
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public First getFirst()
        throws ObjectIsNullException
    { 
        if (this.objFirst == null)
            throw new ObjectIsNullException();

        return this.objFirst; 
    }

    public Second getSecond()
        throws ObjectIsNullException
    { 
        if (this.objSecond == null)
            throw new ObjectIsNullException();

        return this.objSecond; 
    }

    public First getFirstOrDefault(First defaultOption)
    { 
        if (this.objFirst == null)
            return defaultOption;

        return this.objFirst; 
    }

    public Second getSecondOrDefault(Second defaultOption)
    { 
        if (this.objSecond == null)
            return defaultOption;

        return this.objSecond; 
    }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void setFirst(First obj) 
    { this.objFirst = obj; }

    public void setSecond(Second obj) 
    { this.objSecond = obj; }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private First objFirst;
    private Second objSecond;

}
