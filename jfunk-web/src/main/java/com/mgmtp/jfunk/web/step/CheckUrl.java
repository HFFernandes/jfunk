package com.mgmtp.jfunk.web.step;

import com.mgmtp.jfunk.core.exception.PatternException;
import com.mgmtp.jfunk.core.exception.StepException;
import com.mgmtp.jfunk.core.module.TestModule;

/**
 * Checks if the URL of the current HTML page matches a given regular expression.
 * 
 * @version $Id$
 */
public class CheckUrl extends WebDriverStep {

	private final String regEx;

	/**
	 * Creates a new instance of CheckUrl.
	 * 
	 * @param regEx
	 *            a regular expression which will be checked against the URL of the current HTML
	 *            page
	 * @param test
	 *            param no longer used
	 */
	@Deprecated
	public CheckUrl(final String regEx, final TestModule test) {
		this(regEx);
	}

	/**
	 * Creates a new instance of CheckUrl.
	 * 
	 * @param regEx
	 *            a regular expression which will be checked against the URL of the current HTML
	 *            page
	 */
	public CheckUrl(final String regEx) {
		this.regEx = regEx;
	}

	@Override
	public void execute() throws StepException {
		String url = getWebDriver().getCurrentUrl();
		if (!url.matches(regEx)) {
			throw new PatternException("URL '" + url + "'", regEx, true);
		}
	}
}