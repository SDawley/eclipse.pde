/*******************************************************************************
 * Copyright (c) 2006, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.ua.core.cheatsheet.comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.pde.internal.core.XMLPrintHandler;
import org.eclipse.pde.internal.core.util.PDETextHelper;
import org.eclipse.pde.internal.ua.core.icheatsheet.comp.ICompCSDependency;
import org.eclipse.pde.internal.ua.core.icheatsheet.comp.ICompCSModel;
import org.eclipse.pde.internal.ua.core.icheatsheet.comp.ICompCSObject;
import org.eclipse.pde.internal.ua.core.icheatsheet.comp.ICompCSTaskObject;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CompCSDependency extends CompCSObject implements ICompCSDependency {

	private String fFieldTask;

	private static final long serialVersionUID = 1L;

	/**
	 * @param model
	 * @param parent
	 */
	public CompCSDependency(ICompCSModel model, ICompCSObject parent) {
		super(model, parent);
		reset();
	}

	@Override
	public List<ICompCSTaskObject> getChildren() {
		return new ArrayList<>();
	}

	public String getElement() {
		return ELEMENT_DEPENDENCY;
	}

	public String getName() {
		return fFieldTask;
	}

	public int getType() {
		return TYPE_DEPENDENCY;
	}

	protected void parseAttributes(Element element) {
		// Process task attribute
		// Trim leading and trailing whitespace
		fFieldTask = element.getAttribute(ATTRIBUTE_TASK).trim();
	}

	protected void parseElement(Element element) {
		// NO-OP
	}

	public void reset() {
		fFieldTask = null;
	}

	protected void writeAttributes(StringBuilder buffer) {
		// Print task attribute
		if ((fFieldTask != null) && (fFieldTask.length() > 0)) {
			// Trim leading and trailing whitespace
			// Encode characters
			buffer.append(XMLPrintHandler.wrapAttribute(ATTRIBUTE_TASK,
					PDETextHelper.translateWriteText(fFieldTask.trim(),
							DEFAULT_SUBSTITUTE_CHARS)));
		}
	}

	protected void writeElements(String indent, PrintWriter writer) {
		// NO-OP
	}

	public String getFieldTask() {
		return fFieldTask;
	}

	public void setFieldTask(String task) {
		String old = fFieldTask;
		fFieldTask = task;
		if (isEditable()) {
			firePropertyChanged(ATTRIBUTE_TASK, old, fFieldTask);
		}
	}

	protected void parseText(Text text) {
		// NO-OP
	}

}
