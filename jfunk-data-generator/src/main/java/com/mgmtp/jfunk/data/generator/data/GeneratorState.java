/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.data.generator.data;

import com.mgmtp.jfunk.common.random.MathRandom;

/**
 */
public class GeneratorState {
	private MathRandom mathRandom;

	protected static final ThreadLocal<GeneratorState> STATE_HOLDER = new ThreadLocal<GeneratorState>() {
		@Override
		protected GeneratorState initialValue() {
			return new GeneratorState();
		}
	};

	public static GeneratorState currentState() {
		return STATE_HOLDER.get();
	}

	/**
	 * Intializes the MathRandom instance.
	 */
	public long initMathRandom(final Long seed) {
		if (seed == null) {
			mathRandom = new MathRandom();
		} else {
			mathRandom = new MathRandom(seed);
		}
		return mathRandom.getSeed();
	}

	public MathRandom getMathRandom() {
		return mathRandom;
	}
}