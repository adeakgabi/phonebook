package com.b2international.phonebook3.rcp;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		configurer.setShowPerspectiveBar(true);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}
	
	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}
	
	

}
