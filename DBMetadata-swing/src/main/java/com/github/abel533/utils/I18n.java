package com.github.abel533.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author liuzh
 */
public class I18n {
    private static final String SUFFIX = ".properties";
    private static ResourceBundle[] resourceBundles;
    private static Set<String> baseNameList;

    static {
        init(null);
    }

    private static void initBaseNameList() {
        File[] files = new File(Path.LANGUAGE).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(SUFFIX)) {
                    return true;
                }
                return false;
            }
        });
        baseNameList = new HashSet<String>();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            int index = fileName.indexOf('_');
            if (index == -1) {
                baseNameList.add("language/" + fileName.substring(0, fileName.lastIndexOf('.')));
            } else {
                baseNameList.add("language/" + fileName.substring(0, index));
            }
        }
    }

    /**
     * 初始化 - 可以设置语言
     *
     * @param locale
     */
    public static void init(Locale locale) {
        if (locale != null) {
            Locale.setDefault(locale);
        }
        if (baseNameList == null || baseNameList.size() == 0) {
            initBaseNameList();
        }
        resourceBundles = new ResourceBundle[baseNameList.size()];
        int i = 0;
        for (String baseName : baseNameList) {
            resourceBundles[i] = ResourceBundle.getBundle(baseName);
            i++;
        }
    }

    public static String key(String key) {
        for (int i = 0; i < resourceBundles.length; i++) {
            if (resourceBundles[i].containsKey(key)) {
                return resourceBundles[i].getString(key);
            }
        }
        throw new RuntimeException("key:" + key + " not exists!");
    }

    /**
     * 可配参数，如：查找{0}字段
     *
     * @param key
     * @param args
     * @return
     */
    public static String key(String key, Object... args) {
        return MessageFormat.format(key(key), args);
    }
}
