package com.github.abel533.view.panel;

import com.github.abel533.Code;
import com.github.abel533.component.JTextF;
import com.github.abel533.component.JTextPwd;
import com.github.abel533.database.Dialect;
import com.github.abel533.utils.I18n;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextF jdbcUrl;
	private JTextF jdbcUser;
	private JTextPwd jdbcPwd;
	private JComboBox<Dialect> jdbcName;
	private JButton saveCfg;
	private JButton delCfg;
	private JButton login;
    private JCheckBox labelPwd;
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		JLabel labelName = new JLabel(I18n.key("tools.login.jdbc.name"));
		this.add(labelName);
		labelName.setHorizontalAlignment(SwingConstants.RIGHT);
		labelName.setBounds(6, 10, 110, 26);
		labelName.setFont(Code.TEXT_BUTTON);

		jdbcName = new JComboBox<Dialect>();
		jdbcName.setBounds(145, 14, 422, 26);
		jdbcName.setFont(Code.TEXT_FONT);
		this.add(jdbcName);
		
		JLabel labelUrl = new JLabel(I18n.key("tools.login.jdbc.url"));
		this.add(labelUrl);
		labelUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		labelUrl.setBounds(6, 56, 110, 26);
		labelUrl.setFont(Code.TEXT_BUTTON);
		
		jdbcUrl = new JTextF();
		jdbcUrl.setBounds(145, 60, 422, 26);
		jdbcUrl.setColumns(10);
		this.add(jdbcUrl);

		JLabel labelUser = new JLabel(I18n.key("tools.login.jdbc.user"));
		this.add(labelUser);
		labelUser.setHorizontalAlignment(SwingConstants.RIGHT);
		labelUser.setBounds(6, 102, 110, 26);
		labelUser.setFont(Code.TEXT_BUTTON);

		jdbcUser = new JTextF();
		jdbcUser.setBounds(145, 106, 422, 26);
		jdbcUser.setColumns(10);
		this.add(jdbcUser);
		
		labelPwd = new JCheckBox(I18n.key("tools.login.jdbc.pwd"));
		labelPwd.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPwd.setBounds(6, 148, 110, 26);
        labelPwd.setFont(Code.TEXT_BUTTON);
        this.add(labelPwd);

		jdbcPwd = new JTextPwd();
		jdbcPwd.setBounds(145, 152, 422, 26);
		jdbcPwd.setColumns(10);
		this.add(jdbcPwd);
		
		JPanel panelEmpty = new JPanel();
		this.add(panelEmpty);
		
		//按钮
		JPanel panelBtn = new JPanel();
		GridBagLayout btnLayout = new GridBagLayout();
		panelBtn.setLayout(btnLayout);
		this.add(panelBtn);
		
		login = new JButton(I18n.key("tools.login.quick"));
		login.setBounds(10, 200, 332, 49);
		login.setFont(Code.TEXT_BUTTON);
		login.setForeground(Color.WHITE);
		login.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.green));
		panelBtn.add(login);

		saveCfg = new JButton(I18n.key("tools.login.save"));
		saveCfg.setBounds(349, 200, 137, 49);
		saveCfg.setFont(Code.TEXT_BUTTON);
		saveCfg.setForeground(Color.WHITE);
		saveCfg.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.blue));
		panelBtn.add(saveCfg);

		delCfg = new JButton(I18n.key("tools.login.del"));
		delCfg.setBounds(484, 209, 63, 49);
		delCfg.setFont(Code.TEXT_BUTTON);
		delCfg.setForeground(Color.WHITE);
		delCfg.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.red));
		panelBtn.add(delCfg);
		
		//布局
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		layout.setConstraints(labelName, c);
		c.gridwidth = 0;
		layout.setConstraints(jdbcName, c);
		c.gridwidth = 1;
		layout.setConstraints(labelUrl, c);
		c.gridwidth = 0;
		layout.setConstraints(jdbcUrl, c);
		c.gridwidth = 1;
		layout.setConstraints(labelUser, c);
		c.gridwidth = 0;
		layout.setConstraints(jdbcUser, c);
		c.gridwidth = 1;
		layout.setConstraints(labelPwd, c);
		c.gridwidth = 0;
		layout.setConstraints(jdbcPwd, c);
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		layout.setConstraints(panelEmpty, c);
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 0;
		layout.setConstraints(panelBtn, c);
		c.insets = new Insets(0, 5, 0, 0);
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		btnLayout.setConstraints(login, c);
		c.weightx = 0;
		c.gridwidth = 1;
		btnLayout.setConstraints(saveCfg, c);
		c.gridwidth = 0;
		btnLayout.setConstraints(delCfg, c);
	}
	public JTextF getJdbcUrl() {
		return jdbcUrl;
	}
	public JTextF getJdbcUser() {
		return jdbcUser;
	}
	public JTextPwd getJdbcPwd() {
		return jdbcPwd;
	}
	public JComboBox<Dialect> getJdbcName() {
		return jdbcName;
	}
	public JButton getSaveCfg() {
		return saveCfg;
	}
	public JButton getDelCfg() {
		return delCfg;
	}
	public JButton getLogin() {
		return login;
	}
	public JCheckBox getLabelPwd() {
		return labelPwd;
	}
}
