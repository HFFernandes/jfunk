/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.core.event;

/**
 * @author rnaegele
 * @version $Id$
 */
public class BeforeCommandEvent extends CommandEvent {

	public BeforeCommandEvent(final String command, final Object[] params) {
		super(command, params);
	}
}
