package com.android.tools.database;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mouse on 2018/11/8.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableAnnotation {

    String colum() default "";

    String type() default BaseDAO.COL_TYPE_TEXT;

    boolean isPrimaryKsy() default false;

}
