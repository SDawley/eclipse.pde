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

import org.eclipse.jface.action.*;

public class NullToolBarManager implements IToolBarManager {

	public void add(org.eclipse.jface.action.IAction action) {
	}
	public void add(org.eclipse.jface.action.IContributionItem item) {
	}
	public void appendToGroup(
		String groupName,
		org.eclipse.jface.action.IAction action) {
	}
	public void appendToGroup(
		String groupName,
		org.eclipse.jface.action.IContributionItem item) {
	}
	public org.eclipse.jface.action.IContributionItem find(String id) {
		return null;
	}
	public org.eclipse.jface.action.IContributionItem[] getItems() {
		return null;
	}
	public void insertAfter(
		String id,
		org.eclipse.jface.action.IAction action) {
	}
	public void insertAfter(
		String iD,
		org.eclipse.jface.action.IContributionItem item) {
	}
	public void insertBefore(
		String id,
		org.eclipse.jface.action.IAction action) {
	}
	public void insertBefore(
		String iD,
		org.eclipse.jface.action.IContributionItem item) {
	}
	public boolean isDirty() {
		return false;
	}
	public boolean isEmpty() {
		return false;
	}
	public IContributionManagerOverrides getOverrides() {
		return new IContributionManagerOverrides() {
			public Boolean getEnabled(IContributionItem item) {
				return null;
			}
			public Integer getAccelerator(IContributionItem item) {
				return null;
			}
			public String getAcceleratorText(IContributionItem item) {
				return null;
			}
			public String getText(IContributionItem item) {
				return null;
			}
		};
	}
	public void markDirty() {
	}
	public void prependToGroup(
		String groupName,
		org.eclipse.jface.action.IAction action) {
	}
	public void prependToGroup(
		String groupName,
		org.eclipse.jface.action.IContributionItem item) {
	}
	public org.eclipse.jface.action.IContributionItem remove(String id) {
		return null;
	}
	public org.eclipse.jface.action.IContributionItem remove(
		org.eclipse.jface.action.IContributionItem item) {
		return null;
	}
	public void removeAll() {
	}
	public void update(boolean force) {
	}
}
