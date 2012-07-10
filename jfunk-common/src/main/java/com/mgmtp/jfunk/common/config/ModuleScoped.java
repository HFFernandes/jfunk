package com.mgmtp.jfunk.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * Annotation for {@link StackedScope}. Classes annotated with this annotation get module scope.
 * 
 * @see com.google.inject.Scope
 * @author rnaegele
 * @version $Id$
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface ModuleScoped {
	// marker annotation
}