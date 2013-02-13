/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.core.scripting;

import static com.google.common.base.Preconditions.checkState;
import groovy.lang.MetaProperty;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.mgmtp.jfunk.common.JFunkConstants;
import com.mgmtp.jfunk.common.config.ThreadScope;
import com.mgmtp.jfunk.core.event.AfterScriptEvent;
import com.mgmtp.jfunk.core.event.BeforeScriptEvent;
import com.mgmtp.jfunk.core.reporting.SimpleReporter;
import com.mgmtp.jfunk.core.scripting.groovy.Commands;

/**
 * Executes Groovy scripts.
 * 
 * @version $Id$
 */
@Singleton
public class ScriptExecutor {
	private final Logger log = Logger.getLogger(ScriptContext.class);

	private final Provider<ScriptContext> scriptContextProvider;
	private final EventBus eventBus;
	private final ThreadScope scriptScope;
	private final Charset charset;

	/**
	 * Creates a new instance.
	 * 
	 * @param scriptContextProvider
	 *            provides the {@link ScriptContext}
	 * @param eventBus
	 *            the event bus
	 * @param scriptScope
	 *            the script scope instance
	 */
	@Inject
	public ScriptExecutor(final Provider<ScriptContext> scriptContextProvider, final EventBus eventBus, final ThreadScope scriptScope,
			final Charset charset) {
		this.scriptContextProvider = scriptContextProvider;
		this.eventBus = eventBus;
		this.scriptScope = scriptScope;
		this.charset = charset;
	}

	/**
	 * Executes the specified Groovy script.
	 * 
	 * @param script
	 *            the script file
	 * @param scriptProperties
	 *            properties that are set to the script engine's binding and thus will be available
	 *            as variables in the Groovy script
	 * @return the execution result, {@code true} if successful, {@code false} code
	 */
	public boolean executeScript(final File script, final Properties scriptProperties) {
		checkState(script.exists(), "Script file does not exist: %s", script);
		checkState(script.canRead(), "Script file is not readable: %s", script);

		Reader reader = null;
		boolean success = false;

		try {
			scriptScope.enterScope();

			reader = Files.newReader(script, charset);

			ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByExtension("groovy");
			ScriptContext ctx = scriptContextProvider.get();
			ctx.setScript(script);
			ctx.load(JFunkConstants.SCRIPT_PROPERTIES, false);
			ctx.registerReporter(new SimpleReporter());
			initGroovyCommands(scriptEngine, ctx);
			initScriptProperties(scriptEngine, scriptProperties);
			eventBus.post(scriptEngine);
			eventBus.post(new BeforeScriptEvent(script));
			scriptEngine.eval(reader);
			success = true;
		} catch (IOException ex) {
			log.error("Error loading script: " + script, ex);
		} catch (ScriptException ex) {
			// Look up the cause hierarchy if we find a ModuleExecutionException.
			// We only need to log exceptions other than ModuleExecutionException because they
			// have already been logged and we don't want to pollute the log file any further.
			// In fact, other exception cannot normally happen.
			Throwable th = ex.getCause();
			while (!(th instanceof ModuleExecutionException)) {
				if (th == null) {
					// log original exception
					log.error("Error executing script: " + ex.getMessage(), ex);
					success = false;
					break;
				}
				th = th.getCause();
			}
		} finally {
			try {
				eventBus.post(new AfterScriptEvent(script, success));
			} finally {
				scriptScope.exitScope();
				Closeables.closeQuietly(reader);
			}
		}

		return success;
	}

	private void initGroovyCommands(final ScriptEngine scriptEngine, final ScriptContext scriptContext) {
		Commands commands = new Commands(scriptContext);

		for (MetaProperty mp : commands.getMetaClass().getProperties()) {
			String propertyType = mp.getType().getCanonicalName();
			String propertyName = mp.getName();

			if (propertyType.equals(groovy.lang.Closure.class.getCanonicalName())) {
				scriptEngine.put(propertyName, commands.getProperty(propertyName));
			}
		}
	}

	private void initScriptProperties(final ScriptEngine se, final Properties properties) {
		for (String name : properties.stringPropertyNames()) {
			se.put(name, properties.getProperty(name));
		}
	}
}