package PojazdyOsobowe;

import javax.swing.*;
import java.awt.event.*;

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
        new App().run();
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

    private JFrame          frame;
    private MainController  mainController;      
}
