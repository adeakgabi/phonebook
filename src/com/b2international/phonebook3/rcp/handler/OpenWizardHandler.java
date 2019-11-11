package com.b2international.phonebook3.rcp.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.b2international.phonebook3.rcp.wizard.ContactWizard;

public final class OpenWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final WizardDialog wizardDialog = new WizardDialog(shell, new ContactWizard());
	    wizardDialog.open();
		return null;
	}

}
