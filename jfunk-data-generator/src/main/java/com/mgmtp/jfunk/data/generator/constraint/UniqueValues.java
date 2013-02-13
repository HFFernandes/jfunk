/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.data.generator.constraint;

import java.util.Set;
import java.util.TreeSet;

import org.jdom.Element;

import com.mgmtp.jfunk.common.random.MathRandom;
import com.mgmtp.jfunk.data.generator.Generator;
import com.mgmtp.jfunk.data.generator.control.FieldCase;

/**
 * A constraint that takes care that the values of the embedded field object are always unique.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code 
 * <constraint id="c1" class="com.mgmtp.jfunk.data.generator.constraint.UniqueValues">
 *   <field class="com.mgmtp.jfunk.data.generator.field.InverseExpression">
 *     <expression>[A-Z]</expression>
 *   </field>
 * </constraint>
 * }
 * </pre>
 * 
 * There can be 26 different values for c1, more runs would result in an exception being thrown as
 * the values would not be unique anymore.
 * 
 * @version $Id$
 */
public class UniqueValues extends FieldContainer {

	private final Set<String> set;

	public UniqueValues(final MathRandom random, final Element element, final Generator generator) {
		super(random, element, generator);
		set = new TreeSet<String>();
	}

	/**
	 * Calls initValuesImpl on the super class. If the returned value is already in the list of
	 * values initValuesImpl is called again. This happens up to 10000 times. If a new value is
	 * found it is stored in the set.
	 */
	@Override
	protected String initValuesImpl(final FieldCase ca) {
		if (ca == FieldCase.NULL || ca == FieldCase.BLANK) {
			return null;
		}
		String s = super.initValuesImpl(ca);
		int counter = 10000;
		while (set.contains(s) && counter > 0) {
			s = super.initValuesImpl(ca);
			counter--;
		}
		if (counter == 0) {
			throw new IllegalStateException("Could not generate unique value");
		}
		set.add(s);
		return s;
	}

	/**
	 * Calls resetCase on the super class and erases all entries in the value set.
	 */
	@Override
	public void resetCase() {
		super.resetCase();
		set.clear();
	}
}