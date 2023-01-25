package com.igorgry.ecxeption;

public class NoUniqueBeanException extends BeanException {
    public NoUniqueBeanException(String msg) {
        super(msg);
    }

    public NoUniqueBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
