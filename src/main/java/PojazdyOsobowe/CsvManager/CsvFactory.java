package PojazdyOsobowe.CsvManager;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.lang.reflect.Field;

import PojazdyOsobowe.Date.DateLogicException;
import PojazdyOsobowe.Date.DateParsingException;
import PojazdyOsobowe.Date.EDateFormat;
import PojazdyOsobowe.Date.GregorianDate;

/**
 * Fabryka ktora zmienia linie tekstu zgodne z formatem CSV na obiekity, kierujac sie adnotacjiami {@link ICsvField}
 * i w druga strone, czyli obiekty konwertuje w linie CSV.
 *
 */
public class CsvFactory 
{
    /**
     * Prywatny konstruktor defaultowy, ta klasa nie powinna byc nigdy tworzona.
     * Uzywane powinny byc tylko jej statyczne metody. 
     */
    private CsvFactory()
    { };

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    /**
     * @param line Linia csv ktora czytamy
     * @param delimiter Znak ktory symbolizuje odstep miedzy polami w lini
     * @return Zwraca nowy obiekt stworzony na podstawie danych z lini csv
     * @throws CsvException Kiedy linia jest zle sformatowana
     * @throws ObjectConstructionException Kiedy prawidlowe stworzenie obiektu nie bylo mozliwe przez klase lub jej pola
     */
    public static Object fromCsvRow(final String line, final char delimiter)
        throws CsvException
             , ObjectConstructionException
    {
        Vector<String> params       = new Vector<>();
        int            dot          = line.indexOf(delimiter);
        int            start        = dot + 1;
        String         className;    

        if (dot == -1) 
            throw new CsvException("Csv line doesn't seem to use provided delimiter or is emtpy");

        className   = line.substring(0, dot);
        dot         = start;

        while (dot != -1 && dot < line.length())
        {
            dot = line.indexOf(delimiter, start);
            if (dot == -1) 
                dot = line.length();

            params.add(line.substring(start, dot));

            start = dot + 1;
        }

        Object result = null;
        try {
            result = Class.forName(className).getConstructor().newInstance();
            createObjectFromParmasInternal(result, result.getClass(), params);
        }
        catch (ObjectConstructionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ObjectConstructionException("Class doesn't have default constructor", e);
        }

        return result;
    }

    /**
     * @param p Obiek ktory przerabiamy na linie csv, zostana tylko wypisane pola uzywajace adnotacji ICsvField
     * @param delimiter Znak ktory symbolizuje odstep miedzy polami
     * @return Linie w formacie csv sybolizujaca dany produkt
     * @throws ObjectConstructionException Kiedy obiekt nie moze byc prawidlowo przekonwertowany
     */
    public static String toCsvRow(final Object p, final char delimiter)
        throws ObjectConstructionException
    { 
        return p.getClass().getName() + 
               delimiter +
               createCsvRowFromObjectInternal(p, p.getClass(), delimiter); 
    }

    /**
     * Zwraca liste pol oznaczonych {@link ICsvField}, zgodnie z kolejnoscia ustalona przez {@link CsvFactory}
     *
     * @param derived Klasa od ktorej zaczynamy zbierac pola
     * @param base Klasa na ktorej konczymy zbierac pola, ta klasa NIE BEDZIE brala udzialu w zbieraniu pol
     * @return Lista pol ktore implementuja {@link ICsvField}
     */
    public static List<Field> gatherCsvFields(Class<?> derived, Class<?> base) 
    { return CsvFactory.gatherCsvFieldsAndFilterOut(derived, base, null); }

    /**
     * Zwraca liste pol oznaczonych {@link ICsvField}, zgodnie z kolejnoscia ustalona przez {@link CsvFactory}
     *
     * @param derived Klasa od ktorej zaczynamy zbierac pola
     * @param base Klasa na ktorej konczymy zbierac pola, ta klasa NIE BEDZIE brala udzialu w zbieraniu pol
     * @param fieldsName Lista nazw pol ktorych nie chcemy 
     * @return Lista pol ktore implementuja {@link ICsvField}
     */
    public static List<Field> gatherCsvFieldsAndFilter(Class<?> derived,
                                                       Class<?> base,
                                                       String[] fieldsName) 
    {
        List<Field> result = new ArrayList<>();

        for (Class<?> currentClass = derived;
             currentClass != base && currentClass != Object.class;
             currentClass = currentClass.getSuperclass())
        {
            result.addAll(Arrays.stream(currentClass.getDeclaredFields())
                                        .filter(f -> { 
                                            if (!f.isAnnotationPresent(ICsvField.class))
                                                return false;
                                            
                                            if (fieldsName == null)
                                                return true;
                                
                                            for (String fieldName : fieldsName) 
                                                if (fieldName.equals(f.getName())) 
                                                    return true;

                                            return false;
                                        })
                                        .sorted((a, b) -> 
                                               Integer.compare(a.getAnnotation(ICsvField.class).column(), 
                                                               b.getAnnotation(ICsvField.class).column()))
                                        .toList()
                                        .reversed());
        }

        result.forEach(f -> f.setAccessible(true));
        return result.reversed();
    }
    /**
     * Zwraca liste pol oznaczonych {@link ICsvField}, zgodnie z kolejnoscia ustalona przez {@link CsvFactory}
     *
     * @param derived Klasa od ktorej zaczynamy zbierac pola
     * @param base Klasa na ktorej konczymy zbierac pola, ta klasa NIE BEDZIE brala udzialu w zbieraniu pol
     * @param fieldsName Lista nazw pol ktore chcemy 
     * @return Lista pol ktore implementuja {@link ICsvField}
     */
    public static List<Field> gatherCsvFieldsAndFilterOut(Class<?> derived,
                                                          Class<?> base,
                                                          String[] fieldsName) 
    {
        List<Field> result = new ArrayList<>();

        for (Class<?> currentClass = derived;
             currentClass != base && currentClass != Object.class;
             currentClass = currentClass.getSuperclass())
        {
            result.addAll(Arrays.stream(currentClass.getDeclaredFields())
                                        .filter(f -> { 
                                            if (!f.isAnnotationPresent(ICsvField.class))
                                                return false;
                                            
                                            if (fieldsName == null)
                                                return true;
                                
                                            for (String fieldName : fieldsName) 
                                                if (fieldName.equals(f.getName())) 
                                                    return false;

                                            return true;
                                        })
                                        .sorted((a, b) -> 
                                               Integer.compare(a.getAnnotation(ICsvField.class).column(), 
                                                               b.getAnnotation(ICsvField.class).column()))
                                        .toList()
                                        .reversed());
        }

        result.forEach(f -> f.setAccessible(true));
        return result.reversed();
    }
    
    /**
     * @param fieldType Klasa do ktorej castujemy
     * @param str String z danymi ktore castujemy
     * @throws IllegalArgumentException Kiedy obsluga klasy nie jest zaimplementowana
     */
    public static Object parseStringifiedField(final Class<?> fieldType, final String str) 
        throws IllegalArgumentException
    {
        if (fieldType == String.class) {
            return str;
        }
        if (fieldType == int.class) {
            return Integer.parseInt(str);
        }
        if (fieldType == double.class) {
            return Double.parseDouble(str);
        }
        if (fieldType == boolean.class) {
            return Boolean.parseBoolean(str);
        }
        if (fieldType == GregorianDate.class) {
            return forceCreateDateFromStringInternal(str);
        }

        throw new IllegalArgumentException("Handling for provided class isn't implemented. [" + 
                                           fieldType.getName() +
                                           "]");
    }

    /**
     * Zczytuje wartosc pol i przekazuje do {@link BiConsumer}
     *
     * @param o Obiekt na ktorym przeprowadzamy operacje
     * @param fields Lista pol ktora obslugujemy, powinna byc to lista pol implementujacych {@link ICsvField}
     * @param operation Operacja ktora bedziemy przeprowadzali z wartosciami pol
     */
    public static void getOnCsvFields(final Object o, 
                                      final List<Field> fields,
                                      final BiConsumer<Field, Object> operation)
    {
        for (Field field : fields) 
        {
            if (!field.canAccess(o) || !field.isAnnotationPresent(ICsvField.class))
                throw new IllegalArgumentException("List containts invalid fields, or object doesn't contain specified fields");
            
            try {
                operation.accept(field, field.get(o));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * Przekazuje pole do {@link Function}, nastepnie ustawia wynik operacji jaka nowa wartosc pola.
     * Sprawdza czy pole moze byc modyfikowane (stan canModify w {@link ICsvField})
     *
     * @param o Obiekt na ktorym przeprowadzamy operacje
     * @param fields Lista pol ktora obslugujemy, powinna byc to lista pol implementujacych {@link ICsvField}
     * @param operation Operacja z ktorej bedziemy wczytywac nowe wartosci pol
     */
    public static void setOnCsvFields(final Object o, 
                                      final List<Field> fields,
                                      final Function<Field, Object> operation)
    {
        for (Field field : fields) 
        {
            if (!field.canAccess(o) || !field.isAnnotationPresent(ICsvField.class))
                throw new IllegalArgumentException("List containts invalid fields, or object doesn't contain specified fields");

            if (!field.getAnnotation(ICsvField.class).canModify())
                continue;
            
            try {
                field.set(o, operation.apply(field));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * Przekazuje pole do {@link Function}, nastepnie ustawia wynik operacji jaka nowa wartosc pola.
     * Pomija sprawdza czy pole moze byc modyfikowane (stan canModify w {@link ICsvField})
     *
     * @param o Obiekt na ktorym przeprowadzamy operacje
     * @param fields Lista pol ktora obslugujemy, powinna byc to lista pol implementujacych {@link ICsvField}
     * @param operation Operacja z ktorej bedziemy wczytywac nowe wartosci pol
     */
    public static void forceSetOnCsvFields(final Object o, 
                                           final List<Field> fields,
                                           final Function<Field, Object> operation)
    {
        for (Field field : fields) 
        {
            if (!field.canAccess(o) || !field.isAnnotationPresent(ICsvField.class))
                throw new IllegalArgumentException("List containts invalid fields, or object doesn't contain specified fields");

            try {
                field.set(o, operation.apply(field));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    /**
     * Ustawia pola obiektu wzgledem zadanej listy parametrow
     *
     * @param obj Referencja do insntancji obiektu nad ktorym pracujemy
     * @param objClass Klasa tworzonego obiektu
     * @param fieldsStringified Lista z wartosciami pol jako String 
     * @throws ObjectConstructionException Kiedy nie mozemy ustawic jednego z pol
     * @throws CsvException Kiedy dlugosc listy z polami nie jest odpowiednia
     */
    private static void createObjectFromParmasInternal(Object obj, 
                                                       final Class<?> objClass,   
                                                       final Vector<String> fieldsStringified)
        throws ObjectConstructionException
             , CsvException
    {   
        CsvFactory.forceSetOnCsvFields(obj, 
                                       gatherCsvFields(objClass, Object.class), 
                                       (Field f) -> {

            return parseStringifiedField(f.getType(),
                                         fieldsStringified.isEmpty() ? "" : fieldsStringified.removeFirst());
        });
    }

    /**
     * @param obj Obiekt ktory przerabamy na csv
     * @param objClass Orginalna klasa obiektu
     * @param delimiter Znak ktory symbolizuje odstep miedzy polami
     * @return Linia w fromacie csv
     * @throws ObjectConstructionException Kiedy jedno z pol nie moze byc przerobione na String
     */
    private static String createCsvRowFromObjectInternal(final Object obj, 
                                                         final Class<?> objClass,
                                                         final char delimiter)
        throws ObjectConstructionException
    {   
        StringBuilder ss = new StringBuilder();
        
        CsvFactory.getOnCsvFields(obj, 
                                  gatherCsvFields(objClass, Object.class), 
                                  (f, v) -> {
                if (!ss.isEmpty()) 
                    ss.append(delimiter);
                ss.append(v.toString());
        });

        return ss.toString();
    }

    /**
     * Tworzy na sile date, probuje naprawic format, jesli jest niezgodny
     *
     * @param str String z ktorego robimy date
     * @return Rezultat proby utworzenia daty, jesli sie nie udalo, bedzie to 01.01.0001
     */
    private static GregorianDate forceCreateDateFromStringInternal(final String str)
    {
        EDateFormat fmt[] = new EDateFormat[] { 
                        EDateFormat.Day, 
                        EDateFormat.Month,
                        EDateFormat.Year 
        }; 
        try {
            return new GregorianDate(str, fmt,'.');
        } 
        catch (DateLogicException e) {
            System.err.println(e);

            return new GregorianDate();
        } 
        catch (DateParsingException e) 
        {
            System.err.println(e);
            
            // Try again, make sure that delimiter is valid, and the amount of digits is correct
            StringBuilder ss = new StringBuilder();
            AtomicInteger i = new AtomicInteger(0);
            AtomicBoolean b = new AtomicBoolean(false);
            str.chars().forEach(character -> {
                if (i.get() == fmt.length - 1) 
                    return;
                
                if (Character.isDigit(character)) {
                    ss.append(character);
                    b.set(true);
                }

                if (character == ' ' || Character.isLetter(character))
                    return;
                
                if (b.get()) {
                    ss.append('.');
                    i.incrementAndGet();
                    b.set(false);
                }
            });

            try {
                return new GregorianDate(ss.toString(), fmt,'.');
            } catch (Exception ee) {
                System.err.println(ee);

                return new GregorianDate();
            }
        }
    }
}
