package com.github.abel533.view.panel;

import com.github.abel533.Code;
import com.github.abel533.component.JTextF;
import com.github.abel533.utils.I18n;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 主界面 - 查询
 * 
 * @author liuzh
 * @since 2015-03-20
 */
public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextF textSearchName;
    private JTextF textSearchComment;
    private JTextF textChoose;

    private JRadioButton radioTypeTable;
    private JRadioButton radioTypeField;

    private JCheckBox checkBoxQuickSearch;
    private JCheckBox checkBoxCaseSensitive;

    private JRadioButton radioMatchEquals;
    private JRadioButton radioMatchContains;

    private JButton btnSearch;
    private JButton btnCancel;

	/**
	 * Create the this.
	 */
	public SearchPanel() {
        this.setPreferredSize(new Dimension(811, 114));
        this.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(250, 10));
        this.add(panel_2, BorderLayout.EAST);
        panel_2.setLayout(null);

        btnSearch = new JButton(I18n.key("tools.main.btn.search"));
        btnSearch.setBounds(29, 0, 124, 71);
        panel_2.add(btnSearch);
        btnSearch.setFont(Code.TEXT_BUTTON);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));

        btnCancel = new JButton(I18n.key("tools.main.btn.cancel"));
        btnCancel.setBounds(163, 0, 71, 71);
        panel_2.add(btnCancel);
        btnCancel.setFont(Code.TEXT_BUTTON);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));

        checkBoxQuickSearch = new JCheckBox(I18n.key("tools.main.checkbox.quick"));
        checkBoxQuickSearch.setBounds(20, 81, 200, 23);
        panel_2.add(checkBoxQuickSearch);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new EmptyBorder(0, 5, 0, 0));
        panel_3.setPreferredSize(new Dimension(10, 150));
        this.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BorderLayout(0, 0));

        JPanel panel_4 = new JPanel();
        panel_4.setPreferredSize(new Dimension(10, 22));
        panel_3.add(panel_4, BorderLayout.NORTH);
        panel_4.setLayout(new BorderLayout(0, 0));

        JLabel labelSearchType = new JLabel(I18n.key("tools.main.searchtype"));
        panel_4.add(labelSearchType, BorderLayout.WEST);

        JPanel panel_6 = new JPanel();
        panel_4.add(panel_6, BorderLayout.CENTER);
        panel_6.setLayout(null);

        radioTypeTable = new JRadioButton(I18n.key("tools.main.searchtype.table"));
        radioTypeTable.setSelected(true);
        radioTypeTable.setBounds(0, 0, 120, 23);
        panel_6.add(radioTypeTable);

        radioTypeField = new JRadioButton(I18n.key("tools.main.searchtype.field"));
        radioTypeField.setBounds(120, 0, 120, 23);
        panel_6.add(radioTypeField);

        ButtonGroup radioType = new ButtonGroup();
        radioType.add(radioTypeTable);
        radioType.add(radioTypeField);

        JPanel panel_5 = new JPanel();
        panel_5.setBorder(new EmptyBorder(0, 0, 9, 0));
        panel_5.setPreferredSize(new Dimension(10, 66));
        panel_3.add(panel_5, BorderLayout.SOUTH);
        panel_5.setLayout(new BorderLayout(0, 0));

        JLabel labelChoose = new JLabel(I18n.key("tools.main.choose"));
        panel_5.add(labelChoose, BorderLayout.WEST);

        textChoose = new JTextF();
        textChoose.setFont(Code.TEXT_BUTTON);
        panel_5.add(textChoose, BorderLayout.CENTER);
        textChoose.setColumns(10);

        JPanel panel_7 = new JPanel();
        panel_7.setPreferredSize(new Dimension(10, 32));
        panel_5.add(panel_7, BorderLayout.NORTH);
        panel_7.setLayout(new BorderLayout(0, 0));

        JLabel labelMatchType = new JLabel(I18n.key("tools.main.matchtype"));
        panel_7.add(labelMatchType, BorderLayout.WEST);

        JPanel panel_8 = new JPanel();
        panel_7.add(panel_8, BorderLayout.CENTER);
        panel_8.setLayout(null);

        radioMatchEquals = new JRadioButton(I18n.key("tools.main.matchtype.equals"));
        radioMatchEquals.setBounds(0, 6, 120, 23);
        panel_8.add(radioMatchEquals);

        radioMatchContains = new JRadioButton(I18n.key("tools.main.matchtype.contains"));
        radioMatchContains.setSelected(true);
        radioMatchContains.setBounds(120, 6, 120, 23);
        panel_8.add(radioMatchContains);

        ButtonGroup radioMatch = new ButtonGroup();
        radioMatch.add(radioMatchEquals);
        radioMatch.add(radioMatchContains);

        checkBoxCaseSensitive = new JCheckBox(I18n.key("tools.main.casesensitive"));
        checkBoxCaseSensitive.setBounds(240, 6, 160, 23);
        panel_8.add(checkBoxCaseSensitive);

        JLabel label_3 = new JLabel(I18n.key("tools.main.search"));
        panel_3.add(label_3, BorderLayout.WEST);

        JPanel panel_9 = new JPanel();
        panel_9.setPreferredSize(new Dimension(300, 10));
        panel_3.add(panel_9, BorderLayout.EAST);
        panel_9.setLayout(new BorderLayout(0, 0));

        JLabel label_5 = new JLabel(I18n.key("tools.main.search.comment"));
        label_5.setBorder(new EmptyBorder(0, 15, 0, 5));
        panel_9.add(label_5, BorderLayout.WEST);

        textSearchComment = new JTextF();
        panel_9.add(textSearchComment, BorderLayout.CENTER);
        textSearchComment.setColumns(10);

        JPanel panel_10 = new JPanel();
        panel_3.add(panel_10, BorderLayout.CENTER);
        panel_10.setLayout(new BorderLayout(0, 0));

        JLabel label_4 = new JLabel(I18n.key("tools.main.search.name"));
        label_4.setBorder(new EmptyBorder(0, 0, 0, 5));
        panel_10.add(label_4, BorderLayout.WEST);

        textSearchName = new JTextF();
        panel_10.add(textSearchName, BorderLayout.CENTER);
        textSearchName.setColumns(10);
	}

	public JTextF getTextSearchName() {
		return textSearchName;
	}

	public JTextF getTextSearchComment() {
		return textSearchComment;
	}

	public JTextF getTextChoose() {
		return textChoose;
	}

	public JRadioButton getRadioTypeTable() {
		return radioTypeTable;
	}

	public JRadioButton getRadioTypeField() {
		return radioTypeField;
	}

	public JCheckBox getCheckBoxQuickSearch() {
		return checkBoxQuickSearch;
	}

	public JCheckBox getCheckBoxCaseSensitive() {
		return checkBoxCaseSensitive;
	}

	public JRadioButton getRadioMatchEquals() {
		return radioMatchEquals;
	}

	public JRadioButton getRadioMatchContains() {
		return radioMatchContains;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}
}
