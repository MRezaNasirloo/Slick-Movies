package com.github.pedramrn.slick.parent.autovalue;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-16
 */

@Retention(SOURCE)
@Target({METHOD, PARAMETER, FIELD})
public @interface IncludeHashEquals {
}