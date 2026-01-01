package PojazdyOsobowe.Warehouse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton, odpowiada za tlumaczenie slow na inne jezyki, wedlug pliku z tlumaczeniami
 *
 */
public class Translator 
{
    private static final Translator instance = new Translator();

    /**
     * Ta klasa nie powinna nigdy byc tworzona recznie
     */
    private Translator()
    { 
        this.availableLanguages = this.setUpAvailableLanguagesInternal();
        this.translations       = null;
    }

    /**
     * @return Instancja translatora, w programie moze byc tylko jedna taka
     */
    public static Translator get()
    { return instance; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Wczytuje dany jezyk do tlumaczenia elementow aplikacji, po wczytaniu kazde zapytanie
     * do metody translate, bedzie uzywalo wybranego tlumaczenia
     *
     * @param language Jezyk do wczytania
     * @throws IOException Kiedy plik nie moze byc otwarty z jakiegos powodu
     * @throws IllegalArgumentException Kiedy tlumaczenie nie istnieje
     */
    public void loadTranslation(final String language)
        throws IOException
             , IllegalArgumentException
    {
        ObjectMapper    mapper  = new ObjectMapper();
        String          path    = availableLanguages.getOrDefault(language, null);

        if (path == null)
            throw new IllegalArgumentException("Translation is unavilible");

        try {
            translations = mapper.readTree(new File(path));
        }
        catch (IOException e) {
            System.err.println(path);
            throw new IOException("Something has gone wrong", e);
        }
    }

    /**
     * Tlumaczy dany klucz na wczytany do translatora jezyk
     *
     * @param key Klucz do przetlumaczienia, nie musi byc w zaden sposob znormalizowany
     * @param shouldNormalize Czy klucz powinnen byc normalizowany, czyli zmusic go do zaczyniania sie od durzej litery
     * kazdy ciag znakow "aA" mala litera, duza litera zamienic na spacje. Przydatne kiedy kluczem jest ciag wyrazow
     * zapisany w stylach takich jak snake case, pascal case albo camel case.
     * @return Przetlumaczony klucz lub jesli tlumaczenie nie istneje, sam znormalizowany klucz
     */
    public final String translate(final String key, final boolean shouldNormalize)
    {
        String normalizedKey = key.toLowerCase().replaceAll("\\s", "");

        if (this.translations == null || 
            !this.translations.has(normalizedKey)) 
        {
            return shouldNormalize ? translateNormalizeFieldName(key) : key;
        }

        return this.translations.get(normalizedKey).toString().replaceAll("\"", "");
    }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private HashMap<String, String> setUpAvailableLanguagesInternal()
    {
        HashMap<String, String> result = new HashMap<>();

        try {
            for (File file : new File("translations/").listFiles()) 
            {
                String fileName = file.getName();
                result.put(fileName.substring(0, fileName.length() - ".json".length()), file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    private String translateNormalizeFieldName(final String fieldName)
    {
        String normalized = new String();

        char c;
        for (int i = 0; i < fieldName.length(); ++i) 
        {
            c = fieldName.charAt(i);

            if (i == 0) {
                normalized += Character.toUpperCase(c);
                continue;
            }

            if (Character.isUpperCase(c)) {
                normalized += ' ';
                normalized += Character.toLowerCase(c);
                continue;
            }

            normalized += c;
        }

        return normalized;
    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private HashMap<String, String> availableLanguages;
    private JsonNode translations;

}
