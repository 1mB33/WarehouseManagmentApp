package PojazdyOsobowe.Warehouse.GUI.Views;

import javax.swing.*;

import PojazdyOsobowe.Warehouse.Translator;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ProductFormView 
{
    public ProductFormView()
    {
        this.customParameters           = new HashMap<>();
        this.productParameters          = new HashMap<>();

        this.mainPanel                  = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.customCreatorPanel         = new JPanel(new GridLayout(5, 2));
        this.baseProductCreatorPanel    = new JPanel(new GridLayout(3, 2));

        this.mainPanel.add(this.customCreatorPanel);

        this.baseProductCreatorPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 64, 0));
        this.mainPanel.add(this.baseProductCreatorPanel);
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));

        this.scrollPane = new JScrollPane(this.mainPanel);

        this.setUpBaseProductPanel(this.baseProductCreatorPanel);
    }

// Getters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    public JComponent getComponent()
    { return this.scrollPane; }

    public final String getProductParameterValue(final String key)
        throws Exception
    { 
        String r = ((JTextField)(this.productParameters.get(key).getComponent(1))).getText(); 
        if (r.isBlank()) {
            throw new Exception(Translator.get().translate(key, false) + " " + Translator.get().translate("is empty", false));
        }
        return r;
    }

    public final String getCustomParameterValue(final String key)
        throws Exception
    { 
        String r = ((JTextField)(this.customParameters.get(key).getComponent(1))).getText(); 
        if (r.isBlank()) {
            throw new Exception(Translator.get().translate(key, false) + " " + Translator.get().translate("is empty", false));
        }
        return r;
    }

    public HashMap<String, String> getProductParameters()
        throws Exception
    { 
        HashMap<String, String> result = new HashMap<>();

        for (String key : this.productParameters.keySet()) 
            result.put(key, this.getProductParameterValue(key));

        return result; 
    }

    public HashMap<String, String> getCustomParameters()
        throws Exception
    { 
        HashMap<String, String> result = new HashMap<>();

        for (String key : this.customParameters.keySet()) 
            result.put(key, this.getCustomParameterValue(key));

        return result; 
    }

// Setters // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    public void setForm(final List<String> parametersNames)
    {
        this.customParameters.clear();
        this.customCreatorPanel.removeAll();

        for (String p : parametersNames)
        {
            JPanel parameter = this.createCreatorPanelElementInternal(p);
            this.customParameters.put(p, parameter);
            this.customCreatorPanel.add(parameter);
        }

        this.baseProductCreatorPanel.revalidate();
        this.baseProductCreatorPanel.repaint();
        this.customCreatorPanel.revalidate();
        this.customCreatorPanel.repaint();
    }

    public void setFields(final List<String> parametersValues)
    {
        int i = 0;
        try {
        for (i = 0; i < this.productParameters.size(); ++i)
        {
            ((JTextField)(((JPanel)this.baseProductCreatorPanel.getComponent(i))
                                    .getComponent(1)))
                                            .setText(parametersValues.get(i)); 
            if (i >= parametersValues.size()) {
                return; 
            }
        }
        for (; i < this.productParameters.size() + this.customParameters.size(); ++i)
        {
            ((JTextField)(((JPanel)this.customCreatorPanel
                        .getComponent(i - this.productParameters.size()))
                                .getComponent(1)))
                .setText(parametersValues.get(i)); 
            if (i >= parametersValues.size()) {
                return;
            }
        }
            
        } catch (Exception e) {
            System.out.println(this.customParameters.size());
            System.out.println(this.productParameters.size());
            System.out.println(i);
            System.err.println(e.getMessage());
        }
    }

// Internal // --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
    
    private JPanel createCreatorPanelElementInternal(final String content)
    {
        String      normalizedContent   = Translator.get().translate(content, true);
        JPanel      wrapper             = new JPanel(new GridLayout(1, 2));
        JLabel      label               = new JLabel(normalizedContent);
        JTextField  field               = new JTextField();

        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        label.setPreferredSize(new Dimension(20, 40));
        label.setMinimumSize(new Dimension(20, 40));
        label.setMaximumSize(new Dimension(20, 40));

        field.setPreferredSize(new Dimension(20, 40));
        field.setMinimumSize(new Dimension(60, 40));
        field.setMaximumSize(new Dimension(60, 40));

        wrapper.setBorder(BorderFactory.createEmptyBorder(32, 12, 0, 0));
        wrapper.setPreferredSize(new Dimension(80, 52));
        wrapper.setMinimumSize(new Dimension(80, 52));
        wrapper.setMaximumSize(new Dimension(80, 52));

        wrapper.add(label);
        wrapper.add(field);

        return wrapper;
    }
    
    private void setUpBaseProductPanel(JPanel panel)
    {
        final String[] baseProductsFields = {
                    "reciptDate",
                    "price",
                    "amount",
                    "manufacturer",
                    "model",
                    "manufacturingDate",
        };

        panel.removeAll();

        for (String p : baseProductsFields)
        {
            JPanel parameter = this.createCreatorPanelElementInternal(p);
            this.productParameters.put(p, parameter);
            this.baseProductCreatorPanel.add(parameter);
        }
    }

// Members // ---------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    private JPanel mainPanel;
    private JPanel customCreatorPanel;
    private JPanel baseProductCreatorPanel;
    private JScrollPane scrollPane;
    private HashMap<String, JPanel> customParameters;
    private HashMap<String, JPanel> productParameters;
    
}
