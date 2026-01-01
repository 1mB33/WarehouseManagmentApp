package PojazdyOsobowe.Date;

/**
 * Klasy implementujace ten interface maja za zadanie interpretowac daty
 */
public interface IDateResolver
{
    /**
     * Ta metoda powinna przerobic ilosc dni na sensowna date w {@link DateContainer}
     *
     * @param date {@link DateContainer}
     * @param days ilosc dni do zinterpretowania
     */
    public IDateResolver resolveContainer(DateContainer date, final int days);

    /**
     * Powinna przerobic date na ilosc dni
     *
     * @param date {@link DateContainer}
     */
    public int inDays(final DateContainer date);

    /**
     * Powinno zwrocic dopuszczalna wartosc dla danej czesci daty
     *
     * @param part Czesc daty ktora sprawdzamy
     * @param date {@link DateContainer} na ktorym pracujemy, przydatne, jesli dopuszczalny limit
     * jest uzalezniony od innej czesci daty
     */
    public int getDateFormatLimit(final EDateFormat part, final DateContainer date);
}
