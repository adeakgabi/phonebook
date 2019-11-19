package com.b2international.phonebook3.rcp.handler;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.phonebook3.rcp.Activator;
import com.b2international.phonebook3.rcp.contact.BulkRemoveAction;
import com.b2international.phonebook3.rcp.contact.DeleteContactAction;
import com.b2international.phonebook3.rcp.model.Contact;
import com.google.common.collect.Lists;

public class DeleteContactHandler extends AbstractHandler {

	public static final String ID = "com.b2international.phonebook3.rcp.deletecontact";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);

		if (selection.isEmpty()) {
			return null;
		}

		if (selection.size() == 1) {
			final Object selectedNode = selection.getFirstElement();

			if (selectedNode == null || !(selectedNode instanceof Contact)) {
				return null;
			}
			
			final Contact contact = (Contact) selectedNode;

			final boolean confirmation = MessageDialog.openConfirm(
					HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Confirm",
					"Are you sure you want to delete contact " + contact.getFirstName() + " " + contact.getLastName()
							+ "?");

			if (confirmation) {
				Activator.getStore().dispatch(new DeleteContactAction(contact.getId()));
			}
			
		} else {
			final Collection<String> contactIdsToRemove = Lists.newArrayList();
			for (Object selectedNode : selection.toList()) {
				if (selectedNode == null || !(selectedNode instanceof Contact)) {
					return null;
				}
				
				final Contact contact = (Contact) selectedNode;
				contactIdsToRemove.add(contact.getId());
			}

			final boolean confirmation = MessageDialog.openConfirm(
					HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Confirm",
					"Are you sure you want to delete multiple contacts?");
			if (confirmation) {
					Activator.getStore().dispatch(new BulkRemoveAction(contactIdsToRemove));
			}
		}
		return null;
	}

}
