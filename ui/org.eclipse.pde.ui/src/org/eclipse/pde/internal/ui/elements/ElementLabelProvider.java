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

import org.eclipse.swt.graphics.*;
import org.eclipse.jface.viewers.*;

public class ElementLabelProvider extends LabelProvider {
	public static final ElementLabelProvider INSTANCE = new ElementLabelProvider();

public ElementLabelProvider() {
	super();
}
public Image getImage(Object element) {
   if (element instanceof IPDEElement) {
	   return ((IPDEElement)element).getImage();
   }
   return super.getImage(element);
}
public String getText(Object element) {
   if (element instanceof IPDEElement) {
	   return ((IPDEElement)element).getLabel();
   }
   return super.getText(element);
}
}
