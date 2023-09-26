package com.annot;

import java.lang.annotation.*;

/**
 * @author lizhi
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    String value();
}