package com.github.abel533.component;

import com.github.abel533.database.Dialect;
import com.github.abel533.utils.EncrypDES;
import com.github.abel533.utils.I18n;

public class DBServer {
    private Dialect dialect;
    private String name;
    private String url;
    private String user;
    private String pwd;
    private transient String realPwd;

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public String getName() {
        if (name == null || name.length() == 0) {
            name = dialect + "-" + url + "-" + user;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        if (pwd == null || pwd.length() == 0) {
            return "";
        }
        try {
            EncrypDES.Decryptor(pwd);
        } catch (Exception e) {
            try {
                pwd = EncrypDES.Encrytor(pwd);
            } catch (Exception ex) {
                throw new RuntimeException(I18n.key("tools.pwd.decryptor.error"));
            }
        }
        return pwd;
    }

    public String getRealPwd() {
        try {
            realPwd = EncrypDES.Decryptor(pwd);
            return realPwd;
        } catch (Exception e) {
            return pwd;
        }
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBServer)) return false;

        DBServer dbServer = (DBServer) o;

        if (!getName().equals(dbServer.getName())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
