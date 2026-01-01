package PojazdyOsobowe.Warehouse.GUI.Views;

import java.util.function.Predicate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import PojazdyOsobowe.CsvManager.CsvFactory;
import PojazdyOsobowe.Utils.*;
import PojazdyOsobowe.Warehouse.Translator;
import PojazdyOsobowe.Warehouse.GUI.Models.*;
import PojazdyOsobowe.Warehouse.Products.*;

class ProductsList extends DefaultListCellRenderer
{
    public ProductsList(Class<?> filter)
    {
        this.filter = filter;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) 
    {   
        List<String>    names   = new ArrayList<>();
        List<Field>     fields;
        
        if (this.filter != null) {
            fields = CsvFactory.gatherCsvFields(this.filter, Cargo.class);
        } 
        else {
            fields = CsvFactory.gatherCsvFields(Product.class, Object.class);
        }

        CsvFactory.getOnCsvFields(value, fields, (f, v) -> {
            names.add(Translator.get().translate(v.toString(), false));
        });
    
        if (names.isEmpty())
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    
        JPanel row = new JPanel(new GridLayout(1, names.size()));
    
        if (isSelected) 
        {
            row.setBackground(list.getSelectionBackground());
            row.setForeground(list.getSelectionForeground());
        } 
        else 
        {
            row.setBackground(list.getBackground());
            row.setForeground(list.getForeground());
        }
    
        for (int i = 0; i < names.size(); ++i) 
            row.add(createColumnContentForRow(names.get(i), true, index == 0, i == names.size() - 1));
    
        return row;
    }

    static public JLabel createColumnContentForRow(final String text, 
                                                   final boolean border,
                                                   final boolean isTop,
                                                   final boolean isLast)
    {
        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.PLAIN, 12));
        if (border) 
            label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(isTop ? 1 : 0, 
                                                                                               1, 
                                                                                               1, 
                                                                                               isLast ? 1 : 0,
                                                                                               Color.BLACK),
                                                               BorderFactory.createEmptyBorder(0, 12, 0, 6)));
        else 
            label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 6));

        label.setPreferredSize(new Dimension(72, 32));
        label.setMinimumSize(new Dimension(32, 32));
        label.setMaximumSize(new Dimension(256, 32));

        return label;
    }

    private Class<?> filter;
}

class FancySortableLabelMouseHandler implements MouseListener 
{
    enum SortMode 
    {
        ascending,
        descending,
        database
    }

    @SuppressWarnings("unused")
    private FancySortableLabelMouseHandler()
    { 
        this.popupMenu  = this.setUpPopupMenu();
        this.view       = null;
        this.labelName  = null;
        this.sortMode   = SortMode.ascending;
    }

    public FancySortableLabelMouseHandler(DatabaseView<?> view, final String labelName)
    { 
        this.popupMenu  = this.setUpPopupMenu();
        this.view       = view; 
        this.labelName  = labelName;
        this.sortMode   = SortMode.ascending;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) 
    { }
    
    @Override
    public void mouseExited(MouseEvent e) 
    { }
    
    @Override
    public void mousePressed(MouseEvent e) 
    { }
    
    @Override
    public void mouseReleased(MouseEvent e) 
    { }
    
    @Override
    public void mouseClicked(MouseEvent e) 
    { 
        if (e.getButton() == MouseEvent.BUTTON1) 
        {
            sortAction();
        }
        if (e.getButton() == MouseEvent.BUTTON3) 
        {
            this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void sortAction()
    {
       switch (this.sortMode) {
           case ascending:
               this.view.sort(this.labelName, true); 
               this.sortMode = SortMode.descending;
               break;
           case descending:
               this.view.sort(this.labelName, false); 
               this.sortMode = SortMode.database;
               break;
           case database:
               this.view.reloadDatabase();
               this.sortMode = SortMode.ascending;
               break;

           default:
               break;
       }
    }

    public void filterAction()
    {
        this.view.filter(this.labelName, 
                         JOptionPane.showInputDialog(this.view.getComponent(),
                                                     Translator.get().translate("Enter filter phrase:", false)));
    }

    private JPopupMenu setUpPopupMenu()
    {
        JPopupMenu  result          = new JPopupMenu();
        JMenuItem   sortItem        = new JMenuItem(Translator.get().translate("Sort", false));
        JMenuItem   ascendingItem   = new JMenuItem(Translator.get().translate("Sort", false) + " " +
                                                    Translator.get().translate("ascending", false));
        JMenuItem   descendingItem  = new JMenuItem(Translator.get().translate("Sort", false) + " " +
                                                    Translator.get().translate("descending", false));
        JMenuItem   filterItem      = new JMenuItem(Translator.get().translate("Filter", false));
        JMenuItem   resetItem       = new JMenuItem(Translator.get().translate("Reset", false));
        sortItem.addActionListener(e -> {
            this.sortAction();
        });
        ascendingItem.addActionListener(e -> {
            this.sortMode = SortMode.ascending;
            this.sortAction();
        });
        descendingItem.addActionListener(e -> {
            this.sortMode = SortMode.descending;
            this.sortAction();
        });
        filterItem.addActionListener(e -> {
            this.filterAction();
        });
        resetItem.addActionListener(e -> {
            this.view.reloadDatabase();
        });

        result.add(sortItem);
        result.add(ascendingItem);
        result.add(descendingItem);
        result.add(filterItem);
        result.add(resetItem);

        return result;
    }
    
    private final DatabaseView<?>   view;
    private final String            labelName;
    private SortMode                sortMode;
    private JPopupMenu              popupMenu;
}

public class DatabaseView <STORABLE_TYPE extends Cloneable>
{
    public DatabaseView()
    { 
        this.filtered       = null;
        this.comperatorsMap = new HashMap<>();
        this.products       = new DefaultListModel<>();
        this.columnsNames   = new JPanel();
        this.sortedByLabel  = null;
        this.database       = null;

        this.list = new JList<>(this.products);
        this.setUpListInternal(this.list, this.filtered);

        this.setUpColumnNamesInternal(this.columnsNames);

        this.scrollPane = new JScrollPane(this.list);
        this.scrollPane.setColumnHeaderView(this.columnsNames);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

    /**
     * @param model Baza danych z ktorej wczytujemy poczatkowy stan, moze byc null
     * @param onlyFor Klasa ktora wyswietlamy, moze byc null
     */
    public DatabaseView(final Database<STORABLE_TYPE> model, final Class<?> onlyFor)
    {
        this.filtered       = onlyFor;
        this.comperatorsMap = new HashMap<>();
        this.products       = new DefaultListModel<>();
        this.columnsNames   = new JPanel();
        this.sortedByLabel  = null;
        this.database       = model;

        this.reloadDatabase();

        this.list = new JList<>(this.products);
        this.setUpListInternal(this.list, this.filtered);

        this.setUpColumnNamesInternal(this.columnsNames);

        this.scrollPane = new JScrollPane(this.list);
        this.scrollPane.setColumnHeaderView(this.columnsNames);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

    public DatabaseView(final DatabaseView<STORABLE_TYPE> other, final Class<?> onlyFor)
    {
        this.filtered       = onlyFor;
        this.comperatorsMap = other.comperatorsMap;
        this.products       = new DefaultListModel<>();
        this.columnsNames   = new JPanel();
        this.sortedByLabel  = null;
        this.database       = (Database<STORABLE_TYPE>)other.database;

        this.reloadDatabase();

        this.list = new JList<>(this.products);
        this.setUpListInternal(this.list, this.filtered);

        this.setUpColumnNamesInternal(this.columnsNames);

        this.scrollPane = new JScrollPane(this.list);
        this.scrollPane.setColumnHeaderView(this.columnsNames);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

    /**
     * Konstruktor kopiujacy
     *
     * @param other Inna instancja {@link DatabaseView}
     */
    public DatabaseView(final DatabaseView<STORABLE_TYPE> other)
    {
        this.filtered       = null;
        this.comperatorsMap = new HashMap<>(other.comperatorsMap);
        this.products       = new DefaultListModel<>();
        this.columnsNames   = new JPanel();
        this.sortedByLabel  = null;
        this.database       = new Database<STORABLE_TYPE>((Database<STORABLE_TYPE>)other.database);

        if (this.database != null)
            this.reloadDatabase();

        this.list = new JList<>(this.products);
        this.setUpListInternal(this.list, this.filtered);

        this.setUpColumnNamesInternal(this.columnsNames);

        this.scrollPane = new JScrollPane(this.list);
        this.scrollPane.setColumnHeaderView(this.columnsNames);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.scrollPane; }

    public List<STORABLE_TYPE> getSelected()
    { return this.list.getSelectedValuesList(); }

    public DefaultListModel<STORABLE_TYPE> getList()
    { return this.products; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void setDatabase(Database<STORABLE_TYPE> database)
    { 
        this.database = database; 
        this.reloadDatabase();
    }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    /**
     * @param p Produkt ktory dodajemy do bazy
     */
    public void addProduct(final STORABLE_TYPE p)
    {
        // Only add products that are the same class as the first element
        if (this.filtered != null && this.filtered != p.getClass())
            return;

        // If we started with empty list then set up columns
        if (this.products.isEmpty()) 
        {
            this.products.addElement(p); 
            this.setUpColumnNamesInternal(this.columnsNames);
            return;
        }
        
        this.products.addElement(p); 
    }
    
    /**
     * @param p Produkt do usuniecia
     */
    public void removeProduct(final STORABLE_TYPE p)
    { this.products.removeElement(p); }

    public void scrollDown()
    {
        SwingUtilities.invokeLater(() -> {
            JScrollBar scroll = this.scrollPane.getVerticalScrollBar();
            scroll.setValue(scroll.getMaximum());
        });
    }

    /**
     * Odswieza GUI tabeli
     */
    public void refresh()
    {
        this.scrollPane.revalidate();
        this.scrollPane.repaint();
    }

    /**
     * Przeladowywuje dane ktore znajuda sie w tabeli
     */
    public void reloadDatabase()
    {
        if (this.database == null) 
            return;

        Predicate<STORABLE_TYPE> filterStrategy = this.filtered != null 
            ? item -> item.getClass() == this.filtered 
            : item -> true;

        this.products.clear();
        for (STORABLE_TYPE c : this.database.getList()
                                      .stream()
                                      .filter(filterStrategy)
                                      .toList())
        {
            this.addProduct(c);
        }

        if (this.sortedByLabel != null) {
            this.sortedByLabel.setText(this.sortedByLabel.getText()
                                                          .substring(0, 
                                                                     this.sortedByLabel.getText().length() - 3));
            this.sortedByLabel = null;
        }
    }

    /**
     * Sortuje zwiekszajaco kolumne
     *
     * @param columnName Nazwa kolumny
     * @param isReversed Czy odwrocic sortowanie
     */
    @SuppressWarnings("all")
    public void sort(final String columnName, final boolean isReversed)
    {
        Pair<Class<?>, Comparator> compareStrategy = this.comperatorsMap.getOrDefault(columnName.toLowerCase()
                                                                                                .replaceAll("\\s", ""), 
                                                                                      null);
        Object firstItem = this.products.isEmpty() ? null : this.products.firstElement();
        if (compareStrategy == null) {
            System.err.println("This column doesn't have compare strategy implemented [" + columnName + "]");
            return;
        }

        // Chcek if items in the list are instance of or extends the class that the comperator handles 
        if (!compareStrategy.getFirstOrDefault(null).isAssignableFrom(firstItem.getClass())) {
            System.err.println("This column comperator doesn't handles this class [" + firstItem.getClass() + "]");
            return;
        }

        try {
            List<STORABLE_TYPE> l = Collections.list(this.products.elements());
            l.sort(compareStrategy.getSecond());
            if (isReversed) 
                l = l.reversed();

            this.products.clear();

            for (Object o : l) 
                this.products.addElement((STORABLE_TYPE)o);

            this.refresh();

            if (this.sortedByLabel != null) {
                this.sortedByLabel.setText(this.sortedByLabel.getText()
                                                             .substring(0, 
                                                                        this.sortedByLabel.getText().length() - 4));
            }

        
            JLabel currentLabel;
            for (Component c :  this.columnsNames.getComponents())
            {
                currentLabel = (JLabel)c;

                if (!currentLabel.getText().equals(Translator.get().translate(columnName, true))) 
                    continue;
                
                if (isReversed) {
                    currentLabel.setText(currentLabel.getText() + "  /\\");
                }
                else {
                    currentLabel.setText(currentLabel.getText() + "  \\/");
                }

                this.sortedByLabel = currentLabel;
            }
        } catch (Exception e) {
            System.err.println("Something happend on sort [" + e.getMessage() + "]");
        }
    }

    /**
     * Filtruje kolumne
     *
     * @param column Nazwa kolumny ktora filtrujemy
     * @param phrase Haslo ktorego szukamy
     */
    public void filter(final String column, final String phrase)
    {
        try {
            List<STORABLE_TYPE> l = Collections.list(this.products.elements());
            if (l.isEmpty()) {
                this.refresh();
                return;
            }

            this.products.clear();
            List<Field> fieldList = CsvFactory.gatherCsvFieldsAndFilter(l.getFirst().getClass(),
                                                                        Object.class,
                                                                        new String[] { column });
            l.forEach(o -> {
                CsvFactory.getOnCsvFields(o, 
                                          fieldList,
                                          (f, value) -> {
                        if (stringContainsInternal(value.toString(), phrase))
                            this.products.addElement((STORABLE_TYPE)o);
                });
            });

            this.refresh();
            
        } catch (Exception e) {
            System.err.println("Something happend on filter [" + e.getMessage() + "]");
        }
    }

    /**
     * Zaznacza obiekt w liscie
     *
     * @param o Obiket do zaznaczenia
     */
    public void select(Object o) 
    { this.list.setSelectedValue(o, true); }

    @SuppressWarnings("all")
    public void addComparator(final String fieldName, Class<?> forClass, Comparator comparator)
    { this.comperatorsMap.put(fieldName, new Pair<Class<?>, Comparator>(forClass, comparator)); }
    
// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private void setUpListInternal(JList<? extends STORABLE_TYPE> list, Class<?> filtered)
    {
        list.setFont(new Font("Arial", Font.PLAIN, 17));
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        list.setCellRenderer(new ProductsList(filtered));
    }

    private void setUpColumnNamesInternal(JPanel panel)
    {
        List<String>    names       = null;
        Class<?>        mostDerived = this.filtered != null ? this.filtered : Product.class;
        Class<?>        base        = this.filtered != null ? Cargo.class : Object.class;

        names = CsvFactory.gatherCsvFields(mostDerived, base)
                          .stream()
                          .map(Field::getName)
                          .toList();
        

        if (names == null) 
            return;

        panel.removeAll();
        panel.setLayout(new GridLayout(1, names.size()));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String name : names) 
        {
            JLabel label = ProductsList.createColumnContentForRow(Translator.get().translate(name, true), 
                                                                  false,
                                                                  false,
                                                                  false);

            label.addMouseListener(new FancySortableLabelMouseHandler(this, name));

            panel.add(label);
        }
    }

    private boolean stringContainsInternal(final String str, final String phrase)
    {
        return str.toLowerCase().matches(".*" + phrase.toLowerCase() + ".*");
    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private Class<?>                            filtered;
    private DefaultListModel<STORABLE_TYPE>     products;
    private JList<STORABLE_TYPE>                list;
    private JPanel                              columnsNames;
    private JScrollPane                         scrollPane;
    private JLabel                              sortedByLabel;
    private Database<STORABLE_TYPE>             database;
    @SuppressWarnings("all")
    private final HashMap<String, Pair<Class<?>, Comparator>> comperatorsMap;
            
}
