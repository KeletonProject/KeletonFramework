package org.kucro3.keleton.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
    public String id();

    public String[] dependencies() default {};

    public boolean supportDisabling() default false;

    public String version() default "";

    public String description() default "";

    public String[] authors() default {};
}
