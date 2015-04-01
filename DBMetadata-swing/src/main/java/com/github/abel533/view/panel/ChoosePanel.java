package com.github.abel533.view.panel;

import com.github.abel533.Code;
import com.github.abel533.utils.I18n;

import javax.swing.*;
import java.awt.*;

public class ChoosePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> comboBoxCatalog;
	private JComboBox<String> comboBoxSchema;
	private JButton buttonOK;

	public ChoosePanel() {
		JLabel lblTitle = new JLabel(I18n.key("tools.load.label.tip"));
		JLabel lblCatalog = new JLabel(I18n.key("tools.load.label.catalog"));
		comboBoxCatalog = new JComboBox<String>();
		JLabel lblSchema = new JLabel(I18n.key("tools.load.label.schema"));
		comboBoxSchema = new JComboBox<String>();
		buttonOK = new JButton(I18n.key("tools.load.btn.ok"));
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 5, 10, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 0;
		lblTitle.setFont(Code.TEXT_FONT);
		this.add(lblTitle);
		layout.setConstraints(lblTitle, c);
		
		c.gridwidth = 1;
		c.weightx = 0;
		lblCatalog.setFont(Code.TEXT_FONT);
		this.add(lblCatalog);
		layout.setConstraints(lblCatalog, c);
		
		c.gridwidth = 0;
		c.weightx = 1;
		comboBoxCatalog.setFont(Code.TEXT_FONT);
		this.add(comboBoxCatalog);
		layout.setConstraints(comboBoxCatalog, c);
		
		c.gridwidth = 1;
		c.weightx = 0;
		lblSchema.setFont(Code.TEXT_FONT);
		this.add(lblSchema);
		layout.setConstraints(lblSchema, c);
		
		c.gridwidth = 0;
		c.weightx = 1;
		comboBoxSchema.setFont(Code.TEXT_FONT);
		this.add(comboBoxSchema);
		layout.setConstraints(comboBoxSchema, c);
		
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		buttonOK.setFont(Code.TEXT_BUTTON);
		this.add(buttonOK);
		layout.setConstraints(buttonOK, c);
	}

	public JComboBox<String> getComboBoxCatalog() {
		return comboBoxCatalog;
	}

	public JComboBox<String> getComboBoxSchema() {
		return comboBoxSchema;
	}

	public JButton getButtonOK() {
		return buttonOK;
	}
}
