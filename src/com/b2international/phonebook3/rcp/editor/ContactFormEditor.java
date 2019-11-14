package com.b2international.phonebook3.rcp.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import com.b2international.phonebook3.rcp.api.ContactService;
import com.b2international.phonebook3.rcp.model.Contact;

public class ContactFormEditor extends FormEditor {
	
	public static final String ID = "com.b2international.phonebook3.rcp.editor.contactformeditor";
	
	private FormPage infoPage;
	private FormPage addressPhoneNumber;
	private Contact contact;
	protected boolean dirty;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof ContactEditorInput) {
			super.init(site, input);
			final ContactEditorInput editorInput = (ContactEditorInput) input;
			contact = editorInput.getContact();
			setPartName(contact.getFirstName() + " " + contact.getLastName());
			return;
		}
		throw new PartInitException("Wrong editor input." + input);
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
		final ContactService service = ContactService.getInstance();
		service.saveContact(contact);
		dirty = false;
		final String fullName = contact.getFirstName() + " " + contact.getLastName();
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
	
	public Contact getContact() {
		return contact;
	}

	public void notifyTableEditing() {
		dirty = true;
		firePropertyChange(PROP_DIRTY);
	}
	
	public String getPartName() {
		return contact.getFirstName() + " " + contact.getLastName();
	}

	@Override
	public String toString() {
		return "FormEditor: " + contact.getFirstName() + " " + contact.getLastName();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}
