package com.igorgry.context;

import com.igorgry.ecxeption.BeanException;

import java.util.Collection;

/**
 * Central interface to provide configuration for an application.
 */
public interface ApplicationContext {
    /**
     * Return the bean instance that uniquely matches the given object type, if any
     */
    <T> T getBean(Class<T> requiredType) throws BeanException;

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeanException;

    /**
     * Return all beans instances that uniquely matches the given object type, if any
     */
    <T> Collection<T> getAllBeans(Class<T> requiredType);
}
