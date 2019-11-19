package com.b2international.phonebook3.rcp.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.b2international.phonebook3.rcp.Activator;
import com.b2international.phonebook3.rcp.ImageConstants;
import com.b2international.phonebook3.rcp.api.ContactFilter;
import com.b2international.phonebook3.rcp.contact.OpenEditorAction;
import com.b2international.phonebook3.rcp.editor.ContactEditorInput;
import com.b2international.phonebook3.rcp.editor.ContactFormEditor;
import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateChangeListener;

public class ContactTreeView extends ViewPart implements StateChangeListener<State> {

	public static final String ID = "com.b2international.phonebook3.rcp.ui.contacttreeview";
	public static final Image CONTACT_ICON = Activator.getImage(ImageConstants.CONTACT_ICON);
	public static final Image ADDRESS_ICON	 = Activator.getImage(ImageConstants.ADDRESS_ICON);

	private TreeViewer viewer;

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		Activator.getStore().addStateChangeListener(this);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Activator.getStore().removeStateChangeListener(this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		setPartName("Contact");
		final FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		viewer = new TreeViewer(parent);
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(false);
		tree.setLinesVisible(false);
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new ContactLabelProvider());
		onStateChange(null, Activator.getStore().getState());
		createContextMenu(viewer);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				final Object selectedNode = selection.getFirstElement();
				if (selectedNode != null && selectedNode instanceof Contact) {
					final Contact contact = (Contact) selectedNode;
					final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						Activator.getStore().dispatch(new OpenEditorAction(new ContactEditorInput(contact.getId()), ContactFormEditor.ID, page));
				}
			}
		});
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private class ContactLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof Contact) {
				final Contact contact = (Contact) element;
				return contact.getFirstName() + " " + contact.getLastName();
			} else if (element instanceof Address) {
				final Address address = (Address) element;
				return address.getCity() + "-" + address.getCountry(); 
			}
			return super.getText(element);
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof Contact) {
				return CONTACT_ICON;
			} else if(element instanceof Address) {
				return ADDRESS_ICON;
			}
			return super.getImage(element);
		}

	}

	@Override
	public void onStateChange(State oldState, State newState) {
		viewer.setInput(newState.getContactsMap());
	}

	protected void createContextMenu(Viewer viewer) {
		final MenuManager contextMenu = new MenuManager("#ViewerMenu");
		contextMenu.setRemoveAllWhenShown(true);
		final Menu menu = contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, viewer);
		getSite().setSelectionProvider(viewer);
	}
	
	public void sort(boolean isAscending) {

		if (isAscending) {
			viewer.setComparator(new ViewerComparator() {
				public int compare(Viewer viewer, Object e1, Object e2) {
					Contact c1 = (Contact) e1;
					Contact c2 = (Contact) e2;
					if (c1 == null) {
						return 1;
					} else if (c2 == null) {
						return -1;
					} else {
						return (c1.getFirstName().compareToIgnoreCase(c2.getFirstName()));
					}
				}
			});
		} else {
			viewer.setComparator(new ViewerComparator() {
				public int compare(Viewer viewer, Object e1, Object e2) {
					Contact c1 = (Contact) e1;
					Contact c2 = (Contact) e2;
					if (c1 == null) {
						return 1;
					} else if (c2 == null) {
						return -1;
					} else {
						return c2.getFirstName().compareToIgnoreCase(c1.getFirstName());
					}
				}
			});
		}
	}
	
	public List<Contact> filter(ContactFilter filter) {
		final State state = Activator.getStore().getState();
		final Map<String, Contact> newState = new HashMap<>(state.getContactsMap());
		return newState.values().stream()
				.filter(filter::byTitle)
				.filter(filter::byFirstName)
				.filter(filter::byLastName)
				.filter(filter::byDayOfBirth)
				.filter(filter::byPhoneNumber)
				.filter(filter::byAddress)
				.collect(Collectors.toList());
	}
}
