/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.ui.editor.site;

import org.eclipse.update.ui.forms.internal.*;
import org.eclipse.pde.internal.ui.editor.*;

public class ArchivePage extends PDEChildFormPage {

	public ArchivePage(PDEFormPage parent, String title) {
		super(parent, title);
	}
	protected AbstractSectionForm createForm() {
		return new ArchiveForm(this);
	}
}
