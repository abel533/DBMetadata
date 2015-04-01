package com.github.abel533.controller;

import com.github.abel533.component.DBData;
import com.github.abel533.component.DBServer;
import com.github.abel533.component.DBServerList;
import com.github.abel533.database.Dialect;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.LoadFrame;
import com.github.abel533.view.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzh
 * @since 2015-03-16
 */
public class LoginController implements Controller {
    protected LoginFrame loginFrame;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        DBData.dbServerList = DBServerList.fromDBJson();
        if (DBData.dbServerList != null
                && DBData.dbServerList.getServers() != null
                && DBData.dbServerList.getServers().size() > 0) {
            DBData.dbServers = new ArrayList<String>();
            for (DBServer dbServer : DBData.dbServerList.getServers()) {
                DBData.dbServers.add(dbServer.getName());
            }
        }
    }

    @Override
    public LoginController initView() {
        //初始化可选的驱动
        List<Dialect> dialects = new ArrayList<Dialect>();
        for (Dialect dialect : Dialect.values()) {
            if (dialect.exists()) {
                dialects.add(dialect);
            }
        }
        loginFrame.getLoginPanel().getJdbcName().setModel(new DefaultComboBoxModel<Dialect>(dialects.toArray(new Dialect[]{})));
        boolean selected = false;
        if (DBData.dbServerList != null && DBData.dbServerList.getSelected() != null && DBData.dbServerList.getSelected().length() > 0) {
            if (DBData.dbServerList.getServers() != null && DBData.dbServerList.getServers().size() > 0) {
                for (DBServer dbServer : DBData.dbServerList.getServers()) {
                    if (dbServer.getName().equals(DBData.dbServerList.getSelected())) {
                        dbServer2Form(dbServer);
                        selected = true;
                        break;
                    }
                }
            }
        }
        if (!selected && dialects.size() > 0) {
            loginFrame.getLoginPanel().getJdbcName().setSelectedIndex(0);
            loginFrame.getLoginPanel().getJdbcUrl().setText(dialects.get(0).getSample());
        }
        //初始化列表
        loginFrame.getServerList().setModel(new AbstractListModel<String>() {
            private static final long serialVersionUID = 1L;

            public int getSize() {
                if (DBData.dbServers != null) {
                    return DBData.dbServers.size();
                }
                return 0;
            }

            public String getElementAt(int index) {
                if (index > getSize() || index < 0) {
                    return null;
                }
                return DBData.dbServers.get(index);
            }
        });
        return this;
    }

    @Override
    public LoginController initAction() {
        //选择JDBC
        loginFrame.getLoginPanel().getJdbcName().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    Dialect dialect = (Dialect) loginFrame.getLoginPanel().getJdbcName().getSelectedItem();
                    DBServer dbServer = new DBServer();
                    dbServer.setDialect(dialect);
                    dbServer.setUrl(dialect.getSample());
                    dbServer2Form(dbServer);
                }
            }
        });
        //保存一个配置
        loginFrame.getLoginPanel().getSaveCfg().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginFrame.getLoginPanel().getJdbcUrl().getText().equals("")) {
                    showMessageDialog(I18n.key("tools.login.jdbc.url.empty"));
                    return;
                }
                if (DBData.dbServerList == null) {
                    DBData.dbServerList = new DBServerList();
                    DBData.dbServers = new ArrayList<String>();
                }
                DBServer server = form2DBServer();
                if (DBData.dbServerList != null && DBData.dbServerList.getServers().contains(server)) {
                    //如果重复，就删除原有的配置
                    DBData.dbServerList.getServers().remove(server);
                    for (String dbServer : DBData.dbServers) {
                        if (server.getName().equals(dbServer)) {
                            DBData.dbServers.remove(dbServer);
                            break;
                        }
                    }
                }
                DBData.dbServerList.getServers().add(server);
                DBData.dbServerList.setSelected(server.getName());
                DBData.dbServers.add(server.getName());
                try {
                    DBData.dbServerList.toDBJson();
                    showMessageDialog(I18n.key("tools.login.save.success"));
                } catch (Exception e1) {
                    showMessageDialog(e1.getMessage());
                }
            }
        });
        //删除配置
        loginFrame.getLoginPanel().getDelCfg().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = loginFrame.getServerList().getSelectedValue();
                if (selected == null || selected.equals("")) {
                    return;
                }
                boolean delete = false;
                for (String dbServer : DBData.dbServers) {
                    if (selected.equals(dbServer)) {
                        DBData.dbServers.remove(dbServer);
                        break;
                    }
                }
                for (DBServer dbServer : DBData.dbServerList.getServers()) {
                    if (dbServer.getName().equals(selected)) {
                        DBData.dbServerList.getServers().remove(dbServer);
                        delete = true;
                        break;
                    }
                }
                if (selected.equals(DBData.dbServerList.getSelected())) {
                    DBData.dbServerList.setSelected(null);
                }
                if (!delete) {
                    return;
                }
                try {
                    DBData.dbServerList.toDBJson();
                    showMessageDialog(I18n.key("tools.login.del.success"));
                } catch (Exception e1) {
                    showMessageDialog(e1.getMessage());
                }
            }
        });
        //绑定登录
        bindLogin();
        //绑定列表事件
        bindJList();
        return this;
    }

    protected void bindJList() {
        //双击JList
        loginFrame.getServerList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String selected = loginFrame.getServerList().getSelectedValue();
                    if (selected == null || selected.equals("")) {
                        return;
                    }
                    DBData.dbServerList.setSelected(selected);
                    DBServer dbServer = null;
                    for (DBServer server : DBData.dbServerList.getServers()) {
                        if (server.getName().equals(selected)) {
                            dbServer = server;
                            break;
                        }
                    }
                    if (dbServer != null) {
                        dbServer2Form(dbServer);
                        DBData.dbServerList.toDBJson();
                    }
                }
            }
        });
    }

    protected void bindLogin() {
        //登陆
        loginFrame.getLoginPanel().getLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBServer server = form2DBServer();
                //form2DBServer如果没有选择复选框，就没有密码
                server.setPwd(new String(loginFrame.getLoginPanel().getJdbcPwd().getPassword()));
                DBData.init(server);
                try {
                    if (DBData.testConnection()) {
                        loginFrame.dispose();
                        LoadFrame loadFrame = new LoadFrame();
                        loadFrame.setVisible(true);
                        loadFrame.setLocationRelativeTo(null);
                    } else {
                        showMessageDialog(I18n.key("tools.login.error"));
                    }
                } catch (Exception e1) {
                    showMessageDialog(e1.getMessage());
                    try {
                        Thread.sleep(5000);
                        System.exit(0);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    protected DBServer form2DBServer() {
        DBServer server = new DBServer();
        server.setDialect((Dialect) loginFrame.getLoginPanel().getJdbcName().getSelectedItem());
        server.setUrl(loginFrame.getLoginPanel().getJdbcUrl().getText());
        server.setUser(loginFrame.getLoginPanel().getJdbcUser().getText());
        if (loginFrame.getLoginPanel().getLabelPwd().isSelected()) {
            server.setPwd(new String(loginFrame.getLoginPanel().getJdbcPwd().getPassword()));
        }
        return server;
    }

    protected void dbServer2Form(DBServer dbServer) {
        loginFrame.getLoginPanel().getJdbcName().setSelectedItem(dbServer.getDialect());
        loginFrame.getLoginPanel().getJdbcUrl().setText(dbServer.getUrl());
        loginFrame.getLoginPanel().getJdbcUser().setText(dbServer.getUser());
        String pwd = dbServer.getRealPwd();
        loginFrame.getLoginPanel().getJdbcPwd().setText(pwd);
        if (pwd != null && pwd.length() > 0) {
            loginFrame.getLoginPanel().getLabelPwd().setSelected(true);
        }
    }

    protected void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(loginFrame, msg);
    }
}
