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
package org.eclipse.pde.internal.ui.editor.feature;

import org.eclipse.jface.wizard.*;
import org.eclipse.pde.internal.core.ifeature.IFeatureModel;
import org.eclipse.pde.internal.ui.*;

public class IncludeFeaturesWizard extends Wizard {
	private IFeatureModel model;
	private IncludeFeaturesWizardPage mainPage;

public IncludeFeaturesWizard(IFeatureModel model) {
	this.model = model;
	setDefaultPageImageDescriptor(PDEPluginImages.DESC_NEWPPRJ_WIZ);
	setDialogSettings(PDEPlugin.getDefault().getDialogSettings());
	setNeedsProgressMonitor(true);
}

public void addPages() {
	mainPage = new IncludeFeaturesWizardPage(model);
	addPage(mainPage);
}

public boolean performFinish() {
	return mainPage.finish();
}

}
