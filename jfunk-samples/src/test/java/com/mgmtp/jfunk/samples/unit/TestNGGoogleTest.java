package com.mgmtp.jfunk.samples.unit;

import javax.inject.Inject;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.mgmtp.jfunk.core.reporting.CsvReporter;
import com.mgmtp.jfunk.unit.JFunkRunner;
import com.mgmtp.jfunk.unit.JFunkTestNGSupport;

/**
 * @author rnaegele
 * @version $Id$
 */
@Listeners(JFunkTestNGSupport.class)
public class TestNGGoogleTest {

	@Inject
	JFunkRunner jFunkRunner;

	@Test
	public void testForminput() {
		jFunkRunner.registerReporter(CsvReporter.forDataSet("google").writtenTo("google.csv").create());
		jFunkRunner.generate("google");
		jFunkRunner.set("archive.dir", "testruns/testng");
		String searchTerm = jFunkRunner.get("${google searchTerm}");
		jFunkRunner.run(new UnitTestModule(searchTerm));
	}
}
