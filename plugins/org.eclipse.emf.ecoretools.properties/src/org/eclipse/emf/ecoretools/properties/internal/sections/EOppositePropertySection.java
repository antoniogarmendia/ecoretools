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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * A section for the EOpposite property of an EReference.
 * 
 * Creation 16 oct. 07
 * 
 * @author <a href="mailto:jacques.lescot@anyware-tech.com">Jacques LESCOT</a>
 */
public class EOppositePropertySection extends AbstractChooserPropertySection {

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getEReference_EOpposite();
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "EOpposite:";
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractChooserPropertySection#getComboFeatureValues()
	 */
	protected Object[] getComboFeatureValues() {
		return getPropertyDescriptor() != null ? getPropertyDescriptor().getChoiceOfValues(getEObject()).toArray() : new String[] { "" };
	}

	/**
	 * Use the setPropertyValue() method implemented by the generated EMF code.
	 * This automatically handle the refreshment of the two complementary
	 * EOpposite references.
	 * 
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractChooserPropertySection#handleComboModified()
	 */
	protected void handleComboModified() {
		if (!isRefreshing() && getFeatureValue() != getCSingleObjectChooser().getSelection() && getEObjectList().size() == 1) {
			if (getPropertyDescriptor() != null) {
				getPropertyDescriptor().setPropertyValue(getEObject(), getCSingleObjectChooser().getSelection());
			} else {
				super.handleComboModified();
			}
		}
	}

	private IItemPropertyDescriptor getPropertyDescriptor() {
		for (Adapter adapter : getEObject().eAdapters()) {
			if (adapter instanceof ItemProviderAdapter) {
				IItemPropertyDescriptor descriptor = ((ItemProviderAdapter) adapter).getPropertyDescriptor(getEObject(), "EOpposite");
				if (descriptor != null) {
					return descriptor;
				}
			}
		}
		return null;
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractChooserPropertySection#getLabelProvider()
	 */
	protected ILabelProvider getLabelProvider() {
		return new AdapterFactoryLabelProvider(new EcoreItemProviderAdapterFactory());
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 */
	protected Object getFeatureValue() {
		return ((EReference) getEObject()).getEOpposite();
	}

}