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

package org.eclipse.emf.ecoretools.properties.internal.sections;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.tabbedproperties.sections.AbstractBooleanPropertySection;

/**
 * A section for the serializable property of an EDataType Object.
 * 
 * @author Jacques LESCOT
 */
public class IsSerializablePropertySection extends AbstractBooleanPropertySection {

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractBooleanPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getEDataType_Serializable();
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "isSerializable";
	}

}