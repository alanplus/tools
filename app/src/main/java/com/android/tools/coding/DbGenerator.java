package com.android.tools.coding;

import com.android.tools.database.TableAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mouse on 2018/12/4.
 */
public class DbGenerator {

    protected Class clazz;

    public DbGenerator(Class clazz) {
        this.clazz = clazz;
    }

    public void generator() {
        generatorInterfaceDAOFile();
        generatorImplDAOFile();
    }

    public void generatorInterfaceDAOFile() {
        List<String> lines = new ArrayList<>();
        lines.add(GeneratorHelper.generatorPackageLine(clazz));
        lines.add("\n\n");
        lines.add(GeneratorHelper.generatorImprt("com.android.tools.database.IBaseDAO"));
        lines.add(GeneratorHelper.generatorImprt(clazz.getName()));
        lines.add("\n");
        lines.add("public interface " + getInterfaceName() + " extends IBaseDAO<" + clazz.getSimpleName() + "> {");
        lines.add("\n");
        lines.add("    boolean save(" + clazz.getSimpleName() + " " + clazz.getSimpleName().toLowerCase() + ");");
        lines.add("}");
        String path = GeneratorHelper.getGeneratorFilePath(clazz) + getInterfaceName() + ".java";
        GeneratorHelper.writeFile(path, lines);
    }

    protected String getInterfaceName() {
        return "I" + clazz.getSimpleName() + "DAO";
    }

    protected String getImplDAOName() {
        return clazz.getSimpleName() + "DAOImpl";
    }

    protected String getDatabaseName() {
        String simpleName = clazz.getSimpleName().replaceAll("[A-Z]", "_$0").toLowerCase();
        return simpleName.startsWith("_") ? simpleName = simpleName.substring(1) : simpleName;
    }

    public void generatorImplDAOFile() {
        List<String> lines = new ArrayList<>();
        lines.add(GeneratorHelper.generatorPackageLine(clazz));
        lines.add("\n\n");
        lines.add(GeneratorHelper.generatorImprt("android.content.ContentValues"));
        lines.add(GeneratorHelper.generatorImprt("android.content.Context"));
        lines.add(GeneratorHelper.generatorImprt("android.database.Cursor"));
        lines.add(GeneratorHelper.generatorImprt("android.database.sqlite.SQLiteDatabase"));
        lines.add(GeneratorHelper.generatorImprt("com.android.tools.database.BaseDAO"));
        lines.add(GeneratorHelper.generatorImprt("com.android.tools.database.DaoConfig"));
        lines.add(GeneratorHelper.generatorImprt(GeneratorHelper.getPackageName(clazz) + "." + getInterfaceName()));
        lines.add(GeneratorHelper.generatorImprt(clazz.getName()));
        lines.add("\n");
        lines.add("public class " + getImplDAOName() + " extends BaseDAO<" + clazz.getSimpleName() + "> implements " + getInterfaceName() + "{");
        lines.add("\n");
        lines.add(GeneratorHelper.generatorSpace(1) + "public static String TABLE_NAME = \"" + getDatabaseName() + "\";");
        lines.add("\n");
        lines.add(GeneratorHelper.generatorSpace(1) + "public " + getImplDAOName() + "(Context context) {");
        lines.add(GeneratorHelper.generatorSpace(2) + "super(TABLE_NAME, context, DaoConfig.getConfig().getDatabaseConfig());");
        lines.add("    }");
        lines.add("\n");
        generatorOnCreatMethord(lines);
        lines.add("\n");
        generatorGetContentValue(lines);
        lines.add("\n");
        generatorSaveMethord(lines);
        lines.add("\n");
        generatorFindByCursorMethord(lines);
        lines.add("\n");
        lines.add("}");
        String path = GeneratorHelper.getGeneratorFilePath(clazz) + getImplDAOName() + ".java";
        GeneratorHelper.writeFile(path, lines);
    }

    protected void generatorOnCreatMethord(List<String> lines) {
        lines.add(GeneratorHelper.generatorOverride());
        lines.add(GeneratorHelper.generatorMethordName("onCreate", "", "", new String[]{"SQLiteDatabase"}));
        lines.add(GeneratorHelper.generatorSpace(2) + "Map<String, String> columns = new HashMap<String, String>();");
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableAnnotation.class)) {
                TableAnnotation table = field.getAnnotation(TableAnnotation.class);
                String colum = table.colum();
                String type = table.type();
                boolean primaryKsy = table.isPrimaryKsy();
                lines.add(GeneratorHelper.generatorSpace(2) + "columns.put(\"" + colum + "\", \"" + type + "\");");
                if (primaryKsy) {
                    stringBuilder.append(colum);
                    stringBuilder.append(",");
                }
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            lines.add(GeneratorHelper.generatorSpace(2) + "columns.put(SQLiteManager.SQLiteTable.COL_TYPE_PRIMARY_KEY, \"(" + stringBuilder.toString() + ")\");");
        }
        lines.add(GeneratorHelper.generatorSpace(2) + "createTable(sqlitedatabase,columns);");
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }

    protected void generatorSaveMethord(List<String> lines) {
        lines.add(GeneratorHelper.generatorOverride());
        lines.add(GeneratorHelper.generatorMethordName("save", "", "boolean", new String[]{clazz.getSimpleName()}));
        lines.add(GeneratorHelper.generatorSpace(2) + "return save(getContentValues(" + clazz.getSimpleName().toLowerCase() + "))>0;");
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }

    protected void generatorGetContentValue(List<String> lines) {
        lines.add(GeneratorHelper.generatorMethordName("getContentValues", "", "ContentValues", new String[]{clazz.getSimpleName()}));
        lines.add(GeneratorHelper.generatorSpace(2) + "ContentValues values = new ContentValues();");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableAnnotation.class)) {
                TableAnnotation annotation = field.getAnnotation(TableAnnotation.class);
                String colum = annotation.colum();
                String name = field.getName();
                lines.add(GeneratorHelper.generatorSpace(2) + "values.put(\"" + colum + "\"," + clazz.getSimpleName().toLowerCase() + "." + name + ");");
            }
        }
        lines.add(GeneratorHelper.generatorSpace(2) + "return values;");
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }

    protected void generatorFindByCursorMethord(List<String> lines) {
        lines.add(GeneratorHelper.generatorOverride());
        lines.add(GeneratorHelper.generatorMethordName("findByCursor", "protected", clazz.getSimpleName(), new String[]{"Cursor", "int"}, new String[]{"cursor", "n"}));
        lines.add(GeneratorHelper.generatorSpace(2) + clazz.getSimpleName() + " " + clazz.getSimpleName().toLowerCase() + " = new " + clazz.getSimpleName() + "();");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            TableAnnotation annotation = field.getAnnotation(TableAnnotation.class);
            String name = clazz.getSimpleName().toLowerCase() + "." + field.getName() + " = ";
            String type = annotation.type();
            String values = "";
            switch (type) {
                case "INTEGER PRIMARY KEY":
                case "COL_TYPE_INT":
                    values = "getIntFromCusor(cursor, \"" + annotation.colum() + "\");";
                    break;
                case "TEXT":
                    values = "getStringFromCusor(cursor, \"" + annotation.colum() + "\");";
                    break;
                case "COL_TYPE_LONG":
                    values = "getLongFromCusor(cursor, \"" + annotation.colum() + "\");";
                    break;
                case "COL_TYPE_FLOAT":
                    values = "getFloatFromCusor(cursor, \"" + annotation.colum() + "\");";
                    break;
            }
            lines.add(GeneratorHelper.generatorSpace(2) + name + values);
        }
        lines.add(GeneratorHelper.generatorSpace(2) + "return " + clazz.getSimpleName().toLowerCase() + ";");
        lines.add(GeneratorHelper.generatorSpace(1) + "}");
    }
}
