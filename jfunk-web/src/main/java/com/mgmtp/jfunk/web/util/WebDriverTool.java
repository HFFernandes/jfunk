/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.web.util;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.difference;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets.SetView;
import com.mgmtp.jfunk.common.config.ModuleScoped;
import com.mgmtp.jfunk.data.DataSet;

/**
 * <p>
 * Utility class for enhancing {@link WebDriver} functionality. Uses {@link WebElementFinder} and
 * {@link FormInputHandler} internally and thus can handle timeouts implicitly.
 * </p>
 * <p>
 * An script-scoped instance of this class can be retrieve via dependency injection.
 * </p>
 * 
 * @author rnaegele
 */
@ModuleScoped
public class WebDriverTool {

	private static final String APPEND_OPEN_WINDOW_LINK_SCRIPT_TEMPLATE = "(function() { "
			+ "var jFunkAnchorTag = document.createElement('a');"
			+ "jFunkAnchorTag.appendChild(document.createTextNode('jfunk-new-window-link'));"
			+ "jFunkAnchorTag.setAttribute('id', '%s');"
			+ "jFunkAnchorTag.setAttribute('href', '%s');"
			+ "jFunkAnchorTag.setAttribute('target', '_blank');"
			+ "jFunkAnchorTag.setAttribute('style', 'display:block; z-index: 100000; position: relative;');"
			+ "document.getElementsByTagName('body')[0].appendChild(jFunkAnchorTag);"
			+ "}());";

	private static final String REMOVE_OPEN_WINDOW_LINK_SCRIPT_TEMPLATE = "(function() { "
			+ "var jFunkAnchorTag = document.getElementById('%s');"
			+ "jFunkAnchorTag.parentNode.removeChild(jFunkAnchorTag);"
			+ "}());";

	private final WebDriver webDriver;
	private final WebElementFinder wef;
	private final FormInputHandler fih;
	private final Map<String, DataSet> dataSets;

	@Inject
	WebDriverTool(final WebDriver webDriver, final WebElementFinder wef, final FormInputHandler fih,
			final Map<String, DataSet> dataSets) {
		this.webDriver = webDriver;
		this.wef = wef;
		this.fih = fih;
		this.dataSets = dataSets;
	}

	/**
	 * Finds the first element. Uses the internal {@link WebElementFinder}.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @return the element
	 */
	public WebElement find(final By by) {
		return wef.by(by).find();
	}

	/**
	 * Finds the first element. Uses the internal {@link WebElementFinder}, which tries to apply the
	 * specified {@code condition} until it times out.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param condition
	 *            a condition the found element must meet
	 * @return the element
	 */
	public WebElement find(final By by, final Predicate<WebElement> condition) {
		return wef.by(by).condition(condition).find();
	}

	/**
	 * Finds all elements. Uses the internal {@link WebElementFinder}.
	 * 
	 * @param by
	 *            the {@link By} used to locate the elements
	 * @return the list of elements
	 */
	public List<WebElement> findAll(final By by) {
		return wef.by(by).findAll();
	}

	/**
	 * Finds all elements. Uses the internal {@link WebElementFinder}, which tries to apply the
	 * specified {@code condition} until it times out.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param condition
	 *            a condition the found elements must meet
	 * @return the list of elements
	 */
	public List<WebElement> findAll(final By by, final Predicate<WebElement> condition) {
		return wef.by(by).condition(condition).findAll();
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specifed function until one
	 * of the following occurs:
	 * 
	 * <ol>
	 * <li>the function returns neither null nor false,</li>
	 * <li>the function throws an unignored exception,</li>
	 * <li>the timeout expires,
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 * 
	 * @param function
	 *            the function
	 * @param <V>
	 *            the function's expected return type
	 * @return the function's return value if the function returned something different from null or
	 *         false before the timeout expired
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public <V> V waitFor(final Function<? super WebDriver, V> function) {
		return newWebDriverWait().until(function);
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specifed function until one
	 * of the following occurs:
	 * 
	 * <ol>
	 * <li>the function returns neither null nor false,</li>
	 * <li>the function throws an unignored exception,</li>
	 * <li>the timeout expires,
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 * 
	 * @param function
	 *            the function
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * @param <V>
	 *            the function's expected return type
	 * @return the function's return value if the function returned something different from null or
	 *         false before the timeout expired
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public <V> V waitFor(final Function<? super WebDriver, V> function, final long timeoutSeconds) {
		return newWebDriverWait(timeoutSeconds).until(function);
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specifed function until one
	 * of the following occurs:
	 * 
	 * <ol>
	 * <li>the function returns neither null nor false,</li>
	 * <li>the function throws an unignored exception,</li>
	 * <li>the timeout expires,
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 * 
	 * The method uses the same timeout and milliseconds to sleep between polls as the internally
	 * used default {@link WebElementFinder} instance
	 * 
	 * @param function
	 *            the function
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * @param sleepMillis
	 *            the time in milliseconds to sleep between polls
	 * @param <V>
	 *            the function's expected return type
	 * @return the function's return value if the function returned something different from null or
	 *         false before the timeout expired
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public <V> V waitFor(final Function<? super WebDriver, V> function, final long timeoutSeconds, final long sleepMillis) {
		return newWebDriverWait(timeoutSeconds, sleepMillis).until(function);
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specified predicate until
	 * the timeout expires or the predicate evaluates to true.
	 * 
	 * @param predicate
	 *            the predicate to wait on
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public void waitFor(final Predicate<WebDriver> predicate) {
		newWebDriverWait().until(predicate);
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specified predicate until
	 * the timeout expires or the predicate evaluates to true.
	 * 
	 * @param predicate
	 *            the predicate to wait on
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public void waitFor(final Predicate<WebDriver> predicate, final long timeoutSeconds) {
		newWebDriverWait(timeoutSeconds).until(predicate);
	}

	/**
	 * Repeatedly applies the current {@link WebDriver} instance to the specified predicate until
	 * the timeout expires or the predicate evaluates to true.
	 * 
	 * @param predicate
	 *            the predicate to wait on
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * @param sleepMillis
	 *            the time in milliseconds to sleep between polls
	 * @throws TimeoutException
	 *             if the timeout expires.
	 */
	public void waitFor(final Predicate<WebDriver> predicate, final long timeoutSeconds, final long sleepMillis) {
		newWebDriverWait(timeoutSeconds, sleepMillis).until(predicate);
	}

	/**
	 * Creates a new {@link WebDriverWait} with the same timeout and milliseconds to sleep between
	 * polls as the internally used default {@link WebElementFinder} instance.
	 * 
	 * @return the newly created {@link WebDriverWait} instance
	 */
	public WebDriverWait newWebDriverWait() {
		return wef.getSleepMillis() > 0L
				? newWebDriverWait(wef.getTimeoutSeconds(), wef.getSleepMillis())
				: newWebDriverWait(wef.getTimeoutSeconds());
	}

	/**
	 * Creates a new {@link WebDriverWait} with the specified timeout.
	 * 
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * 
	 * @return the newly created {@link WebDriverWait} instance
	 */
	public WebDriverWait newWebDriverWait(final long timeoutSeconds) {
		return new LoggingWebDriverWait(webDriver, timeoutSeconds);
	}

	/**
	 * Creates a new {@link WebDriverWait} with the specified timeout and milliseconds to sleep
	 * between polls.
	 * 
	 * @param timeoutSeconds
	 *            the timeout in seconds
	 * @param sleepMillis
	 *            the time in milliseconds to sleep between polls
	 * 
	 * @return the newly created {@link WebDriverWait} instance
	 */
	public WebDriverWait newWebDriverWait(final long timeoutSeconds, final long sleepMillis) {
		return new LoggingWebDriverWait(webDriver, timeoutSeconds, sleepMillis);
	}

	/**
	 * Tries to find and element and clicks on it if found. Uses a timeout of two seconds.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @return {@code true} if the element was found and clicked, {@code false} otherwise
	 */
	public boolean tryClick(final By by) {
		List<WebElement> elements = wef.timeout(2L).by(by).findAll();
		if (elements.size() > 0) {
			elements.get(0).click();
			return true;
		}
		return false;
	}

	/**
	 * Delegates to {@link #find(By)} and calls {@link WebElement#sendKeys(CharSequence...)
	 * sendKeys(CharSequence...)} on the returned element.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param keysToSend
	 *            the keys to send
	 */
	public void sendKeys(final By by, final CharSequence... keysToSend) {
		find(by).sendKeys(keysToSend);
	}

	/**
	 * Delegates to {@link #find(By)} and calls {@link WebElement#clear() clear()} on the returned
	 * element.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 */
	public void clear(final By by) {
		find(by).clear();
	}

	/**
	 * Delegates to {@link #find(By)} and calls {@link WebElement#click() click()} on the returned
	 * element.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 */
	public void click(final By by) {
		find(by).click();
	}

	/**
	 * Delegates to {@link #find(By)} and then performs a context-click using the {@link Actions}
	 * class.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 */
	public void contextClick(final By by) {
		WebElement element = find(by);
		new Actions(webDriver).contextClick(element).perform();
	}

	/**
	 * Delegates to {@link #find(By)} and then performs a double-click using the {@link Actions}
	 * class.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 */
	public void doubleClick(final By by) {
		WebElement element = find(by);
		new Actions(webDriver).doubleClick(element).perform();
	}

	/**
	 * Delegates to {@link #find(By)} and then moves the mouse to the returned element using the
	 * {@link Actions} class.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 */
	public void hover(final By by) {
		WebElement element = find(by);
		new Actions(webDriver).moveToElement(element).perform();
	}

	/**
	 * Delegates to {@link #find(By)}, moves the mouse to the returned element using the
	 * {@link Actions} class and then tries to find and element using {@code byToAppear} with a
	 * timeout of 1 seconds, retrying up to ten times because hovers sometimes do not work very
	 * reliably.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param byToAppear
	 *            the {@link By} used to locate the element that is supposed to appear after
	 *            hovering
	 */
	public WebElement hover(final By by, final By byToAppear) {
		WebElementFinder finder = wef.timeout(1L, 200L).by(byToAppear);

		RuntimeException exception = null;
		for (int i = 0; i < 10; ++i) {
			try {
				WebElement element = find(by);
				new Actions(webDriver).moveToElement(element).perform();
				return finder.find();
			} catch (NoSuchElementException ex) {
				exception = ex;
			} catch (TimeoutException ex) {
				exception = ex;
			} catch (StaleElementReferenceException ex) {
				exception = ex;
			}
		}
		throw exception;
	}

	/**
	 * Delegates to {@link #find(By)} and then calls {@link WebElement#getAttribute(String)
	 * getAttribute(String)} on the returned element.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param attributeName
	 *            the attribute name
	 * @return the attribute value
	 */
	public String getAttributeValue(final By by, final String attributeName) {
		WebElement element = find(by);
		return element.getAttribute(attributeName);
	}

	/**
	 * Delegates to {@link #find(By)} and then calls {@link WebElement#getText() getText()} on the
	 * returned element. The element's text is passed to {@link StringUtils#normalizeSpace(String)}.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @return the text
	 */
	public String getElementText(final By by) {
		return getElementText(by, true);
	}

	/**
	 * Delegates to {@link #find(By)} and then calls {@link WebElement#getText() getText()} on the
	 * returned element. If {@code normalizeSpace} is {@code true}, the element's text is passed to
	 * {@link StringUtils#normalizeSpace(String)}.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element
	 * @param normalizeSpace
	 *            specifies whether whitespace in the element text are to be normalized
	 * @return the text
	 */
	public String getElementText(final By by, final boolean normalizeSpace) {
		WebElement element = find(by);
		String text = element.getText();
		return normalizeSpace ? StringUtils.normalizeSpace(text) : text;
	}

	/**
	 * Uses the internal {@link FormInputHandler} to set a form field.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element representing an HTML input or textarea
	 * @param dataSetKey
	 *            the data set key
	 * @param dataKey
	 *            the key used to retrieve the value for the field from the data set with the
	 *            specifies data set key
	 */
	public void processField(final By by, final String dataSetKey, final String dataKey) {
		fih.by(by).dataSet(dataSets.get(dataSetKey)).dataKey(dataKey).perform();
	}

	/**
	 * Uses the internal {@link FormInputHandler} to set an indexed form field.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element representing an HTML input or textarea
	 * @param dataSetKey
	 *            the data set key
	 * @param dataKey
	 *            the key used to retrieve the value for the field from the data set with the
	 *            specifies data set key
	 * @param dataIndex
	 *            the index for looking up dat value in the data set
	 */
	public void processField(final By by, final String dataSetKey, final String dataKey, final Integer dataIndex) {
		fih.by(by).dataSet(dataSets.get(dataSetKey)).dataKeyWithIndex(dataKey, dataIndex).perform();
	}

	/**
	 * Uses the internal {@link FormInputHandler} to set form field. This method does not use a data
	 * set to retrieve the value.
	 * 
	 * @param by
	 *            the {@link By} used to locate the element representing an HTML input or textarea
	 * @param value
	 *            the value to set the field to
	 */
	public void processField(final By by, final String value) {
		fih.by(by).value(value).perform();
	}

	/**
	 * Opens a new window and switches to it. The window to switch to is determined by diffing the
	 * given {@code existingWindowHandles} with the current ones. The difference must be exactly one
	 * window handle which is then used to switch to.
	 * 
	 * @param openClickBy
	 *            identifies the element to click on in order to open the new window
	 * @param timeoutSeconds
	 *            the timeout in seconds to wait for the new window to open
	 * @return the handle of window that opened the new window
	 */
	public String openNewWindow(final By openClickBy, final long timeoutSeconds) {
		return openNewWindow(new Runnable() {
			@Override
			public void run() {
				click(openClickBy);
			}
		}, timeoutSeconds);
	}

	/**
	 * Opens a new window blank window ({@code about:blank}) and switches to it. The new window is
	 * opened by inserting a new link with {@code target='_blank'} and {@code href='about:blank'} at
	 * the end of the page, which is then clicked and removed again afterwards.
	 * 
	 * @return the handle of window that opened the new window
	 */
	public String openNewWindow() {
		return openNewWindow("about:blank");
	}

	/**
	 * Opens a new window, switches to it, and loads the given URL in the new window. The new window
	 * is opened by inserting a new link with {@code target='_blank'} and {@code href='about:blank'}
	 * at the end of the page, which is then clicked and removed again afterwards.
	 * 
	 * @param url
	 *            the url to open
	 * @return the handle of window that opened the new window
	 */
	public String openNewWindow(final String url) {
		String id = UUID.randomUUID().toString();
		// add link
		((JavascriptExecutor) webDriver).executeScript(String.format(APPEND_OPEN_WINDOW_LINK_SCRIPT_TEMPLATE, id, url));
		String oldHandle = openNewWindow(By.id(id), 2L);
		String newHandle = webDriver.getWindowHandle();

		// remove link again
		webDriver.switchTo().window(oldHandle);
		((JavascriptExecutor) webDriver).executeScript(String.format(REMOVE_OPEN_WINDOW_LINK_SCRIPT_TEMPLATE, id));

		webDriver.switchTo().window(newHandle);
		return oldHandle;
	}

	/**
	 * Opens a new window and switches to it. The window to switch to is determined by diffing the
	 * given {@code existingWindowHandles} with the current ones. The difference must be exactly one
	 * window handle which is then used to switch to.
	 * 
	 * @param openCommand
	 *            logic for opening the new window
	 * @param timeoutSeconds
	 *            the timeout in seconds to wait for the new window to open
	 * @return the handle of window that opened the new window
	 */
	public String openNewWindow(final Runnable openCommand, final long timeoutSeconds) {
		String oldHandle = webDriver.getWindowHandle();
		final Set<String> existingWindowHandles = webDriver.getWindowHandles();

		openCommand.run();

		Function<WebDriver, String> function = new Function<WebDriver, String>() {
			@Override
			public String apply(final WebDriver input) {
				Set<String> newWindowHandles = webDriver.getWindowHandles();
				SetView<String> newWindows = difference(newWindowHandles, existingWindowHandles);
				if (newWindows.isEmpty()) {
					throw new NotFoundException("No new window found.");
				}
				return getOnlyElement(newWindows);
			}

			@Override
			public String toString() {
				return "to open new window";
			}
		};

		String newHandle = waitFor(function);
		webDriver.switchTo().window(newHandle);
		return oldHandle;
	}
}
