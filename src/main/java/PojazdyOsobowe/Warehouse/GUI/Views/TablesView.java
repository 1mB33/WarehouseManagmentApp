package PojazdyOsobowe.Warehouse.GUI.Views;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TablesView 
{
    public TablesView()
    { 
        this.buttons        = new HashMap<>();
        this.mainPanel      = new JPanel(new BorderLayout()); 
        this.buttonsPanel   = new JPanel(new GridLayout(1, 4)); 
        this.layout         = new CardLayout();
        this.tablePanel     = new JPanel(this.layout); 

        this.mainPanel.add(buttonsPanel, BorderLayout.NORTH);
        this.mainPanel.add(tablePanel, BorderLayout.CENTER);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.mainPanel; }

    public final String getActiveTableName()
    { return this.activeTable; }

// Setters // ---------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------
    
    public void setActivePanel(final String panelName)
    { 
        this.activeTable = panelName;
        this.resetButtons();
        this.buttons.get(panelName).setBackground(Color.WHITE);
        this.buttons.get(panelName).setForeground(Color.BLACK);
        this.layout.show(this.tablePanel, panelName); 
    }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public void addTable(JComponent panel, final String panelName)
    {
        this.tablePanel.add(panel, panelName);
        this.addButton(panelName);
    }

    public void clear()
    { this.tablePanel.removeAll(); }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private void addButton(final String name)
    {
        JButton b = new JButton(name);

        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(64, 25));
        b.setMaximumSize(new Dimension(128, 25));
        b.addActionListener(e -> {
            this.setActivePanel(name);
            this.activeTable = name;
        });

        this.buttons.put(name, b);
        this.buttonsPanel.add(b);
    }

    private void resetButtons()
    {
        buttons.forEach((s, b) -> {
            b.setBackground(Color.BLACK);
            b.setForeground(Color.WHITE);
        });
    }


// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private HashMap<String, JButton> buttons;
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private CardLayout layout;
    private JPanel tablePanel;
    private String activeTable;

}
