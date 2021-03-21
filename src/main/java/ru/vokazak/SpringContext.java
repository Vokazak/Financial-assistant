package ru.vokazak;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContext {

    private static ApplicationContext context;
    public static ApplicationContext getContext() {
        if (context == null) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("org.postgresql.Driver's class was not found", e);
            }
            context = new AnnotationConfigApplicationContext("ru.vokazak");
        }
        return context;
    }
}
