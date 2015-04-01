package com.github.abel533.view.panel;

import com.github.abel533.utils.I18n;

import javax.swing.*;
import java.awt.*;

/**
 * 主界面中，可以重新选择数据库，重新加载
 *
 * @author liuzh
 * @since 2015-03-20
 */
public class ControlPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JSlider sliderFontsize;
    private JButton changeDatabase;
    private JComboBox<String> comboBoxCatalog;
    private JComboBox<String> comboBoxSchema;
    private JButton btnReload;

    /**
     * Create the panel.
     */
    public ControlPanel() {
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        JLabel labelFontsize = new JLabel(I18n.key("tools.controlpanel.fontsize"));
        this.add(labelFontsize);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 5, 0, 5);
        c.gridwidth = 1;
        layout.setConstraints(labelFontsize, c);

        sliderFontsize = new JSlider();
        this.add(sliderFontsize);
        sliderFontsize.setPaintLabels(true);
        sliderFontsize.setValue(12);
        sliderFontsize.setMinimum(6);
        sliderFontsize.setMaximum(16);
        c.weightx = 1;
        layout.setConstraints(sliderFontsize, c);

        JLabel lblDatabase = new JLabel(I18n.key("tools.controlpanel.database"));
        this.add(lblDatabase);
        c.weightx = 0;
        layout.setConstraints(lblDatabase, c);

        changeDatabase = new JButton(I18n.key("tools.controlpanel.switch"));
        this.add(changeDatabase);

        c.weightx = 0;
        layout.setConstraints(changeDatabase, c);

        JLabel lblCatalog = new JLabel(I18n.key("tools.controlpanel.catalog"));
        this.add(lblCatalog);

        c.weightx = 0;
        layout.setConstraints(lblCatalog, c);

        comboBoxCatalog = new JComboBox<String>();
        this.add(comboBoxCatalog);

        c.weightx = 0;
        layout.setConstraints(comboBoxCatalog, c);

        JLabel lblSchema = new JLabel(I18n.key("tools.controlpanel.schema"));
        this.add(lblSchema);

        c.weightx = 0;
        layout.setConstraints(lblSchema, c);

        comboBoxSchema = new JComboBox<String>();
        this.add(comboBoxSchema);

        c.weightx = 1;
        layout.setConstraints(comboBoxSchema, c);

        btnReload = new JButton(I18n.key("tools.controlpanel.reload"));
        this.add(btnReload);

        c.gridwidth = 0;
        layout.setConstraints(btnReload, c);

    }

    public JSlider getSliderFontsize() {
        return sliderFontsize;
    }

    public JComboBox<String> getComboBoxCatalog() {
        return comboBoxCatalog;
    }

    public JButton getChangeDatabase() {
        return changeDatabase;
    }

    public JComboBox<String> getComboBoxSchema() {
        return comboBoxSchema;
    }

    public JButton getBtnReload() {
        return btnReload;
    }
}
