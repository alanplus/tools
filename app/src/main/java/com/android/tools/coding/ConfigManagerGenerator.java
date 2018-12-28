package com.android.tools.coding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mouse on 2018/12/7.
 */
public class ConfigManagerGenerator {

    protected Class clazz;

    public ConfigManagerGenerator(Class clazz) {
        this.clazz = clazz;
    }

    public void addMethord(String key, String type, String defaultValue) {
        List<String> list = GeneratorHelper.readJavaFile(clazz);
        int lastEndCharactor = getLastEndCharactor(list);
        if (lastEndCharactor < 0) {
            return;
        }
        List<String> lines = new ArrayList<>();
        lines.add("\n");
        lines.add(GeneratorHelper.generatorSpace(1) + "private static final String " + getVariableString(key) + " = " + getVariableValueString(key));
        lines.add("\n");
        addGetMethord(lines, type, key, defaultValue);
        addSetMethord(lines, type, key);
        list.addAll(lastEndCharactor, lines);
        String generatorFilePath = GeneratorHelper.getGeneratorFilePath(clazz);
        GeneratorHelper.writeFile(new File(generatorFilePath, clazz.getSimpleName() + ".java"), list);
    }

    protected void addGetMethord(List<String> lines, String type, String key, String defaultValue) {
        lines.add(GeneratorHelper.generatorSpace(1) + "public " + type + " " + getMethordName("get", key) + "(){");
        if ("String".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.getString(" + getVariableString(key) + ",\"" + defaultValue + "\");");
        } else if ("int".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.getInt(" + getVariableString(key) + ", " + defaultValue + ");");
        } else if ("boolean".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.getBoolean(" + getVariableString(key) + ", " + defaultValue + ");");
        } else if ("long".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.getLong(" + getVariableString(key) + ", " + defaultValue + ");");
        }
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }

    protected void addSetMethord(List<String> lines, String type, String key) {
        lines.add(GeneratorHelper.generatorSpace(1) + "public " + type + " " + getMethordName("set", key) + "(" + type + " value){");
        if ("String".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.setString(" + getVariableString(key) + ",value );");
        } else if ("int".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.setInt(" + getVariableString(key) + ",value);");
        } else if ("boolean".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.setBoolean(" + getVariableString(key) + ", value);");
        } else if ("long".equals(type)) {
            lines.add(GeneratorHelper.generatorSpace(2) + "return sp.setLong(" + getVariableString(key) + ", value);");
        }
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }

    protected String getVariableString(String key) {
        return key.toUpperCase();
    }

    protected String getVariableValueString(String key) {
        return key.toLowerCase();
    }

    protected String getMethordName(String pre, String key) {
        key = key.toLowerCase();
        while (key.indexOf("_") >= 0) {
            int i = key.indexOf("_");
            key = key.substring(0, i) + (i + 1 < key.length() ? key.substring(i + 1).toUpperCase() : "") + (i + 2 < key.length() ? key.substring(i + 2) : "");
        }
        return pre + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    protected int getLastEndCharactor(List<String> list) {
        if (null == list || list.size() == 0) {
            return -1;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            String s = list.get(i);
            if (!GeneratorHelper.isEmpty(s) && !GeneratorHelper.isEmpty(s.trim()) && "}".equals(s.trim())) {
                return i;
            }
        }
        return -1;
    }
}
