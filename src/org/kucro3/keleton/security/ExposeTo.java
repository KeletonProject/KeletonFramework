package org.kucro3.keleton.security;

import java.lang.annotation.*;
import java.security.Permission;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeTo {
    public Class<? extends Permission>[] value();
}
