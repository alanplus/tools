package com.android.tools.coding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mouse on 2018/12/7.
 */
public class ExtenalDbGenerator extends DbGenerator {

    public ExtenalDbGenerator(Class clazz) {
        super(clazz);
    }

    @Override
    public void generatorImplDAOFile() {
        List<String> lines = new ArrayList<>();
        lines.add(GeneratorHelper.generatorPackageLine(clazz));
        lines.add("\n\n");
        lines.add(GeneratorHelper.generatorImprt("android.content.ContentValues"));
        lines.add(GeneratorHelper.generatorImprt("android.content.Context"));
        lines.add(GeneratorHelper.generatorImprt("android.database.Cursor"));
        lines.add(GeneratorHelper.generatorImprt("android.database.sqlite.SQLiteDatabase"));
        lines.add(GeneratorHelper.generatorImprt("com.android.tools.database.DaoConfig"));
        lines.add(GeneratorHelper.generatorImprt("java.util.HashMap"));
        lines.add(GeneratorHelper.generatorImprt("java.util.Map"));
        lines.add(GeneratorHelper.generatorImprt(GeneratorHelper.getPackageName(clazz) + "." + getInterfaceName()));
        lines.add(GeneratorHelper.generatorImprt(clazz.getName()));
        lines.add("\n");
        lines.add("public class " + getImplDAOName() + " extends ExternalBaseDao<" + clazz.getSimpleName() + "> implements " + getInterfaceName() + "{");
        lines.add("\n");
        lines.add(GeneratorHelper.generatorSpace(1) + "public static String TABLE_NAME = \"" + getDatabaseName() + "\";");
        lines.add("\n");
        lines.add(GeneratorHelper.generatorSpace(1) + "public " + getImplDAOName() + "(Context context) {");
        lines.add(GeneratorHelper.generatorSpace(2) + "super(TABLE_NAME, context);");
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
}
