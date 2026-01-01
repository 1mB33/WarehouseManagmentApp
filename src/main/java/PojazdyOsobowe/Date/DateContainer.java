package PojazdyOsobowe.Date;

import java.util.Arrays;

/**
 * Array ktory zawiera elementy daty, przydatny do interpretacji daty,
 * zapewnia, ze {@link EDateFormat} zawsze bedzie odpowiadal odpowiedniemu indeksowi w arrayu
 * nawet jesli zostana dodane nastepne czesci daty (np godzina, minuty) lub kolejnosc zostanie zmieniona
 *
 */
public class DateContainer implements Comparable<DateContainer>
{
    DateContainer()
    { this.container = new int[EDateFormat.values().length]; }

    /**
     * @param other {@link DateContainer} ktory kopiujemy
     */
    DateContainer(DateContainer other)
    { this.container = Arrays.copyOf(other.container, other.container.length); }

    /**
     * Tworzy klase i natychmiast ustawia dni
     *
     * @param days
     */
    DateContainer(final int days)
    { 
        this(); 
        this.set(EDateFormat.Day, days);
    }

    /**
     * Tworzy klase i natychmiast ustawia dni, miesiac i rok
     *
     * @param days
     * @param month
     * @param year
     */
    DateContainer(final int days, final int month, final int year)
    { 
        this(); 
        this.set(EDateFormat.Day, days);
        this.set(EDateFormat.Month, month);
        this.set(EDateFormat.Year, year);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @return Ilosc elementow przechowywanych przez ten kontener
     */
    public int getSize()
    { return container.length; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Ustawia czesc daty przez uzycie {@link EDateFormat} zamiast indeksu
     *
     * @param datePart czesc daty do ustawienia
     * @param value ustawiana wartosc
     */
    public void set(EDateFormat datePart, int value)
    { this.container[getIndex(datePart)] = value; }

    /**
     * Ustawia czesc daty przez uzycie indeksu
     * nie gwarantuje ze indeks nie wykraczy poza tablice
     *
     * @param partIndex indeks do ustawienia
     * @param value ustawiana wartosc
     */
    public void set(int partIndex, int value)
    { this.container[partIndex] = value; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Zwraca indeks liczbowy na bazie {@link EDateFormat}
     *
     * @param part {@link EDateFormat}
     */
    public int getIndex(EDateFormat part) 
    {
        switch (part) {
            case Year:
                return 0;
            case Month:
                return 1;
            case Day:
                return 2;
            default:
                assert false : "LookUpIndex(), index isn't expceted";
                return 0;
        }
    }

    /**
     * Zwraca {@link EDateFormat} na bazie indeksu liczbowego
     *
     * @param index Indeks liczbowy
     */
    public EDateFormat getDateFormat(int index) 
    {
        switch (index) {
            case 0:
                return EDateFormat.Year;
            case 1:
                return EDateFormat.Month;
            case 2:
                return EDateFormat.Day;
            default:
                assert false : "LookUpDatePart(), index isn't expceted";
                return EDateFormat.Day;
        }
    }

    /**
     * @param index Indeks wartosci elementu daty ktory chcemy dostac
     * @return Liczba ktora jest rowna tej czesci daty
     */
    public int get(int index)
    { return container[index]; }

    /**
     * @param part {@link EDateFormat}
     * @return Wartosc ktora jest przypisana tej czesci daty
     */
    public int get(EDateFormat part)
    { return container[getIndex(part)]; }

    /**
     * @param partIndex Indeks wartosci elementu daty ktory chcemy dostac
     * @param value Liczba o ile chcemy zwiekszyc wartosc czesci daty
     */
    public void increase(int partIndex, int value)
    { this.container[partIndex] += value; }

    /**
     * @param datePart {@link EDateFormat}
     * @param value Liczba o ile chcemy zwiekszyc wartosc czesci daty
     */
    public void increase(EDateFormat datePart, int value)
    { this.container[getIndex(datePart)] += value; }

    @Override
    public String toString()
    {
        return String.format("%02d.%02d.%04d", 
                             this.get(EDateFormat.Day),
                             this.get(EDateFormat.Month),
                             this.get(EDateFormat.Year)); 
    }

    @Override
    public int compareTo(final DateContainer other)
    {
        int result = 0;

        for (int i = 0; i < this.getSize(); ++i)
        {
            result = this.container[i] - other.container[i];
            if (result != 0) 
                break;
        }

        return result;
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

        DateContainer otherDateContianer = (DateContainer)other;

        for (int i = 0; i < this.getSize(); ++i)
            if (this.container[i] != otherDateContianer.container[i])
                return false;

        return true;
    }

// Members // ---------------------------------------------------------------------------------------------------------

    private int[] container;

}
