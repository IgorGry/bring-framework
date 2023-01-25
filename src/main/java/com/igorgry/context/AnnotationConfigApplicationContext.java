package com.igorgry.context;

import com.igorgry.annotation.Autowired;
import com.igorgry.annotation.Bean;
import com.igorgry.ecxeption.BeanException;
import com.igorgry.ecxeption.NoSuchBeanException;
import com.igorgry.ecxeption.NoUniqueBeanException;
import org.reflections.Reflections;

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements ApplicationContext {
    private final Map<String, Object> beansByNameMap = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(String basePackage) throws Exception {
        initializeBeanInstances(basePackage);
        injectBeanDependencies();
    }

    private void initializeBeanInstances(String basePackage) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        //1. Scan base package
        var reflections = new Reflections(basePackage);
        //2. Find all classes marked with @Bean
        var beanClasses = reflections.getTypesAnnotatedWith(Bean.class);
        //3. Create instance of com.igorgry.example.service classes
        for (var beanClass : beanClasses) {
            Constructor<?> constructor = beanClass.getConstructor();
            var bean = constructor.newInstance();
            var beanName = beanClass.getAnnotation(Bean.class).value();
            if (beanName.isEmpty()) {
                beanName = Introspector.decapitalize(beanClass.getSimpleName());
            }
            //4. Store those instances in some map
            beansByNameMap.put(beanName, bean);
        }
    }

    private void injectBeanDependencies() throws IllegalAccessException {
        //5. Find instances that have fields marked with @Autowired
        for (var entry : beansByNameMap.entrySet()) {
            Object bean = entry.getValue();
            var beanClass = bean.getClass();
            for (var field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    var autowiredBean = getBean(field.getType());
                    //6. Set those fields using the existing map
                    field.set(bean, autowiredBean);
                }
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        final Collection<T> allBeansWithRequiredType = getAllBeans(requiredType);
        if (allBeansWithRequiredType.isEmpty()) {
            throw new NoSuchBeanException("No qualifying bean of type '" + requiredType + "' available");
        } else if (allBeansWithRequiredType.size() > 1) {
            String commaSeparatedBeanNames = allBeansWithRequiredType.stream()
                    .map(bean -> bean.getClass().getSimpleName())
                    .collect(Collectors.joining(","));
            throw new NoUniqueBeanException("Expected single matching of qualifying bean of type " + requiredType +
                    " but found " + allBeansWithRequiredType.size() + ": " + commaSeparatedBeanNames);
        } else {
            return allBeansWithRequiredType.iterator().next();
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeanException {
        return beansByNameMap.entrySet().stream()
                .filter(e -> e.getKey().equals(name) && requiredType.isAssignableFrom(e.getValue().getClass()))
                .map(Map.Entry::getValue)
                .map(requiredType::cast)
                .findAny()
                .orElseThrow(() -> new NoSuchBeanException("No bean named '" + name + "' of type '" + requiredType +
                        "' available"));
    }

    @Override
    public <T> Collection<T> getAllBeans(Class<T> requiredType) {
        return beansByNameMap.values()
                .stream()
                .filter(o -> requiredType.isAssignableFrom(o.getClass()))
                .map(requiredType::cast)
                .toList();
    }
}
