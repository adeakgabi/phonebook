package com.b2international.phonebook3.rcp.contact;

import org.eclipse.ui.IWorkbenchPage;

import com.b2international.phonebook3.rcp.editor.ContactEditorInput;
import com.b2international.phonebook3.rcp.redux.Action;

public class OpenEditorAction implements Action {
	
	private final ContactEditorInput contactEditorInput;
	private final String editorId;
	private final IWorkbenchPage page;
	
	public OpenEditorAction(ContactEditorInput contactEditorInput, String editorId, IWorkbenchPage page) {
		this.contactEditorInput = contactEditorInput;
		this.editorId = editorId;
		this.page = page;
	}

	public ContactEditorInput getContactEditorInput() {
		return contactEditorInput;
	}

	public String getEditorId() {
		return editorId;
	}
	
	public IWorkbenchPage getPage() {
		return page;
	}
	

}
