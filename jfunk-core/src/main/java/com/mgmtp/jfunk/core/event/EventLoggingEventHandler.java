package com.mgmtp.jfunk.core.event;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * Event handler for logging all {@code before} and {@code after} events at debug level.
 * 
 * @author rnaegele
 * @version $Id$
 */
@Singleton
public class EventLoggingEventHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Subscribe
	@AllowConcurrentEvents
	public void handleEvent(final Object event) {
		String name = event.getClass().getSimpleName();
		if (name.startsWith("Before")) {
			log.debug(">> {}", event);
		} else if (name.startsWith("After")) {
			log.debug("<< {}", event);
		} else {
			// do nothing
		}
	}
}