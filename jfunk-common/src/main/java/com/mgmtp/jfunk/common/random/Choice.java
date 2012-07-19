package com.mgmtp.jfunk.common.random;

import com.google.common.collect.Lists;

/**
 * Extension of {@link RandomCollection}. The collection only contains {@link Boolean#TRUE} and
 * {@link Boolean#FALSE}.
 * 
 * @version $Id$
 */
public class Choice extends RandomCollection<Boolean> {

	public Choice(final MathRandom random) {
		super(random, Lists.newArrayList(Boolean.TRUE, Boolean.FALSE));
	}
}