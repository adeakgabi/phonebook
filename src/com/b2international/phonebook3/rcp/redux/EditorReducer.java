package com.b2international.phonebook3.rcp.redux;

import java.util.Set;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;

public class EditorReducer implements Reducer<Set<FormEditor>> {

	@SuppressWarnings("incomplete-switch")
	@Override
	public Set<FormEditor> reduce(Set<FormEditor> state, Action action) {
		Set<FormEditor> currentState = state;
		FormEditor activeEditor = (FormEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(action instanceof Actions) {
			switch((Actions) action) {
			case OPEN_EDITOR:
				currentState.add(activeEditor);
				return currentState;
			case CLOSE_EDITOR:
				currentState.remove(activeEditor);
			}
		}
		return state;
	}

}
