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
package org.eclipse.pde.internal.builders;

import java.io.*;
import java.net.*;
import java.util.Map;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.*;
import org.eclipse.pde.internal.PDE;
import org.eclipse.pde.internal.core.*;

public class ExtensionPointSchemaBuilder extends IncrementalProjectBuilder {
	public static final String BUILDERS_SCHEMA_COMPILING =
		"Builders.Schema.compiling";
	public static final String BUILDERS_SCHEMA_COMPILING_SCHEMAS =
		"Builders.Schema.compilingSchemas";
	public static final String BUILDERS_UPDATING = "Builders.updating";
	public static final String BUILDERS_SCHEMA_REMOVING =
		"Builders.Schema.removing";

	private ISchemaTransformer transformer;
	private URL cssURL;

	class DeltaVisitor implements IResourceDeltaVisitor {
		private IProgressMonitor monitor;
		public DeltaVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}
		public boolean visit(IResourceDelta delta) {
			IResource resource = delta.getResource();

			if (resource instanceof IProject) {
				// Only check projects with plugin nature
				IProject project = (IProject) resource;
				return isInterestingProject(project);
			}
			if (resource instanceof IFolder)
				return true;
			if (resource instanceof IFile) {
				// see if this is it
				IFile candidate = (IFile) resource;

				if (isSchemaFile(candidate)) {
					// That's it, but only check it if it has been added or changed
					if (delta.getKind() != IResourceDelta.REMOVED) {
						compileFile(candidate, monitor);
						return true;
					} else {
						removeOutputFile(candidate, monitor);
					}
				}
			}
			return false;
		}
	}

	public ExtensionPointSchemaBuilder() {
		super();
		transformer = new SchemaTransformer();
	}
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
		throws CoreException {

		IResourceDelta delta = null;

		if (kind != FULL_BUILD)
			delta = getDelta(getProject());

		if (delta == null || kind == FULL_BUILD) {
			// Full build
			IProject project = getProject();
			if (!isInterestingProject(project))
				return null;
			IPath path = project.getFullPath().append(getSchemaLocation());
			IWorkspace workspace = project.getWorkspace();
			if (workspace.getRoot().exists(path)) {
				IResource res = workspace.getRoot().findMember(path);
				if (res != null && res instanceof IFolder) {
					compileSchemasIn((IFolder) res, monitor);
				}
			}
		} else {
			delta.accept(new DeltaVisitor(monitor));
		}
		return null;
	}

	private boolean isInterestingProject(IProject project) {
		if (!PDE.hasPluginNature(project))
			return false;
		if (WorkspaceModelManager.isBinaryPluginProject(project))
			return false;
		// This is it - a plug-in project that is not external or binary
		return true;
	}
	private void compileFile(IFile file, IProgressMonitor monitor) {
		String message =
			PDE.getFormattedMessage(
				BUILDERS_SCHEMA_COMPILING,
				file.getFullPath().toString());
		monitor.subTask(message);
		PluginErrorReporter reporter = new PluginErrorReporter(file);

		String outputFileName = getOutputFileName(file);
		IWorkspace workspace = file.getWorkspace();
		Path outputPath = new Path(outputFileName);

		try {
			InputStream source = file.getContents(false);
			StringWriter stringWriter = new StringWriter();
			PrintWriter pwriter = new PrintWriter(stringWriter);
			URL url = null;
			
			try {
				url = new URL("file:"+file.getLocation().toOSString());
			}
			catch (MalformedURLException e) {
			}

			transform(url, source, pwriter, reporter, cssURL);
			stringWriter.close();
			if (reporter.getErrorCount() == 0
				&& CompilerFlags.getBoolean(CompilerFlags.S_CREATE_DOCS)) {
				String docLocation = getDocLocation();
				ensureFoldersExist(file.getProject(), docLocation);
				IFile outputFile = workspace.getRoot().getFile(outputPath);
				ByteArrayInputStream target =
					new ByteArrayInputStream(
						stringWriter.toString().getBytes("UTF8"));
				if (!workspace.getRoot().exists(outputPath)) {
					// the file does not exist - create it
					outputFile.create(target, true, monitor);
				} else {
					outputFile.setContents(target, true, false, monitor);
				}
			}
		} catch (UnsupportedEncodingException e) {
			PDE.logException(e);
		} catch (CoreException e) {
			PDE.logException(e);
		} catch (IOException e) {
			PDE.logException(e);
		}
		monitor.subTask(PDE.getResourceString(BUILDERS_UPDATING));
		monitor.done();
	}
	
	private void ensureFoldersExist(IProject project, String pathName) throws CoreException {
		IPath path = new Path(pathName);
		IContainer parent=project;
		
		for (int i=0; i<path.segmentCount(); i++){
			String segment = path.segment(i);
			IFolder folder = parent.getFolder(new Path(segment));
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
			parent = folder;
		}
	}

	private void compileSchemasIn(IFolder folder, IProgressMonitor monitor)
		throws CoreException {
		monitor.subTask(
			PDE.getResourceString(BUILDERS_SCHEMA_COMPILING_SCHEMAS));

		IResource[] members = folder.members();

		for (int i = 0; i < members.length; i++) {
			IResource member = members[i];
			if (member instanceof IFolder)
				compileSchemasIn((IFolder) member, monitor);
			else if (member instanceof IFile && isSchemaFile((IFile) member)) {
				compileFile((IFile) member, monitor);
			}
		}
		monitor.done();
	}
	public String getDocLocation() {
		return CompilerFlags.getString(CompilerFlags.S_DOC_FOLDER);
	}
	private String getOutputFileName(IFile file) {
		String fileName = file.getName();
		int dot = fileName.lastIndexOf('.');
		String pageName = fileName.substring(0, dot) + ".html";
		String mangledPluginId = getMangledPluginId(file);
		if (mangledPluginId!=null)
		   pageName = mangledPluginId + "_"+pageName;
		IPath path =
			file.getProject().getFullPath().append(getDocLocation()).append(
				pageName);
		return path.toString();
	}
	
	private String getMangledPluginId(IFile file) {
		IProject project = file.getProject();
		WorkspaceModelManager manager = PDECore.getDefault().getWorkspaceModelManager();
		IModel model = manager.getWorkspaceModel(project);
		if (model instanceof IPluginModelBase) {
			IPluginBase plugin = ((IPluginModelBase)model).getPluginBase();
			if (plugin!=null) {
				String id = plugin.getId();
				return id.replace('.', '_');
			}
		}
		return null;
	}
	
	public String getSchemaLocation() {
		return "schema";
	}
	
	public URL getCSSURL(){
		return cssURL;
	}
	
	public void setCSSURL(String url){
		try {
			cssURL = new URL(url);
		} catch (MalformedURLException e) {
			PDE.logException(e);
		}
	}
	
	public void setCSSURL(URL url){
		cssURL = url;
	}
	
	private boolean isSchemaFile(IFile file) {
		String name = file.getName();
		return name.endsWith(".exsd") || name.endsWith(".xsd");
	}
	private void removeOutputFile(IFile file, IProgressMonitor monitor) {
		String outputFileName = getOutputFileName(file);
		String message =
			PDE.getFormattedMessage(BUILDERS_SCHEMA_REMOVING, outputFileName);

		monitor.subTask(message);

		IWorkspace workspace = file.getWorkspace();
		IPath path = new Path(outputFileName);
		if (workspace.getRoot().exists(path)) {
			IFile outputFile = workspace.getRoot().getFile(path);
			if (outputFile != null) {
				try {
					outputFile.delete(true, true, monitor);
				} catch (CoreException e) {
					PDE.logException(e);
				}
			}
		}
		monitor.done();
	}
	protected void startupOnInitialize() {
		super.startupOnInitialize();
	}
	private void transform(
		URL schemaURL,
		InputStream input,
		PrintWriter output,
		PluginErrorReporter reporter,
		URL cssURL) {
		transformer.transform(schemaURL, input, output, reporter, cssURL);
	}
}
