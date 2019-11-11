package com.b2international.phonebook3.rcp.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;

public class ContactPhoneAddressPage extends FormPage {
	
	public static final String ID = "com.b2international.phonebook3.rcp.editor.contactphoneaddresspage";
	
	private ContactFormEditor contactFormEditor;
	private Contact contact;
	private TableViewer addressViewer;
	private TableViewer phoneNumberViewer;

	public ContactPhoneAddressPage(ContactFormEditor editor, String id, String title) {
		super(editor, id, title);
		contactFormEditor = editor;
		contact = editor.getContact();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final Composite composite = managedForm.getForm().getBody();
		composite.setLayout(new GridLayout());
		
		final Group addressGroup = new Group(composite, SWT.NONE);
		addressGroup.setLayout(new GridLayout());
		addressGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addressGroup.setText("Address");
		
		addressViewer = new TableViewer(addressGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createAddressColumns(addressViewer);
		final Table addressTable = addressViewer.getTable();
		addressTable.setHeaderVisible(true);
		addressTable.setLinesVisible(true);
		addressViewer.setContentProvider(new ArrayContentProvider());
		addressViewer.setInput(contact.getAddresses());
		
		final Group phoneNumberGroup = new Group(composite, SWT.TOP);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = SWT.TOP;
		phoneNumberGroup.setLayout(gridLayout);
		phoneNumberGroup.setText("Phone number");
		phoneNumberViewer = new TableViewer(phoneNumberGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        final TableViewerColumn viewerColumn = new TableViewerColumn(phoneNumberViewer,
                SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText("Phone number");
        column.setWidth(100);
        column.setResizable(true);
        column.setMoveable(true);
        
        viewerColumn.setEditingSupport(new PropertyEditingSupport<String>(phoneNumberViewer, phoneNumber -> phoneNumber, (phoneNumber, newPhoneNumber) -> {
			List<String> newPhoneNumbers = new ArrayList<String>(contact.getPhoneNumbers());
			if(!newPhoneNumbers.contains(newPhoneNumber)) {
				contactFormEditor.notifyTableEditing();
				newPhoneNumbers.remove(phoneNumber);
				newPhoneNumbers.add((String) newPhoneNumber);
				contact.setPhoneNumbers(newPhoneNumbers);
			}
			phoneNumberViewer.setInput(contact.getPhoneNumbers());
		}));
        
        viewerColumn.setLabelProvider(new ColumnLabelProvider() {
            
        	@Override
            public String getText(Object element) {
                String phoneNumberString = (String) element;
                return phoneNumberString;
            }
        });
		
		final Table phoneTable = phoneNumberViewer.getTable();
		phoneTable.setHeaderVisible(false);
		phoneTable.setLinesVisible(true);
		
		phoneNumberViewer.setContentProvider(new ArrayContentProvider());
		phoneNumberViewer.setInput(contact.getPhoneNumbers());
	}
	
    private void createAddressColumns(final TableViewer viewer) {
        String[] titles = {"Country", "City", "ZipCode", "Street"};
        int[] bounds = { 100, 100, 100, 100 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setEditingSupport(new PropertyEditingSupport<Address>(viewer, Address::getCountry, (address, newValue) -> {
        	if(address.getCountry() != newValue) {
        		contactFormEditor.notifyTableEditing();
        	}
			address.setCountry((String) newValue);
			viewer.update(address, null);
			
		}));
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Address address = (Address) element;
                return address.getCountry();
            }
        });
        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setEditingSupport(new PropertyEditingSupport<Address>(viewer, Address::getCity, (address, newValue) -> {
        	contactFormEditor.notifyTableEditing();
        	address.setCity((String) newValue);
        	viewer.update(address, null);
        }));
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Address address = (Address) element;
                return address.getCity();
            }
        });
        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setEditingSupport(new PropertyEditingSupport<Address>(viewer, Address::getZipCode, (address, newValue) -> {
        	contactFormEditor.notifyTableEditing();
        	address.setZipCode((String) newValue);
        	viewer.update(address, null);
        	
        }));
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Address address = (Address) element;
                return address.getZipCode();
            }
        });
        col = createTableViewerColumn(titles[3], bounds[3], 3);
        col.setEditingSupport(new PropertyEditingSupport<Address>(viewer, Address::getStreet, (address, newValue) -> {
        	contactFormEditor.notifyTableEditing();
        	address.setStreet((String) newValue);
        	viewer.update(address, null);
        }));
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
               	Address address = (Address) element;
                return address.getStreet();
            }
        });
    }
    
    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(addressViewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

}
