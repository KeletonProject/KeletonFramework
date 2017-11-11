package org.kucro3.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({CONSTRUCTOR, FIELD, METHOD, PACKAGE, TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Since {
	public int majorVersion();
	
	public int minorVersion();
}
