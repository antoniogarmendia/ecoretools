/***********************************************************************
 * Copyright (c) 2008 Anyware Technologies
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

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;

/**
 * This sections can be used to handle the genmodel documentation EAnnotation
 * associated with an EObject
 * 
 * Creation 30 jan. 2008
 * 
 * @author <a href="mailto:jacques.lescot@anyware-tech.com">Jacques LESCOT</a>
 */
public class GenModelDocumentationPropertySection extends AbstractTabbedPropertySection {

	private static final String GENMODEL_SOURCE = "http://www.eclipse.org/emf/2002/GenModel";

	private static final String GENMODEL_DOC_KEY = "documentation";

	private Text commentsText;

	private FocusAdapter focusListener;

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "GenModel Documentation:";
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getEAnnotation_Source();
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#createWidgets(org.eclipse.swt.widgets.Composite)
	 */
	protected void createWidgets(Composite composite) {
		commentsText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);

		focusListener = new FocusAdapter() {

			private String previousComment;

			public void focusGained(FocusEvent e) {
				previousComment = commentsText.getText();
			}

			public void focusLost(FocusEvent e) {
				if (!commentsText.getText().equals(previousComment)) {
					handleDocChanged();
				}
			}
		};

		commentsText.addFocusListener(focusListener);
	}

	/**
	 * @see org.eclipse.emf.tabbedproperties.sections.AbstractTabbedPropertySection#setSectionData(org.eclipse.swt.widgets.Composite)
	 */
	protected void setSectionData(Composite composite) {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		data.bottom = new FormAttachment(100, 0);
		commentsText.setLayoutData(data);
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
	 */
	public boolean shouldUseExtraSpace() {
		return true;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	public void refresh() {
		commentsText.setText("");
		commentsText.setEnabled(getEObject() != null);
		if (getEObject() != null) {
			EAnnotation annotation = ((EModelElement) getEObject()).getEAnnotation(GENMODEL_SOURCE);
			if (annotation != null) {
				String doc = annotation.getDetails().get(GENMODEL_DOC_KEY);
				if (doc != null) {
					commentsText.setText(doc);
				}
			}
		}
	}

	private void handleDocChanged() {
		String newDoc = commentsText.getText();
		if (getEObject() != null) {
			Command cmd = new ChangeComments((EModelElement) getEObject(), newDoc);
			getEditingDomain().getCommandStack().execute(cmd);
		}
	}

	private class ChangeComments extends AbstractCommand {

		private EModelElement element;

		private String newComments;

		private String oldComments;

		public ChangeComments(EModelElement element, String comments) {
			super("Change comments of EModelElement");

			this.element = element;
			this.newComments = comments;
		}

		/**
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 */
		public boolean canExecute() {
			return true;
		}
		
		/**
		 * @see org.eclipse.emf.common.command.AbstractCommand#canUndo()
		 */
		public boolean canUndo() {
			return true;
		}
		
		/**
		 * @see org.eclipse.emf.common.command.Command#execute()
		 */
		public void execute() {
			// stores the previous doc
			oldComments = null;
			EAnnotation annotation = element.getEAnnotation(GENMODEL_SOURCE);
			if (annotation != null) {
				oldComments = annotation.getDetails().get(GENMODEL_DOC_KEY);
			}

			redo();
		}

		/**
		 * Set the documentation for the given Model Element
		 * 
		 * @param elt
		 *            the element to document
		 * @param newDoc
		 *            the documentation text
		 */
		protected void changeDocumentation(EModelElement elt, String newDoc) {
			EAnnotation annotation = elt.getEAnnotation(GENMODEL_SOURCE);
			if (newDoc != null && !"".equals(newDoc)) {
				// creates EAnnotation if needed
				if (annotation == null) {
					annotation = EcoreFactory.eINSTANCE.createEAnnotation();
					annotation.setSource(GENMODEL_SOURCE);
					elt.getEAnnotations().add(annotation);
				}

				annotation.getDetails().put(GENMODEL_DOC_KEY, newDoc);
			} else {
				// remove the documentation
				if (annotation != null) {
					annotation.getDetails().remove(GENMODEL_DOC_KEY);

					// remove the EAnnotation if empty
					if (annotation.getDetails().size() == 0) {
						elt.getEAnnotations().remove(annotation);
					}
				}
			}
		}

		/**
		 * @see org.eclipse.emf.common.command.Command#redo()
		 */
		public void redo() {
			changeDocumentation(element, newComments);
		}

		/**
		 * @see org.eclipse.emf.common.command.AbstractCommand#undo()
		 */
		public void undo() {
			changeDocumentation(element, oldComments);
		}
	}

}