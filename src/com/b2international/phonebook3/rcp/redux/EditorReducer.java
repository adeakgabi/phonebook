package com.b2international.phonebook3.rcp.redux;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;

import com.b2international.phonebook3.rcp.contact.CloseEditorAction;
import com.b2international.phonebook3.rcp.contact.OpenEditorAction;

public class EditorReducer implements Reducer<Set<FormEditor>> {

	@Override
	public Set<FormEditor> reduce(Set<FormEditor> state, Action action) {
		if (action instanceof OpenEditorAction) {
			FormEditor activeEditor = (FormEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			final Set<FormEditor> newState = new HashSet<>(state);
			final IWorkbenchPage page = ((OpenEditorAction) action).getPage();
			try {
				page.openEditor(((OpenEditorAction) action).getContactEditorInput(), ((OpenEditorAction) action).getEditorId());
			} catch (PartInitException e) {
				throw new RuntimeException(e);
			}
			activeEditor = (FormEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			newState.add(activeEditor);
			return newState;
		} else if (action instanceof CloseEditorAction) {
			final Set<FormEditor> newState = new HashSet<>(state);
			final FormEditor editorToClose = ((CloseEditorAction) action).getEditorToClose();
			newState.remove(editorToClose);
			return newState;
		}
		return state;
	}

}
