package com.github.abel533.view;

import com.github.abel533.ICON;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.panel.LoginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JList<String> serverList;
	private LoginPanel loginPanel;

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setIconImage(ICON.LOGO_PNG.getImage());
		setPreferredSize(new Dimension(880, 340));
		setMinimumSize(new Dimension(880, 340));
		setTitle(I18n.key("tools.login.title"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 880, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelServerList = new JPanel();
		panelServerList.setPreferredSize(new Dimension(300, 10));
		panelServerList.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelServerList.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setPreferredSize(new Dimension(200, 2));

		serverList = new JList<String>();
        serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(serverList);

		JLabel label = new JLabel(I18n.key("tools.login.serverList"));
		panelServerList.add(label, BorderLayout.NORTH);


		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		contentPane.add(splitPane, BorderLayout.CENTER);

		loginPanel = new LoginPanel();
		splitPane.setRightComponent(panelServerList);
		splitPane.setLeftComponent(loginPanel);
	}

	public JList<String> getServerList() {
		return serverList;
	}

	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
}
