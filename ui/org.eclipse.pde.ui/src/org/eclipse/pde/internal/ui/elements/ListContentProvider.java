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
package org.eclipse.pde.internal.ui.elements;

import org.eclipse.jface.viewers.*;

public class ListContentProvider extends DefaultContentProvider implements IStructuredContentProvider {

public ListContentProvider() {
	super();
}
public Object[] getElements(Object element) {
	if (element instanceof IPDEElement) {
		return ((IPDEElement)element).getChildren();
	}
	return null;
}
}
