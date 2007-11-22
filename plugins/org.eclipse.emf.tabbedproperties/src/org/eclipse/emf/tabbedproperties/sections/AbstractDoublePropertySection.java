/***********************************************************************
 * Copyright (c) 2007 Anyware Technologies
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Anyware Technologies - initial API and implementation
 **********************************************************************/

package org.eclipse.emf.tabbedproperties.sections;

import java.util.regex.Pattern;

import org.eclipse.emf.tabbedproperties.internal.utils.ColorRegistry;
import org.eclipse.swt.widgets.Event;

/**
 * An abstract implementation of a section for a field with a String property
 * value.
 * 
 * Creation 5 apr. 2006 Updated 7 aug. 2006
 * 
 * @author Jacques Lescot
 * @author Alfredo Serrano
 */
public abstract class AbstractDoublePropertySection extends AbstractTextPropertySection {

	/**
	 * Predefined string pattern value for decimal, absloute with '-' and exp
	 * notation : -25.36e-6
	 */
	public static final String EXP_NUMERIC_PATTERN = "^[-\\d][\\d]*\\.?[\\d]*((e|E)?-?[\\d]*)"; //$NON-NLS-1$

	/** The Pattern used to check a Double value */
	public static final Pattern DOUBLE_PATTERN = Pattern.compile(EXP_NUMERIC_PATTERN);

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTextPropertySection#verifyField(Event)
	 */
	protected void verifyField(Event e) {
		String value = getText().getText();
		if (value == null || value.equals("") || isTextValid()) {
			setErrorMessage(null);
			getText().setBackground(null);
			e.doit = true;
		} else {
			setErrorMessage("The character is not valid!!!");
			getText().setBackground(ColorRegistry.COLOR_ERROR);
			e.doit = false;
		}
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTextPropertySection#getFeatureAsString()
	 */
	protected String getFeatureAsString() {
		return getFeatureDouble().toString();
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTextPropertySection#getOldFeatureValue()
	 */
	protected Object getOldFeatureValue() {
		return getFeatureDouble();
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTextPropertySection#getNewFeatureValue(java.lang.String)
	 */
	protected Object getNewFeatureValue(String newText) {
		if (newText == null || newText.equals("")) {
			return null;
		}
		return new Double(Double.parseDouble(newText));
	}

	/**
	 * Get the text value of the feature for the text field for the section.
	 * 
	 * @return the text value of the feature.
	 */
	protected abstract Double getFeatureDouble();

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTextPropertySection#isTextValid()
	 */
	protected boolean isTextValid() {
		return DOUBLE_PATTERN.matcher(getText().getText()).matches();
	}
}