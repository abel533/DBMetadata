package com.github.abel533.view.panel;

import com.github.abel533.Code;

import javax.swing.*;
import java.awt.*;

public class TableSplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;

    private JTable dbTable;
    private JTable dbField;

	public TableSplitPane() {
        this.setResizeWeight(0.3);

        JPanel panelTable = new JPanel();
        this.setLeftComponent(panelTable);
        panelTable.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panelTable.add(scrollPane, BorderLayout.CENTER);

        dbTable = new JTable();
        dbTable.setFont(Code.TEXT_TABLE);

        scrollPane.setViewportView(dbTable);
        
        JPanel panel_11 = new JPanel();
        panelTable.add(panel_11, BorderLayout.NORTH);
        panel_11.setLayout(new BorderLayout(0, 0));

        JPanel panelField = new JPanel();
        this.setRightComponent(panelField);
        panelField.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        panelField.add(scrollPane_1, BorderLayout.CENTER);

        dbField = new JTable();
        dbField.setFont(Code.TEXT_TABLE);
        scrollPane_1.setViewportView(dbField);
	}

	public JTable getDbTable() {
		return dbTable;
	}

	public JTable getDbField() {
		return dbField;
	}
}
