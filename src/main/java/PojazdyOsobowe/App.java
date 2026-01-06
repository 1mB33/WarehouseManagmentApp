package PojazdyOsobowe;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import PojazdyOsobowe.CsvManager.CsvFactory;
import PojazdyOsobowe.Warehouse.Translator;
import PojazdyOsobowe.Warehouse.Products.*;
import PojazdyOsobowe.Warehouse.GUI.Controllers.MainController;

/**
 * Glowna klasa aplikacji
 *
 */
public class App 
{
    /**
     * Uruchamia graficza wersji aplikacji 
     *
     * @param args Argumenty konsoli
     */
    public static void main(String[] args)
    {
        new App().runCli();
    }

    /**
     * Konstruktor ustawia podstawowe parametry aplikacji
     */
    public App()
    {
        try {
            Translator.get().loadTranslation("polish");
        } catch (Exception e) {
            System.err.println("Couldn't load polish translation [" + e.getMessage() + "]");
            e.printStackTrace();
        }
        System.setProperty("awt.useSystemAAFontSettings","on");

        this.frame          = null;
        this.mainController = null;
    }
    
    /**
     * Uruchamia graficza czesc aplikacji
     */
    public void run()
    {
        this.frame = new JFrame("Vehicles database");
        try {
            this.mainController = new MainController("data/baza.csv",
                                                     new Class<?>[] {
                                                        Car.class,
                                                        UsedCar.class,
                                                        Motorbike.class,
                                                        Quad.class,
                                                        OnionDummyTestClass.class
                                                     });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setSize(1200, 700);
            this.frame.add(this.mainController.getComponent());

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    mainController.save();
                }
            });

            this.frame.setLocationRelativeTo(null);
            this.frame.setVisible(true);
        });
    }

    public void runCli()
    {
        final String[] baseProductsFields = {
            "reciptDate",
            "price",
            "amount",
            "manufacturer",
            "model",
            "manufacturingDate",
        };                    

        try {
            this.mainController = new MainController("data/baza.csv",
                                                     new Class<?>[] {
                                                        Car.class,
                                                        UsedCar.class,
                                                        Motorbike.class,
                                                        Quad.class,
                                                        OnionDummyTestClass.class
                                                     });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int i = 0;
        HashMap<String, String> productsParams = new HashMap<>();
        HashMap<String, String> customParams   = new HashMap<>();
        for (;;)
        {
            try {
                System.out.print("> "); 
                String       cmdStr = in.readLine();
                List<String> cmd    = Arrays.asList(cmdStr.split(" "));
                i = 0;

                String cmdFirstCommandLower = cmd.get(0).toLowerCase();
                if (cmdFirstCommandLower.equals("quit") || cmdFirstCommandLower.equals("exit")) 
                    break;

                if (cmdFirstCommandLower.equals("show"))
                    this.cliShowInternal(this.mainController.getDatabase().getList());

                if (cmdFirstCommandLower.equals("add"))
                {
                    i = 1;

                    productsParams.clear();
                    customParams.clear();

                    if (cmdStr.indexOf(' ') != -1 && 
                        cmdStr.indexOf(' ') != cmdStr.length() - 1)
                    {
                        cmd = Arrays.asList(cmdStr.substring(cmdStr.indexOf(' ') + 1, cmdStr.length()).split(";"));
                    }

                    for (String s : baseProductsFields)
                    {
                        System.out.print("Enter parameter " + s + " > "); 
                        if (i < cmd.size()) {
                            System.out.println(cmd.get(i));
                            productsParams.put(s, cmd.get(i++));
                            continue;
                        }

                        i   = 0;
                        cmd = Arrays.asList(in.readLine().split(";"));

                        productsParams.put(s, cmd.get(i++));
                    }
                        
                    for (String s : this.mainController.getFormController().getParametersNames())
                    {
                        System.out.print("Enter parameter " + s + " > "); 
                        if (i < cmd.size()) {
                            System.out.println(cmd.get(i));
                            customParams.put(s, cmd.get(i++));
                            continue;
                        }

                        i   = 0;
                        cmd = Arrays.asList(in.readLine().split(";"));

                        customParams.put(s, cmd.get(i++));
                    }

                    this.mainController.createAndAddProduct(productsParams, customParams);
                }

                if (cmdFirstCommandLower.equals("delete"))
                {
                    i = 1;
                    int inSearchIndex = -1;
                    int inAmount = -1;
                    if (i < cmd.size()) {
                        try {
                            System.out.print("Enter index > "); 
                            System.out.println(cmd.get(i));
                            inSearchIndex = Integer.parseInt(cmd.get(i++));
                        } catch (Exception e) {
                            inSearchIndex = -1;
                        }
                    }

                    if (i < cmd.size()) {
                        try {
                            System.out.print("Enter amount > "); 
                            System.out.println(cmd.get(i));
                            inAmount = Integer.parseInt(cmd.get(i++));
                        } catch (Exception e) {
                            inAmount = -1;
                        }
                    }

                    while (inSearchIndex == -1) {
                        try {
                            System.out.print("Enter index > "); 
                            inSearchIndex = Integer.parseInt(in.readLine());
                        } catch (Exception e) {
                            inSearchIndex = -1;
                        }
                    }

                    while (inAmount == -1) {
                        try {
                            System.out.print("Enter amount > "); 
                            inAmount = Integer.parseInt(in.readLine());
                        } catch (Exception e) {
                            inAmount = -1;
                        }
                    }

                    int k = 1;
                    for (Product entry : this.mainController.getDatabase().getList())
                    {   
                        if (k == inSearchIndex) {
                            this.mainController.getTableController().decreaseAmount(entry, inAmount);
                            break;
                        }
                        ++k;
                    }

                    if (k > inSearchIndex) {
                        System.out.println("Couldn't find index");
                    }
                }

                if (cmdFirstCommandLower.equals("filter"))
                {
                    if (this.mainController.getDatabase().getList().isEmpty()) {
                        System.out.println("Database is empty");
                        continue;
                    }

                    i = 1;
                    String phrase = null;
                    String column = null;
                    if (i < cmd.size()) {
                        try {
                            System.out.print("Enter column name > "); 
                            System.out.println(cmd.get(i));
                            column = cmd.get(i++);
                        } catch (Exception e) {
                            column = null;
                        }
                    }

                    if (i < cmd.size()) {
                        try {
                            System.out.print("Enter search phrase > "); 
                            System.out.println(cmd.get(i));
                            phrase = cmd.get(i++);
                        } catch (Exception e) {
                            phrase = null;
                        }
                    }

                    while (column == null) {
                        try {
                            System.out.print("Enter column name > "); 
                            column = in.readLine();
                        } catch (Exception e) {
                            column = null;
                        }
                    }

                    while (phrase == null) {
                        try {
                            System.out.print("Enter search phrase > "); 
                            phrase = in.readLine();
                        } catch (Exception e) {
                            phrase = null;
                        }
                    }

                    List<Product> l = new ArrayList<>();
                    List<Field> fieldList = CsvFactory.gatherCsvFieldsAndFilter(this.mainController.getDatabase()
                                                                                                   .getList()
                                                                                                   .getFirst()
                                                                                                   .getClass(),
                                                                                Object.class,
                                                                                new String[] { column });

                    final String filetrPhrase = phrase;
                    this.mainController.getDatabase().getList().forEach(o -> {
                        CsvFactory.getOnCsvFields(o, 
                                                  fieldList,
                                                 (f, value) -> {
                        if (stringContainsInternal(value.toString(), filetrPhrase))
                            l.add(o);
                        });
                    });

                    this.cliShowInternal(l);
                }
                if (cmdFirstCommandLower.equals("save"))
                {
                    this.mainController.save();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cliShowInternal(final List<Product> productList)
    {
        System.out.printf("%4s %64s %9s %16s %16s %16s %16s %16s\n", 
                          "index",
                          "id",
                          "amount",
                          "model",
                          "manufacturer",
                          "category",
                          "reciptDate",
                          "manufacturingDate");
        int k = 0;
        for (Product entry : productList)
        {
            System.out.printf("%4s %64s %9s %16s %16s %16s %16s %16s\n", 
                              ++k,
                              entry.getId(),
                              entry.getAmount(),
                              entry.getModel(),
                              entry.getManufacturer(),
                              entry.getCategory(),
                              entry.getReciptDate(),
                              entry.getManufacturingDate());
        }
    }

    private boolean stringContainsInternal(final String str, final String phrase)
    {
        return str.toLowerCase().matches(".*" + phrase.toLowerCase() + ".*");
    }

    private JFrame          frame;
    private MainController  mainController;      
}
