package com.igorgry.ecxeption;

/**
 * Abstract superclass for all exceptions thrown in the beans package
 * and subpackages.
 */
public abstract class BeanException extends RuntimeException {

    public BeanException(String msg) {
        super(msg);
    }

    public BeanException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
