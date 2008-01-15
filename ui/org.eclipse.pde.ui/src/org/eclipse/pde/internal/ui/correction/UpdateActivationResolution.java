/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.ui.correction;

import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.TargetPlatformHelper;
import org.eclipse.pde.internal.core.text.bundle.BundleModel;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.osgi.framework.Constants;

public class UpdateActivationResolution extends AbstractManifestMarkerResolution {

	String fHeader = null;

	public UpdateActivationResolution(int type, String currentHeader) {
		super(type);
		fHeader = currentHeader;
	}

	protected void createChange(BundleModel model) {
		if (TargetPlatformHelper.getTargetVersion() >= 3.4) {
			boolean lazy = false;
			String header = model.getBundle().getHeader(ICoreConstants.ECLIPSE_AUTOSTART);
			if (header != null) {
				lazy = "true".equals(header.trim()); //$NON-NLS-1$
				model.getBundle().setHeader(ICoreConstants.ECLIPSE_AUTOSTART, null);
			}
			header = model.getBundle().getHeader(ICoreConstants.ECLIPSE_LAZYSTART);
			if (header != null) {
				lazy = "true".equals(header.trim()); //$NON-NLS-1$
				model.getBundle().setHeader(ICoreConstants.ECLIPSE_LAZYSTART, null);
			}
			if (lazy)
				model.getBundle().setHeader(Constants.BUNDLE_ACTIVATIONPOLICY, Constants.ACTIVATION_LAZY);
		} else {
			// if we should not use the Bundle-ActivationPolicy header, then we know we are renaming the Eclipse-AutoStart header to Eclipse-LazyStart
			model.getBundle().renameHeader(ICoreConstants.ECLIPSE_AUTOSTART, ICoreConstants.ECLIPSE_LAZYSTART);
		}
	}

	public String getDescription() {
		if (TargetPlatformHelper.getTargetVersion() >= 3.4)
			return PDEUIMessages.UpdateActivationResolution_bundleActivationPolicy_label;
		return PDEUIMessages.UpdateActivationResolution_lazyStart_label;
	}

	public String getLabel() {
		if (TargetPlatformHelper.getTargetVersion() >= 3.4)
			return NLS.bind(PDEUIMessages.UpdateActivationResolution_bundleActivationPolicy_desc, fHeader);
		return PDEUIMessages.UpdateActivationResolution_lazyStart_desc;
	}
}
