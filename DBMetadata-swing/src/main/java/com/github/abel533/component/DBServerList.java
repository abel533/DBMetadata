package com.github.abel533.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.abel533.utils.I18n;
import com.github.abel533.utils.Path;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DBServerList {
    private Set<DBServer> servers;
    private String selected;

    public DBServerList() {
        this.servers = new HashSet<DBServer>();
    }

    public Set<DBServer> getServers() {
        return servers;
    }

    public void setServers(Set<DBServer> servers) {
        this.servers = servers;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void toDBJson() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(Path.db_json);
            JSON.writeJSONStringTo(this, writer, SerializerFeature.PrettyFormat);
        } catch (IOException e) {
            throw new RuntimeException(I18n.key("tools.login.save.failure"));
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static DBServerList fromDBJson() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Path.db_json));
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line).append("\n");
            }
            if (jsonBuilder.length() == 0) {
                return null;
            }
            DBServerList dbServerList = JSON.parseObject(jsonBuilder.toString(), DBServerList.class);
            return dbServerList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(I18n.key("tools.login.serverList.read.error"));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
