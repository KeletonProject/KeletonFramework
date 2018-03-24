package org.kucro3.keleton.security;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sealed {
}
