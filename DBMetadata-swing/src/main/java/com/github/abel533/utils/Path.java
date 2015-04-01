package com.github.abel533.utils;


/**
 * @author liuzh
 * @since 2015-03-17
 */
public abstract class Path {

    public static final String ROOT;
    public static final String LANGUAGE;
    public static final String db_json;

    static {
        String root = null;
        try {
            root = getRealPath();
        } catch (Exception e) {
            e.printStackTrace();
            root = getProjectPath();
        }
        if (root != null) {
            root = root.replaceAll("\\\\", "/");
        }
        ROOT = root + "/";
        LANGUAGE = ROOT + "language/";
        db_json = ROOT + "/db.json";
    }

    public static String getProjectPath() {
        java.net.URL url = Path.class.getProtectionDomain().getCodeSource()
                .getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar"))
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        java.io.File file = new java.io.File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }

    public static String getRealPath() throws Exception {
        String realPath = Path.class.getClassLoader().getResource("").getFile();
        java.io.File file = new java.io.File(realPath);
        realPath = file.getAbsolutePath();
        realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        return realPath;
    }
}
