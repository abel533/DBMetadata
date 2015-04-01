package com.github.abel533.view;

import com.github.abel533.ICON;
import com.github.abel533.controller.LoadController;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.panel.ChoosePanel;
import com.github.abel533.view.panel.MsgPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoadFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private MsgPane msgPane;
    private ChoosePanel choosePanel;


    /**
     * Create the dialog.
     */
    public LoadFrame() {
        setTitle(I18n.key("tools.load.title.loading"));
        setIconImage(ICON.LOGO_PNG.getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(cardLayout);

        msgPane = new MsgPane();
        contentPanel.add(msgPane, "msgPane");

        choosePanel = new ChoosePanel();
        contentPanel.add(choosePanel, "choosePanel");

        new LoadController(this).initView().initAction();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public MsgPane getMsgPane() {
        return msgPane;
    }

    public ChoosePanel getChoosePanel() {
        return choosePanel;
    }
}
