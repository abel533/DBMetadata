package com.github.abel533.view;

import com.github.abel533.ICON;
import com.github.abel533.controller.ToolsController;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.panel.ControlPanel;
import com.github.abel533.view.panel.SearchPanel;
import com.github.abel533.view.panel.TableSplitPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolsFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private TableSplitPane tableSplitPane;
    private SearchPanel searchPanel;
    private ControlPanel controlPanel;

    public ToolsFrame() {
        setMinimumSize(new Dimension(890, 360));
        setTitle(I18n.key("tools.name"));
        setIconImage(ICON.LOGO_PNG.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 633);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tableSplitPane = new TableSplitPane();
        contentPane.add(tableSplitPane, BorderLayout.CENTER);

        searchPanel = new SearchPanel();
        contentPane.add(searchPanel, BorderLayout.NORTH);

        controlPanel = new ControlPanel();
        contentPane.add(controlPanel, BorderLayout.SOUTH);

        new ToolsController(this).initView().initAction();
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public TableSplitPane getTableSplitPane() {
        return tableSplitPane;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}
