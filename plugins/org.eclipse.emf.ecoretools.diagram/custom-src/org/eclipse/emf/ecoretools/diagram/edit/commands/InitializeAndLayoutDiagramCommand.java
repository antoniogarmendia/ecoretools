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
package org.eclipse.emf.ecoretools.diagram.edit.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecoretools.diagram.part.EcoreDiagramContentInitializer;
import org.eclipse.emf.ecoretools.diagram.part.Messages;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * 
 * TODO Describe the class here <br>
 * creation : 3 janv. 2008
 * 
 * @author <a href="mailto:gilles.cannenterre@anyware-tech.com">Gilles Cannenterre</a>
 */
public class InitializeAndLayoutDiagramCommand extends AbstractTransactionalCommand {

	private Diagram diagram;

	public InitializeAndLayoutDiagramCommand(TransactionalEditingDomain domain, Diagram diagram) {
		super(domain, Messages.CommandName_InitializeAndLayoutDiagram, null);
		this.diagram = diagram;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (diagram == null)
		{
			return CommandResult.newWarningCommandResult("Unable to proceed with null diagam", null);
		}
		// Initialize diagram content
		EcoreDiagramContentInitializer intializer = new EcoreDiagramContentInitializer();
		intializer.initDiagramContent(diagram);
		
		// Layout diagram content if necessary
		if (false == diagram.getChildren().isEmpty())
		{
			List<Node> nodes = new ArrayList<Node>();
			for(Object view : diagram.getChildren())
			{
				if (view instanceof Node) {
					nodes.add((Node)view);
				}
			}
			LayoutService.getInstance().layoutNodes(nodes, true, LayoutType.DEFAULT);
		}
		
		return CommandResult.newOKCommandResult();
	}

}