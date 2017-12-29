package org.kucro3.keleton.implementation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KeletonModule {
    public String name();

    public String[] dependencies() default {};
}
