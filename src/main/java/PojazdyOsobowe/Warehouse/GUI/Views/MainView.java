package PojazdyOsobowe.Warehouse.GUI.Views;

import javax.swing.*;
import java.awt.*;

public class MainView 
{
    public MainView()
    {
        this.mainPanel      = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.toolView       = new JPanel(new GridLayout(2, 6));
        this.tableView      = new JPanel(new BorderLayout());
        this.messageView    = new JPanel(new BorderLayout());
        this.messageView.setMaximumSize(new Dimension(99999, 20));
        this.messageView.add(new JLabel());
        this.creatorView    = new JPanel(new BorderLayout());
        this.creatorView.setMinimumSize(new Dimension(0, 100));
        this.creatorView.setPreferredSize(new Dimension(0, 500));
        this.creatorView.setMaximumSize(new Dimension(99999, 500));

        this.mainPanel.setMaximumSize(new Dimension(128, 200));

        this.mainPanel.add(this.toolView);
        this.mainPanel.add(this.creatorView);
        this.mainPanel.add(this.messageView);
        this.mainPanel.add(this.tableView);
    }

    public JComponent getComponent()
    { return this.mainPanel; }

    public void setTableView(JComponent view)
    { 
        this.tableView.removeAll();
        this.tableView.add(view);   
    }

    public void setCreatorView(JComponent view)
    { 
        this.creatorView.removeAll();
        this.creatorView.add(view);   
    }

    public void addToolAction(final String actionName, Runnable action)
    {
        JButton b = new JButton(actionName);
        b.setPreferredSize(new Dimension(64, 32));
        b.addActionListener(e -> {
            action.run();
        });
        this.toolView.add(b);
    }

    public void setMessage(final String message)
    {
        ((JLabel)this.messageView.getComponent(0)).setText(message);
    }

    private JPanel creatorView;
    private JPanel tableView;
    private JPanel toolView;
    private JPanel messageView;
    private JPanel mainPanel;
    
}
