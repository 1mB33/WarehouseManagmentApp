package PojazdyOsobowe.Warehouse.GUI.Controllers;

import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import PojazdyOsobowe.CsvManager.*;
import PojazdyOsobowe.Warehouse.Translator;
import PojazdyOsobowe.Warehouse.GUI.Models.*;
import PojazdyOsobowe.Warehouse.GUI.Views.MainView;
import PojazdyOsobowe.Warehouse.Products.Comparators.*;
import PojazdyOsobowe.Warehouse.Products.*;

/**
 * Kontroluje glowny panel aplikacji
 *
 */
public class MainController 
{
    public MainController()
        throws IOException
    { this(null, null); }

    public MainController(final String databasePath, Class<?>[] usedClasses)
        throws IOException
    {
        this.databasePath   = databasePath;
        this.view           = new MainView();
        this.tables         = new TablesController<>();
        this.creator        = new CreatorController();
        this.db             = new Database<>();
        this.usedClass      = usedClasses;

        this.view.setCreatorView(this.creator.getComponent());
        this.view.setTableView(this.tables.getComponent());

        if (this.databasePath != null) {
            this.setUpDbInternal(this.db, this.databasePath);
            this.setUpTablesInternal(this.usedClass, this.db, this.tables);
        }
        this.setUpCreatorInternal(this.usedClass, this.creator);
        this.setUpToolBarInternal(this.view);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public JComponent getComponent()
    { return this.view.getComponent(); }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * Zapisuje stan bazy danych do tego samego pliku z ktorego zostala wczytana
     */
    public void save()
    { 
        if (this.db != null && this.databasePath != null)
            this.db.saveFile(this.databasePath); 
    }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private void setUpDbInternal(Database<Product> db, final String databasePath) 
        throws IOException
    {
        try {
            db.loadFile(databasePath);
        } catch (FileNotFoundException e) {
            int index = databasePath.indexOf('\\') != -1 
                                               ? databasePath.indexOf('\\') 
                                               : databasePath.indexOf('/');
            if (index != -1)
                new File(databasePath.substring(0, index)).mkdirs();

            new File(databasePath).createNewFile();
            db.loadFile(databasePath);
        }
    }

    private void setUpTablesInternal(Class<?>[] productsClasses, Database<Product> db, TablesController<Product> tablesController)
    {
        DatabaseController<Product> base = new DatabaseController<Product>(db, null);

        base.addComparator("id", Cargo.class, new IdComparator());
        base.addComparator("price", Product.class, new PriceComparator());
        base.addComparator("model", Product.class, new ModelComparator());
        base.addComparator("manufacturer", Product.class, new ManufacturerComparator());
        base.addComparator("reciptdate", Product.class, new ReciptDateComparator());
        base.addComparator("manufacturingdate", Product.class, new ManufacturingDateComparator());
        base.addComparator("amount", Product.class, new AmountComparator());
        base.addComparator("category", Product.class, new CategoryComparator());

        tablesController.addTable(base,
                                  Translator.get().translate("All", false));

        if (productsClasses == null || productsClasses.length == 0) {
            return;
        }

        String name;
        for (int i = 0; i < productsClasses.length; ++i)
        {
            name = productsClasses[i].getName();
            tablesController.addTable(new DatabaseController<Product>(base, productsClasses[i]),
                                      Translator.get().translate(name.substring(name.lastIndexOf('.') + 1, name.length()), 
                                                                 false));
        }
    }

    private void setUpCreatorInternal(Class<?>[] productsClasses, CreatorController creatorController)
    { 
        if (productsClasses == null || productsClasses.length == 0) {
            return;
        }

        String name;
        for (int i = 0; i < productsClasses.length; ++i)
        {
            name = productsClasses[i].getName();
            creatorController.addForm(Translator.get().translate("Form", false) + ": " +
                                      Translator.get().translate(name.substring(name.lastIndexOf('.') + 1, name.length()), 
                                                                                                       false),
                                      productsClasses[i]);
        }
        creatorController.setFormClass(productsClasses[0]); 
    }

    private void setUpToolBarInternal(MainView view)
    {
        view.addToolAction(Translator.get().translate("New product", false), () -> {
            try {
                Product newProduct = this.creator.tryToCreate();

                if (newProduct == null) 
                    return;

                DatabaseController<Product>   table       = this.tables.getActiveTable();
                Optional<Product>             duplicate   = this.db
                                                              .getList()
                                                              .stream()
                                                              .filter(c -> c.equals(newProduct))
                                                              .findFirst();


                if (duplicate.isEmpty()) {
                    table.add(newProduct);
                    table.scrollDown();
                }
                else {
                    table.increaseAmount(duplicate.get(), newProduct.getAmount());
                }

                if (table.getHandledClass() != newProduct.getClass()) 
                    this.tables.reloadTablesThatHandles(newProduct.getClass());

            } catch (Exception e) {
                view.setMessage(e.getMessage());
            }
        });

        view.addToolAction(Translator.get().translate("Add product (1 item)", false), () -> {
            try {
                DatabaseController<Product> table = this.tables.getActiveTable();
                List<Product> toBeDecreased = table.getSelected();
                
                for (Product c : toBeDecreased)
                    table.increaseAmount(c, 1);

            } catch (Exception e) {
                view.setMessage(e.getMessage());
            }
        });

        view.addToolAction(Translator.get().translate("Delete product (1 item)", false), () -> {
            try {
                DatabaseController<Product>   table           = this.tables.getActiveTable();
                List<Product>                 toBeDecreased   = table.getSelected();
                
                for (Product c : toBeDecreased)
                    table.decreaseAmount(c, 1);

            } catch (Exception e) {
                view.setMessage(e.getMessage());
            }
        });

        view.addToolAction(Translator.get().translate("Delete product (whole row)", false), () -> {
            DatabaseController<Product> table = this.tables.getActiveTable();
            List<Product> toBeRemoved = table.getSelected();
            for (Product c : toBeRemoved)
                table.remove(c);
        });
        view.addToolAction(Translator.get().translate("Load database", false), () -> {
            JFileChooser        openFile    = new JFileChooser();
            String              dbPath      = new String();
            Database<Product>   db          = new Database<>();
            
            if (openFile.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION) {
                dbPath = openFile.getSelectedFile().getName();
            } else {
                view.setMessage("No file selected.");
                return;
            }

            if (!dbPath.substring(dbPath.length() - 4, dbPath.length()).equals(".csv")) {
                view.setMessage("Please select a csv file");
                return;
            }

            try {
                this.setUpDbInternal(db, dbPath);
            } catch (Exception e) {
                view.setMessage(e.getMessage());
                return;
            }

            this.databasePath   = dbPath;
            this.db             = db;

            this.tables.clear();
            this.setUpTablesInternal(usedClass, this.db, this.tables);
        });
        view.addToolAction(Translator.get().translate("Load product", false), () -> {
            List<Product> selected = this.tables.getActiveTable().getSelected();
            if (selected.isEmpty()) {
                return;
            }
            
            this.creator.setForm(selected.getFirst());
            this.creator.loadItem(selected.getFirst());
        });
        view.addToolAction(Translator.get().translate("Modify selected product", false), () -> {
            DatabaseController<Product>   table                 = this.tables.getActiveTable();
            List<Product>                 selectedProducts      = table.getSelected();
            if (selectedProducts.isEmpty()) {
                return;
            }
            Product selectedItem = selectedProducts.getFirst();

            try {
                Product newProduct = this.creator.tryToCreate();
                if (newProduct == null) 
                    return;

                CsvFactory.forceSetOnCsvFields(selectedItem,
                                               CsvFactory.gatherCsvFields(newProduct.getClass(), Cargo.class), 
                                               (Field f) -> {
                        try {
                            if (!f.getAnnotation(ICsvField.class).canModify() && 
                                !f.get(newProduct).equals(f.get(selectedItem))) 
                            {
                                    view.setMessage(Translator.get().translate("This field cannot be modified: ", false) + 
                                                    Translator.get().translate(f.getName(), true));
                            }
                            return f.get(newProduct);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                        }

                        return 0;
                    });

                table.reload();
                table.select(selectedItem);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                view.setMessage(e.getMessage());
            }
        });

        view.addToolAction(Translator.get().translate("Reset table view", false), () -> {
            this.tables.getActiveTable().reload();
        });

        view.addToolAction(Translator.get().translate("Load data from Csv", false), () -> {
            try {
                Database<Product>   newProducts = new Database<>();
                JFileChooser        openFile    = new JFileChooser();
                String              dbPath      = new String();
                
                if (openFile.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION) {
                    dbPath = openFile.getSelectedFile().getName();
                } else {
                    view.setMessage("No file selected.");
                    return;
                }

                this.setUpDbInternal(newProducts, dbPath);
                newProducts.getList().forEach(p -> this.db.addProduct(p));
                this.tables.reloadTables();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.getComponent(), "Error on file load");
            }
        });

        view.addToolAction(Translator.get().translate("Sell products from Csv data", false), () -> {
            try {
                Database<Product>   soldProducts = new Database<>();
                JFileChooser        openFile    = new JFileChooser();
                String              dbPath      = new String();
                
                if (openFile.showOpenDialog(this.getComponent()) == JFileChooser.APPROVE_OPTION) {
                    dbPath = openFile.getSelectedFile().getName();
                } else {
                    view.setMessage("No file selected.");
                    return;
                }

                this.setUpDbInternal(soldProducts, dbPath);
                soldProducts.getList().forEach(p -> this.db.removeProduct(p));
                this.tables.reloadTables();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.getComponent(), "Error on file load");
            }
        });
    }


// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private String databasePath;
    private MainView view;
    private TablesController<Product> tables;
    private CreatorController creator;
    private Database<Product> db;
    private Class<?>[] usedClass;

}
