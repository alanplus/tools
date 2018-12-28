package com.android.tools.coding;

import com.android.tools.FileTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Mouse on 2018/12/4.
 */
public class GeneratorHelper {

    public static String getPackageName(Class clazz) {
        return clazz.getPackage().getName();
    }

    /**
     * 仅用于android studio 项目
     *
     * @param clazz
     * @return
     */
    public static String getGeneratorFilePath(Class clazz) {
        String path = clazz.getResource(".").getPath();
        int index = path.indexOf("build");
        String packageName = getPackageName(clazz);
        System.out.println(packageName);
        String[] split = packageName.split("\\.");
        String s = path.substring(0, index) + "src" + "/main/java/";
        for (String item : split) {
            s += item + "/";
        }
        return s;
    }

    public static String generatorPackageLine(Class clazz) {
        return "package " + getPackageName(clazz) + ";";
    }

    public static void writeFile(File file, List<String> list) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bufWrite = new BufferedWriter(outWriter);
            for (String s : list) {
                bufWrite.write(s + "\n");
                System.out.println(s);
            }
            bufWrite.close();
            outWriter.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String path, List<String> lines) {
        writeFile(new File(path), lines);
    }

    public static String generatorImprt(String pck) {
        return "import " + pck + ";";
    }

    public static String generatorOverride() {
        return generatorSpace(1) + "@Override";
    }

    public static String generatorSpace(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }

    public static String generatorMethordName(String name, String p, String re, String[] args) {
        re = isEmpty(re) ? "void" : re;
        p = isEmpty(p) ? "public" : p;
        String line = generatorSpace(1) + p + " " + re + " " + name + "(";
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                line += args[i] + " " + args[i].toLowerCase();
            }
        }
        line += "){";
        return line;

    }

    public static String generatorMethordName(String name, String p, String re, String[] args, String[] args1) {
        re = isEmpty(re) ? "void" : re;
        p = isEmpty(p) ? "public" : p;
        String line = generatorSpace(1) + p + " " + re + " " + name + "(";
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String n = args[i];
                String n2 = args1[i];
                line += n + " " + n2;
                if (i != args.length - 1) {
                    line += ",";
                }
            }
        }
        line += "){";
        return line;
    }

    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    public static List<String> readJavaFile(Class clazz) {
        String generatorFilePath = getGeneratorFilePath(clazz);
        return FileTools.readStringList(generatorFilePath + clazz.getSimpleName()+".java");
    }
}
