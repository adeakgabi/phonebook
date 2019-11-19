package com.b2international.phonebook3.rcp.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.b2international.phonebook3.rcp.Activator;
import com.b2international.phonebook3.rcp.contact.CreateContactAction;
import com.b2international.phonebook3.rcp.contact.OpenEditorAction;
import com.b2international.phonebook3.rcp.editor.ContactEditorInput;
import com.b2international.phonebook3.rcp.editor.ContactFormEditor;
import com.b2international.phonebook3.rcp.editor.EditorContact;
import com.b2international.phonebook3.rcp.model.Contact;

public class ContactWizard extends Wizard {
	
	private ContactWizardInfoPage infoPage;
	private ContactWizardPhoneAddressPage phoneAddressPage;
	private EditorContact editorContact = new EditorContact(Contact.createEmptyContact());
	
	public ContactWizard() {
		setNeedsProgressMonitor(true);
	}
	
    @Override
    public String getWindowTitle() {
        return "Add new contact";
    }
    
    @Override
    public void addPages() {
        infoPage = new ContactWizardInfoPage(editorContact);
        phoneAddressPage = new ContactWizardPhoneAddressPage(editorContact);
        addPage(infoPage);
        addPage(phoneAddressPage);
    }
	
	@Override
	public boolean performFinish() {
		phoneAddressPage.updatePhoneAndAddress();
		CreateContactAction createContactAction = new CreateContactAction(editorContact.getTitle(), editorContact.getFirstName(), editorContact.getLastName(), 
				Contact.toDateString(editorContact.getDateOfBirth()), editorContact.getPhoneNumbers(), editorContact.getAddresses());
		String editorContactId = createContactAction.getId();
		Activator.getStore().dispatch(createContactAction);
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		Activator.getStore().dispatch(new OpenEditorAction(new ContactEditorInput(editorContactId), ContactFormEditor.ID, page));
		return true;
	}
	
}
