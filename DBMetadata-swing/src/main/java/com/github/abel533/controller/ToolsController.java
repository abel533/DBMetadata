package com.github.abel533.controller;

import com.github.abel533.component.DBData;
import com.github.abel533.component.DBServer;
import com.github.abel533.database.AbstractDatabaseProcess;
import com.github.abel533.database.DatabaseConfig;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.database.MatchType;
import com.github.abel533.service.ToolsService;
import com.github.abel533.utils.I18n;
import com.github.abel533.utils.StringUtils;
import com.github.abel533.view.LoginFrame;
import com.github.abel533.view.ToolsFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liuzh
 * @since 2015-03-16
 */
@SuppressWarnings("serial")
public class ToolsController implements Controller {
    private ToolsFrame toolsFrame;
    private ToolsService toolsService;

    public ToolsController(ToolsFrame toolsFrame) {
        this.toolsFrame = toolsFrame;
        this.toolsService = new ToolsService();
    }

    @Override
    public ToolsController initView() {
        initControlComboBox();
        toolsFrame.getTableSplitPane().getDbTable().setModel(toolsService.getDbTableModel());
        toolsFrame.getTableSplitPane().getDbField().setModel(toolsService.getDbFieldModel());
        //table列
        toolsService.prcessDbFieldHeader(toolsFrame.getTableSplitPane().getDbField());
        toolsService.loadAll();
        return this;
    }

    private void initControlComboBox() {
        //初始化最下面的选择面板
        toolsFrame.getControlPanel().getComboBoxCatalog().setModel(DBData.getCatalogModel());
        toolsFrame.getControlPanel().getComboBoxSchema().setModel(DBData.getSchemaModel());
        //设置已经选中的catalog和schema
        if (DBData.config != null) {
            if (StringUtils.isNotEmpty(DBData.config.getCatalog())) {
                toolsFrame.getControlPanel().getComboBoxCatalog().setSelectedItem(DBData.convertLetterByCase(DBData.config.getCatalog()));
            }
            if (StringUtils.isNotEmpty(DBData.config.getSchemaPattern())) {
                toolsFrame.getControlPanel().getComboBoxSchema().setSelectedItem(DBData.convertLetterByCase(DBData.config.getSchemaPattern()));
            }
        }
    }

    @Override
    public ToolsController initAction() {
        //查询
        toolsFrame.getSearchPanel().getBtnSearch().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        //取消
        toolsFrame.getSearchPanel().getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolsService.loadAll();
                toolsFrame.getSearchPanel().getTextSearchName().setText("");
                toolsFrame.getSearchPanel().getTextSearchComment().setText("");
                toolsFrame.getSearchPanel().getTextChoose().setText("");
            }
        });
        //名字输入框的监听
        toolsFrame.getSearchPanel().getTextSearchName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (toolsFrame.getSearchPanel().getCheckBoxQuickSearch().isSelected()) {
                    search();
                }
            }
        });
        //注释输入框的监听
        toolsFrame.getSearchPanel().getTextSearchComment().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (toolsFrame.getSearchPanel().getCheckBoxQuickSearch().isSelected()) {
                    search();
                }
            }
        });
        //表
        toolsFrame.getTableSplitPane().getDbTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = toolsFrame.getTableSplitPane().getDbTable().getSelectedRow();
                toolsService.loadTableColumnsByRowIndex(row);
                if (e.getClickCount() > 1) {
                    toolsFrame.getSearchPanel().getTextChoose().setText(toolsService.getChooseTableTextByRowIndex(row));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int r = toolsFrame.getTableSplitPane().getDbTable().rowAtPoint(e.getPoint());
                if (r >= 0 && r < toolsFrame.getTableSplitPane().getDbTable().getRowCount()) {
                    toolsFrame.getTableSplitPane().getDbTable().setRowSelectionInterval(r, r);
                } else {
                    toolsFrame.getTableSplitPane().getDbTable().clearSelection();
                }

                final int rowindex = toolsFrame.getTableSplitPane().getDbTable().getSelectedRow();
                if (rowindex < 0)
                    return;
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    final String tableName = toolsService.getTableNameByRowIndex(rowindex);

                    JPopupMenu popup = new JPopupMenu();
                    popup.add(new JMenuItem(I18n.key("tools.main.table.popmenu.export", tableName)) {{
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JOptionPane.showMessageDialog(toolsFrame, I18n.key("tools.main.table.popmenu.export.msg"));
                            }
                        });
                    }});
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        //字段
        toolsFrame.getTableSplitPane().getDbField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    int row = toolsFrame.getTableSplitPane().getDbField().getSelectedRow();
                    toolsFrame.getSearchPanel().getTextChoose().setText(toolsService.getChooseFieldTextByRowIndex(row));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int r = toolsFrame.getTableSplitPane().getDbField().rowAtPoint(e.getPoint());
                if (r >= 0 && r < toolsFrame.getTableSplitPane().getDbField().getRowCount()) {
                    toolsFrame.getTableSplitPane().getDbField().setRowSelectionInterval(r, r);
                } else {
                    toolsFrame.getTableSplitPane().getDbField().clearSelection();
                }

                final int rowindex = toolsFrame.getTableSplitPane().getDbField().getSelectedRow();
                if (rowindex < 0)
                    return;
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    final String fieldName = toolsService.getFieldNameByRowIndex(rowindex);
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(new JMenuItem(I18n.key("tools.main.field.popmenu.search", fieldName)) {{
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                toolsFrame.getSearchPanel().getRadioMatchEquals().setSelected(true);
                                toolsFrame.getSearchPanel().getRadioTypeField().setSelected(true);
                                toolsFrame.getSearchPanel().getTextSearchName().setText(fieldName);
                                toolsFrame.getSearchPanel().getTextSearchComment().setText("");
                                search();
                            }
                        });
                    }});
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        //调整字体大小
        toolsFrame.getControlPanel().getSliderFontsize().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = toolsFrame.getControlPanel().getSliderFontsize().getValue();
                Font font = new Font(I18n.key("tools.text.font"), Font.PLAIN, value);
                toolsFrame.getTableSplitPane().getDbField().setFont(font);
                toolsFrame.getTableSplitPane().getDbTable().setFont(font);
            }
        });

        //控制面板操作
        //切换数据库
        toolsFrame.getControlPanel().getChangeDatabase().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        LoginFrame loginFrame = new LoginFrame();
                        LoginController controller = new LoginController(loginFrame) {
                            @Override
                            protected void bindLogin() {
                                this.loginFrame.getLoginPanel().getLogin().addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        DBServer server = form2DBServer();
                                        //form2DBServer如果没有选择复选框，就没有密码
                                        server.setPwd(new String(loginFrame.getLoginPanel().getJdbcPwd().getPassword()));
                                        DBData.init(server);
                                        try {
                                            if (DBData.testConnection()) {
                                                initControlComboBox();
                                                if (DBData.config == null) {
                                                    JOptionPane.showMessageDialog(toolsFrame, I18n.key("tools.load.label.tip"));
                                                    toolsService.clearAll();
                                                } else {
                                                    initTable();
                                                }
                                                loginFrame.dispose();
                                            } else {
                                                showMessageDialog(I18n.key("tools.login.error"));
                                            }
                                        } catch (Exception e1) {
                                            showMessageDialog(e1.getMessage());
                                        }
                                    }
                                });
                            }
                        };
                        // 绑定控制层并初始化
                        controller.initView().initAction();
                        loginFrame.setLocationRelativeTo(null);
                        loginFrame.setVisible(true);
                    }
                });
            }
        });
        //重新载入数据
        toolsFrame.getControlPanel().getBtnReload().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String catalog = StringUtils.getNotNull(toolsFrame.getControlPanel().getComboBoxCatalog().getSelectedItem());
                String schema = StringUtils.getNotNull(toolsFrame.getControlPanel().getComboBoxSchema().getSelectedItem());
                DBData.config = new DatabaseConfig(catalog, schema);
                initTable();
            }
        });
        return this;
    }

    public synchronized void initTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBData.config.setDatabaseProcess(new AbstractDatabaseProcess() {
                        @Override
                        public void processStart() {
                            enableAllComponent(toolsFrame.getControlPanel(), false);
                            toolsFrame.getControlPanel().getBtnReload().setText(I18n.key("tools.controlpanel.reload.loading"));
                        }

                        @Override
                        public void processComplete(List<IntrospectedTable> introspectedTables) {
                            enableAllComponent(toolsFrame.getControlPanel(), true);
                            toolsFrame.getControlPanel().getBtnReload().setText(I18n.key("tools.controlpanel.reload"));
                        }
                    });
                    DBData.initTables();
                    toolsService.loadAll();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(toolsFrame, e1.getMessage());
                }
            }
        }).start();
    }

    private void enableAllComponent(JComponent parent, boolean enable) {
        for (Component component : parent.getComponents()) {
            component.setEnabled(enable);
        }
    }

    public synchronized void search() {
        String searchText = toolsFrame.getSearchPanel().getTextSearchName().getText();
        String searchComment = toolsFrame.getSearchPanel().getTextSearchComment().getText();
        MatchType matchType = toolsFrame.getSearchPanel().getRadioMatchEquals().isSelected() ? MatchType.EQUALS : MatchType.CONTAINS;
        boolean table = toolsFrame.getSearchPanel().getRadioTypeTable().isSelected();
        boolean caseSensitive = toolsFrame.getSearchPanel().getCheckBoxCaseSensitive().isSelected();
        toolsService.search(searchText, searchComment, matchType, table, caseSensitive);
    }

}
