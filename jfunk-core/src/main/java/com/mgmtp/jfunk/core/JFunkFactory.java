/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.core;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @author rnaegele
 * @version $Id$
 */
public interface JFunkFactory {

	JFunkBase create(int threadCount, boolean parallel, List<File> scripts, Properties scriptProperties);
}
