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
package org.eclipse.pde.internal.ui.editor.build;

import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.editor.PDEMultiPageEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.update.ui.forms.internal.AbstractSectionForm;

public class BuildPage extends PDEFormPage {

	public BuildPage(PDEMultiPageEditor editor, String title) {
		super(editor, title);
	}
	public IContentOutlinePage createContentOutlinePage() {
		return null;
	}
	protected AbstractSectionForm createForm() {
		return new BuildForm(this);
	}

}
