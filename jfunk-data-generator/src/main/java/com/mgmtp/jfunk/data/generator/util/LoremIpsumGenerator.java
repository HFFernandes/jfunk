/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.data.generator.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.text.StrBuilder;

import com.mgmtp.jfunk.data.generator.config.LoremIpsum;

/**
 * @author rnaegele
 */
@Singleton
public class LoremIpsumGenerator {

	private final String loremIpsum;
	private final int loremLength;

	@Inject
	public LoremIpsumGenerator(@LoremIpsum final String loremIpsum) {
		this.loremIpsum = loremIpsum;
		loremLength = loremIpsum.length();
	}

	public String generateLoremIpsum(final int length) {
		if (loremLength < length) {
			int multiplier = length / loremLength;
			int remainder = length % loremLength;

			StrBuilder sb = new StrBuilder(multiplier * length + remainder);
			for (int i = 0; i < multiplier; ++i) {
				sb.append(loremIpsum);
			}
			sb.append(loremIpsum.substring(0, remainder));
			return sb.toString();
		}

		return loremIpsum.substring(0, length);
	}
}
