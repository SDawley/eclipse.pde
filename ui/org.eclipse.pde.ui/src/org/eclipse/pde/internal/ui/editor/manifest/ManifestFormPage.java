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
package org.eclipse.pde.internal.ui.editor.manifest;

import org.eclipse.pde.internal.ui.editor.*;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.update.ui.forms.internal.*;

public class ManifestFormPage extends PDEFormPage {


public ManifestFormPage(ManifestEditor editor, String title) {
	super(editor, title);
}

public IContentOutlinePage createContentOutlinePage() {
	return new ManifestFormOutlinePage(this);
}
protected AbstractSectionForm createForm() {
	return new ManifestForm(this);
}
}
