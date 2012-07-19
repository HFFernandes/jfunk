package com.mgmtp.jfunk.web;

import java.util.Set;

import javax.inject.Inject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.mgmtp.jfunk.common.util.Configuration;

/**
 * @author rnaegele
 * @version $Id$
 */
public class FirefoxDriverProvider extends BaseWebDriverProvider {

	@Inject
	protected FirefoxDriverProvider(final Configuration config, final Set<WebDriverEventListener> eventListeners) {
		super(config, eventListeners);
	}

	@Override
	protected WebDriver createWebDriver() {
		return new FirefoxDriver();
	}
}