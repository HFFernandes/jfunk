/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.samples.forminput;

import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.BASE_URL;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.BOOLEAN_PROPERTY;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.INTEGER_IN_RANGE_PROPERTY;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.INTEGER_PROPERTY;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.NUMBER_RADIO_CHOICE;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.PHONE_NUMBER_US;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.SAVE_BUTTON;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.STRING_PROPERTY;
import static com.mgmtp.jfunk.samples.forminput.ForminputConstants.URL_PROPERTY;

import org.openqa.selenium.By;

import com.mgmtp.jfunk.common.util.RegExUtil;
import com.mgmtp.jfunk.core.module.TestModuleImpl;
import com.mgmtp.jfunk.core.step.base.StepMode;
import com.mgmtp.jfunk.data.DataSet;
import com.mgmtp.jfunk.data.generator.data.GeneratorDataSource;
import com.mgmtp.jfunk.web.step.CheckHtml4Pattern;
import com.mgmtp.jfunk.web.step.ClickElement;
import com.mgmtp.jfunk.web.step.LoadPage;

/**
 * Example jFunk module. The following steps are performed:
 * <ol>
 * <li>Load the Wicket forminput example page</li>
 * <li>Check input fields for their default values</li>
 * <li>Enter new values based using data generated by the {@link GeneratorDataSource}</li>
 * <li>Click the save button</li>
 * <li>Check the status message on the page</li>
 * <li>Check the input fields if they still contain the previously entered values</li>
 * </ol>
 * 
 * @version $Id$
 */
public class ForminputModule extends TestModuleImpl {

	public ForminputModule() {
		super("forminput");
	}

	@Override
	protected void executeSteps() {
		executeSteps(
				new LoadPage(BASE_URL),
				new ForminputPage(StepMode.CHECK_VALUE),
				new ForminputPage(StepMode.SET_VALUE),
				new ClickElement(By.name(SAVE_BUTTON)),
				new CheckHtml4Pattern(getResultString()),
				new ForminputPage(StepMode.CHECK_VALUE));
	}

	private String getResultString() {
		DataSet dataSet = getDataSet();

		StringBuilder sb = new StringBuilder();
		sb.append(".*Saved model \\[TestInputObject stringProperty = '");
		sb.append(dataSet.getValue(STRING_PROPERTY));
		sb.append("', integerProperty = ");
		sb.append(dataSet.getValue(INTEGER_PROPERTY));
		sb.append(", doubleProperty = .*");
		sb.append(", booleanProperty = ");
		sb.append(dataSet.getValue(BOOLEAN_PROPERTY));
		sb.append(", integerInRangeProperty = ");
		sb.append(dataSet.getValue(INTEGER_IN_RANGE_PROPERTY));
		sb.append(", urlProperty = ");
		sb.append(dataSet.getValue(URL_PROPERTY));
		sb.append(", phoneNumberUS = ");
		sb.append(RegExUtil.escape(dataSet.getValue(PHONE_NUMBER_US)));
		sb.append(", numberRadioChoice = ");
		sb.append(convertNumberRadioChoiceValue(dataSet.getValue(NUMBER_RADIO_CHOICE)));
		sb.append(".*numberRadioGroup= .*");
		return sb.toString();
	}

	private String convertNumberRadioChoiceValue(final String originalValue) {
		return String.valueOf(Integer.parseInt(originalValue) + 1);
	}
}