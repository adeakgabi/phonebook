package com.b2international.phonebook3.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.b2international.phonebook3.rcp.ui.ContactTreeView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "com.b2international.phonebook3.rcp.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		layout.addView(ContactTreeView.ID, IPageLayout.LEFT, 0.3f, editorArea);
		layout.addPerspectiveShortcut(ID);
	}

}
