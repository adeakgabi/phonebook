package com.b2international.phonebook3.rcp.contact;

import org.eclipse.ui.forms.editor.FormEditor;

import com.b2international.phonebook3.rcp.redux.Action;

public class CloseEditorAction implements Action {
	
	private final FormEditor editorToClose;
	

	public CloseEditorAction(FormEditor editorToClose) {
		this.editorToClose = editorToClose;
	}


	public FormEditor getEditorToClose() {
		return editorToClose;
	}

}
