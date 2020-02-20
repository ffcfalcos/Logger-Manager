package com.github.ffcfalcos.logger.interceptor;

import com.github.ffcfalcos.logger.handler.formatter.FormatterHandler;
import com.github.ffcfalcos.logger.handler.persisting.PersistingHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SuppressWarnings("rawtypes")
public @interface TraceBefore {

    Class persistingHandlerClass();

    Class formatterHandlerClass();

}
