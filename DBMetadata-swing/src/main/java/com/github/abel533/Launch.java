package com.github.abel533;

import com.github.abel533.controller.LoginController;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.LoginFrame;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.utils.Platform;

import javax.swing.*;

public class Launch {
    private static void initUserInterface() {
        try {
            if (Platform.isWindows()) {
                UIManager.put("RootPane.setupButtonVisible", false);
                BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
                BeautyEyeLNFHelper.launchBeautyEyeLNF();
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initClass() {
        try {
            Class.forName(I18n.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        initClass();
        initUserInterface();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                // 绑定控制层并初始化
                new LoginController(loginFrame).initView().initAction();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });
    }
}