package org.kucro3.keleton;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({CONSTRUCTOR, FIELD, METHOD, PACKAGE, TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface LastChange {
	public int majorVersion();
	
	public int minorVersion();
}
