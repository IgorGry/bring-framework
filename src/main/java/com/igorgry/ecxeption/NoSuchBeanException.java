package com.igorgry.ecxeption;

public class NoSuchBeanException extends BeanException {
    public NoSuchBeanException(String msg) {
        super(msg);
    }

    public NoSuchBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
