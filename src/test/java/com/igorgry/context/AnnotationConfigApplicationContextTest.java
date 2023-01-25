package com.igorgry.context;

import com.igorgry.ecxeption.NoSuchBeanException;
import com.igorgry.ecxeption.NoUniqueBeanException;
import com.igorgry.example.FooSubClassService;
import com.igorgry.example.service.BarService;
import com.igorgry.example.service.FooService;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotationConfigApplicationContextTest {

    @Test
    public void getBeanTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example.service");
        assertNotNull(context.getBean(FooService.class));
        assertNotNull(context.getBean(BarService.class));
    }

    @Test
    public void getBeanNoSuchBeanExceptionTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example.service");
        assertThrows(NoSuchBeanException.class, () -> context.getBean(FooSubClassService.class));
    }

    @Test
    public void getBeanNoUniqueBeanExceptionTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example");
        assertThrows(NoUniqueBeanException.class, () -> context.getBean(FooService.class));
    }

    @Test
    public void getBeanByNameTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example.service");
        assertNotNull(context.getBean("fooService", FooService.class));
        assertNotNull(context.getBean("barService", BarService.class));
    }

    @Test
    public void getAllBeansTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example");
        Collection<FooService> fooServices = context.getAllBeans(FooService.class);
        assertEquals(2, fooServices.size());
        Collection<BarService> barServices = context.getAllBeans(BarService.class);
        assertEquals(1, barServices.size());
    }

    @Test
    public void nameResolvingTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example");
        assertNotNull(context.getBean("fooService", FooService.class));
        assertNotNull(context.getBean("barService", BarService.class));
        assertNotNull(context.getBean("customFooServiceName", FooService.class));
    }

    @Test
    public void autowiredTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.igorgry.example.service");
        FooService fooService = context.getBean(FooService.class);
        BarService barService = context.getBean(BarService.class);
        assertEquals(barService.bar(), fooService.foo());
    }
}
