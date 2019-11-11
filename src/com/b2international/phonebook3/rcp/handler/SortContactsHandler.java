package com.b2international.phonebook3.rcp.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.phonebook3.rcp.ui.ContactTreeView;

public class SortContactsHandler extends AbstractHandler{
	
	public static final String ID = "com.b2international.phonebook3.rcp.sortcontacts";
	
	private boolean isAscending = true;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		isAscending = !isAscending;
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		final IViewPart part = activeWorkbenchWindow.getActivePage().findView(ContactTreeView.ID);
		if(part instanceof ContactTreeView) {
			final ContactTreeView contactTreeView = (ContactTreeView) part;
			contactTreeView.sort(isAscending);
		}
		
		return null;
	}
	
}
