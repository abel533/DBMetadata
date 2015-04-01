package com.github.abel533.controller;

import com.github.abel533.component.DBData;
import com.github.abel533.database.AbstractDatabaseProcess;
import com.github.abel533.database.DatabaseConfig;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.LoadFrame;
import com.github.abel533.view.ToolsFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class LoadController implements Controller {
    private LoadFrame loadFrame;

    public LoadController(LoadFrame loadFrame) {
        super();
        this.loadFrame = loadFrame;
    }

    public Controller initView() {
        if (DBData.config == null) {
            loadFrame.setTitle(I18n.key("tools.load.title.choose"));
            loadFrame.getChoosePanel().getComboBoxCatalog().setModel(new DefaultComboBoxModel<String>(DBData.catalogs.toArray(new String[]{})));
            loadFrame.getChoosePanel().getComboBoxSchema().setModel(new DefaultComboBoxModel<String>(DBData.schemas.toArray(new String[]{})));
            //让用户选择
            loadFrame.getCardLayout().last(loadFrame.getContentPanel());
        } else {
            loadData(DBData.config);
        }
        return this;
    }

    private synchronized void loadData(DatabaseConfig config) {
        new Thread(new LoadData(config)).start();
    }

    public Controller initAction() {
        loadFrame.getChoosePanel().getButtonOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object catalog = loadFrame.getChoosePanel().getComboBoxCatalog().getSelectedItem();
                Object schema = loadFrame.getChoosePanel().getComboBoxSchema().getSelectedItem();
                loadFrame.setTitle(I18n.key("tools.load.title.loading"));
                loadFrame.getCardLayout().first(loadFrame.getContentPanel());
                loadData(new DatabaseConfig(catalog == null ? null : catalog.toString(), schema == null ? null : schema.toString()));
            }
        });

        loadFrame.getMsgPane().getLoadMsg().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadFrame.getMsgPane().getLoadMsg().setCaretPosition(e.getDocument().getLength() - 1);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        return this;
    }

    private class LoadData implements Runnable {
        public LoadData(DatabaseConfig config) {
            DBData.config = config;
            DBData.config.setDatabaseProcess(new AbstractDatabaseProcess() {
                @Override
                public void processTable(IntrospectedTable table) {
                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append(I18n.key("tools.load.ctrl.process.table")).append(table.getName()).append("\n");
                    loadFrame.getMsgPane().getLoadMsg().append(msgBuilder.toString());
                }

                @Override
                public void processComplete(List<IntrospectedTable> introspectedTables) {
                    loadFrame.setTitle(I18n.key("tools.load.title.complete"));
                    loadFrame.getMsgPane().getLoadMsg().append(I18n.key("tools.load.ctrl.process.complete"));
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void run() {
            try {
                //初始化表
                DBData.initTables();
                //销毁载入窗口
                loadFrame.setVisible(false);
                loadFrame.dispose();
                //打开主窗口
                ToolsFrame toolsFrame = new ToolsFrame();
                toolsFrame.setLocationRelativeTo(null);
                toolsFrame.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

}
