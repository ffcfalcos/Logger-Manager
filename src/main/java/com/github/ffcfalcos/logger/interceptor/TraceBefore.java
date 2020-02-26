package com.github.ffcfalcos.logger.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Thomas Beauchataud
 * @since 24.02.2020
 * Annotation to place on a method to trace it before his execution
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TraceBefore {

    /**
     * The PersistingHandlerClass to use to trace the method execution
     * @return Class
     */
    Class<?> persistingHandlerClass() default void.class;

    /**
     * The FormatterHandlerClass to use to trace the method execution
     * @return Class
     */
    Class<?> formatterHandlerClass() default void.class;

}
