package com.android.tools.exception;

/**
 * Created by Mouse on 2019/4/17.
 */
public class XmException extends Exception {

    private int code;
    private String desciption;

    public XmException(int code, String desciption) {
        super(desciption);
        this.code = code;
        this.desciption = desciption;
    }

    public XmException(String desciption) {
        this(0,desciption);
    }

    public int getCode() {
        return code;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
