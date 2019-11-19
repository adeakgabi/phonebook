package com.b2international.phonebook3.rcp.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import com.b2international.phonebook3.rcp.Activator;
import com.b2international.phonebook3.rcp.contact.CloseEditorAction;
import com.b2international.phonebook3.rcp.contact.ContactUpdateAction;
import com.b2international.phonebook3.rcp.exceptions.WrongInputException;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateChangeListener;

public class ContactFormEditor extends FormEditor implements StateChangeListener<State> {
	
	public static final String ID = "com.b2international.phonebook3.rcp.editor.contactformeditor";
	
	private FormPage infoPage;
	private FormPage addressPhoneNumber;
	private EditorContact editorContact;
	protected boolean dirty;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof ContactEditorInput) {
			super.init(site, input);
			final ContactEditorInput editorInput = (ContactEditorInput) input;
			editorContact = new EditorContact(Activator.getStore().getState().getContact(editorInput.getContactId()));
			setPartName(editorContact.getFirstName() + " " + editorContact.getLastName());
			Activator.getStore().addStateChangeListener(this);
		} else {
			throw new PartInitException("Wrong editor input." + input);
		}
	}
	
	@Override
	protected void addPages() {
		infoPage = new ContactInfoPage(this, ContactInfoPage.ID, "Personal information");
		addressPhoneNumber = new ContactPhoneAddressPage(this, ContactPhoneAddressPage.ID, "Contact details"); 
		try {
			addPage(infoPage);
			addPage(addressPhoneNumber);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
			try {
				Activator.getStore().dispatch(ContactUpdateAction.builder(editorContact.getId())
						.title(editorContact.getTitle())
						.firstName(editorContact.getFirstName())
						.lastName(editorContact.getLastName())
						.dateOfBirth(editorContact.getDateOfBirth())
						.phoneNumbers(editorContact.getPhoneNumbers())
						.addresses(editorContact.getAddresses())
						.build());
			} catch (WrongInputException e) {
				e.printStackTrace();
			}
		dirty = false;
		final String fullName = editorContact.getFirstName() + " " + editorContact.getLastName();
		setPartName(fullName);
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
		// Ignore
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public EditorContact getContact() {
		return editorContact;
	}

	public void notifyTableEditing() {
		dirty = true;
		firePropertyChange(PROP_DIRTY);
	}
	
	public String getPartName() {
		return editorContact.getFirstName() + " " + editorContact.getLastName();
	}

	@Override
	public String toString() {
		return "FormEditor: " + editorContact.getFirstName() + " " + editorContact.getLastName();
	}
	
	@Override
	public void dispose() {
		Activator.getStore().dispatch(new CloseEditorAction(this));
	}

	@Override
	public void onStateChange(State oldState, State newState) {
		
	}

}
