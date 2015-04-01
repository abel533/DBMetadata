package com.github.abel533.view.panel;

import com.github.abel533.Code;

import javax.swing.*;
import java.awt.*;

public class MsgPane extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	private JTextArea loadMsg;
	
	public MsgPane() {
		loadMsg = new JTextArea();
		loadMsg.setFont(Code.TEXT_MSG);
		loadMsg.setEditable(false);
		loadMsg.setBackground(new Color(0, 0, 0));
		loadMsg.setForeground(new Color(0, 128, 0));
		this.setViewportView(loadMsg);
	}

	public JTextArea getLoadMsg() {
		return loadMsg;
	}
}
