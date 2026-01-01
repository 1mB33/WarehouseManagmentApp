package PojazdyOsobowe.CsvManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja ktora oznacza pola w klasie. Po tej adnotacji bedziemy sortowac pozniej pola do formatu csv
 * i tylko te pola ktore implementuja ta adanotacjie, beda zapisywane i wczytywane z pliku csv.
 * Kazda klasa moze oznaczac kolumny wedlug wlasnej potrzeby, poniewaz sortowanie zawsze odbywa sie tez wzgledem 
 * hierarchi dziedziczonych klas.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ICsvField 
{
    int column();

    boolean canModify() default true;

}
