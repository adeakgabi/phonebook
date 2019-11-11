package com.b2international.phonebook3.rcp.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.b2international.phonebook3.rcp.api.ContactService;
import com.b2international.phonebook3.rcp.editor.ContactEditorInput;
import com.b2international.phonebook3.rcp.editor.ContactFormEditor;
import com.b2international.phonebook3.rcp.model.Contact;

public class ContactWizard extends Wizard {
	
	private ContactWizardInfoPage infoPage;
	private ContactWizardPhoneAddressPage phoneAddressPage;
	private final ContactService contactService = ContactService.getInstance(); 
	private final Contact contact = Contact.createEmptyContact();
	
	public ContactWizard() {
		setNeedsProgressMonitor(true);
	}
	
    @Override
    public String getWindowTitle() {
        return "Add new contact";
    }
    
    @Override
    public void addPages() {
        infoPage = new ContactWizardInfoPage(contact);
        phoneAddressPage = new ContactWizardPhoneAddressPage(contact);
        addPage(infoPage);
        addPage(phoneAddressPage);
    }
	
	@Override
	public boolean performFinish() {
		phoneAddressPage.updatePhoneAndAddress(); 
		contactService.saveContact(contact);
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(new ContactEditorInput(contact), ContactFormEditor.ID);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
}
