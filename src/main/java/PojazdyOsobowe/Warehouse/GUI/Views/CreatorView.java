package PojazdyOsobowe.Warehouse.GUI.Views;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CreatorView 
{
    public CreatorView()
    { this(null); }

    public CreatorView(JComponent creator)
    {
        this.buttons                    = new HashMap<>();
        this.mainPanel                  = new JPanel(new BorderLayout());
        this.form                       = creator;
        this.newProductSelectorPanel    = new JPanel(new GridLayout(10, 1));

        this.mainPanel.add(this.form, BorderLayout.CENTER);
        this.mainPanel.add(this.newProductSelectorPanel, BorderLayout.WEST);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.mainPanel; }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public void setCreatorPanel(JComponent creator)
    { this.form = creator; }

// Methods // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public void addToProductSelector(final String selectorName, Runnable action)
    {
        JButton b = new JButton(selectorName);
        b.addActionListener(e -> {
            this.resetButtons();
            b.setBackground(Color.WHITE);
            b.setForeground(Color.BLACK);
            action.run();
        });
        this.buttons.put(selectorName, b);
        this.newProductSelectorPanel.add(b);
        SwingUtilities.invokeLater(() -> {
            b.doClick();
        });
    }
	
// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    

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
    private JPanel      mainPanel;
    private JComponent  form;
    private JPanel      newProductSelectorPanel;

}
