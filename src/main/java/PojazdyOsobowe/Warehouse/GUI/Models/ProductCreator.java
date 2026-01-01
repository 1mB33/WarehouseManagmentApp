package PojazdyOsobowe.Warehouse.GUI.Models;

import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import PojazdyOsobowe.CsvManager.CsvFactory;
import PojazdyOsobowe.Date.EDateFormat;
import PojazdyOsobowe.Warehouse.GUI.IProductConstructor;
import PojazdyOsobowe.Date.GregorianDate;
import PojazdyOsobowe.Warehouse.Products.*;

/**
 * Pomaga w stworzeniu nowych produktow
 *
 */
public class ProductCreator 
{
    public ProductCreator()
    {
        this.formParameters = new ArrayList<>();
        this.createdClass   = null;
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @return Klasa obiektu ktory aktulanie tworzymy
     */
    public final Class<?> getTargetedClass()
    { return this.createdClass; }

    /**
     * @return Lista nazw pol klasy ktora targetujemy
     */
    public final List<String> getParametersNames()
    { return this.formParameters; }

    /**
     * @param o Obiekt ktory przetwarzamy
     * @return Lista z wartosciami pol implementujacych {@link PojazdyOsobowe.CsvManager.ICsvField} w postaci String
     */
    public List<String> getObjectValues(Object o)
    {
        List<String> values = new ArrayList<>();
        try {
            List<Field>  fields = CsvFactory.gatherCsvFieldsAndFilterOut(o.getClass(), 
                                                                         Cargo.class,
                                                                         new String[] {
                                                                           "category"
                                                                         });
    
            CsvFactory.getOnCsvFields(o, fields, (f, v) -> {
                Class<?> type = f.getType();
                if (type == Integer.class) {
                    values.add(Integer.toString((int)v));
                    return;
                }
                if (type == Double.class) {
                    values.add(Double.toString((Double)v));
                    return;
                }
                values.add(v.toString());
            });

            return values; 
        } catch (Exception e) {
        }

        return values;
    } 

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param c Wczytuje klase jako aktualnie tworzona
     */
    public void loadClass(Class<?> c) 
    {
        this.formParameters = CsvFactory.gatherCsvFields(c, Product.class)
                                        .stream()
                                        .map(Field::getName)
                                        .toList();
        this.createdClass = c;
    }

    /**
     * Probuje stworzyc obiekt na podstawie parametow z list
     *
     * @param productParams Lista wartosci odpowiadajaca klasie {@link Product}
     * @param customParams Lista wartosci odpowiadajaca klasie ktora jest aktulanym targetem
     * @return Nowo stworzony obiekt 
     * @throws Exception Jesli cokolwiek pojdzie nie tak podczas tworzenia obiektu
     */
    public Object tryToCreatoFromParams(final HashMap<String, String> productParams, 
                                         final HashMap<String, String> customParams)
        throws Exception
    {
        Class<?> toBeCreated = this.createdClass;

        try 
        {
            if (toBeCreated == null)
                throw new Exception("No class selected for creation");

            String reciptDateStr = productParams.get("reciptDate").strip();
            GregorianDate reciptDate = new GregorianDate(reciptDateStr,
                                                         new EDateFormat[] {
                                                             EDateFormat.Day,
                                                             EDateFormat.Month,
                                                             EDateFormat.Year,
                                                         }, 
                                                         '.');
            String manufacturingDateStr = productParams.get("manufacturingDate").strip();
            GregorianDate manufacturingDate = new GregorianDate(manufacturingDateStr,
                                                                new EDateFormat[] {
                                                                    EDateFormat.Day,
                                                                    EDateFormat.Month,
                                                                    EDateFormat.Year,
                                                                }, 
                                                                '.');
            Product base = new Product("UNKNOWN",
                                       reciptDate,
                                       Double.parseDouble(productParams.get("price").strip()),
                                       Integer.parseInt(productParams.get("amount").strip()),
                                       productParams.get("manufacturer"),
                                       productParams.get("model"),
                                       manufacturingDate);

            Optional<Constructor<?>> productConstructor = Arrays.stream(toBeCreated.getConstructors())
                                                                .filter(contstructor -> {
                                                                    return contstructor.isAnnotationPresent(
                                                                                    IProductConstructor.class);
                                                                })
                                                                .findFirst();

            if (productConstructor.isEmpty()) {
                throw new Exception("This product cannot be created, it doesn't have a product constructor");
            }

            Object newProduct = productConstructor.get().newInstance(base);
            CsvFactory.forceSetOnCsvFields(newProduct, 
                                      CsvFactory.gatherCsvFields(toBeCreated, Product.class), 
                                      (Field f) -> {
                    return CsvFactory.parseStringifiedField(f.getType(), 
                                                            customParams.get(f.getName()));
            });

            return (Object)newProduct;
        }
        catch (Exception e) 
        {
            throw e;
        }

    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private List<String> formParameters;
    private Class<?> createdClass;

}
