package org.kucro3.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface CaseInsensitive {
}
