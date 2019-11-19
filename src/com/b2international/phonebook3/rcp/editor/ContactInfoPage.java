package com.b2international.phonebook3.rcp.editor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.b2international.phonebook3.rcp.model.Title;

public class ContactInfoPage extends FormPage implements IChangeListener {

	public static final String ID = "com.b2international.phonebook3.rcp.editor.contactinfopage";
	
	private final ContactFormEditor contactFormEditor;
	private final EditorContact contact;
	private final DataBindingContext bindingContext;
	private Text firstNameText;
	private Text lastNameText;
	private DateTime date;
	private Title title;

	public ContactInfoPage(ContactFormEditor editor, String id, String title) {
		super(editor, id, title);
		contactFormEditor = editor;
		contact = editor.getContact();
		bindingContext = new DataBindingContext();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final Composite composite = managedForm.getForm().getBody();
		final FormToolkit toolkit = managedForm.getToolkit();
		
		final GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		final GridData gridData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		composite.setLayoutData(gridData);

		toolkit.createLabel(composite, "Title:");
		final ComboViewer titleViewer = new ComboViewer(composite, SWT.READ_ONLY);
		titleViewer.setContentProvider(ArrayContentProvider.getInstance());
		titleViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Title) {
					Title title = (Title) element;
					return title.getDisplayTitle();
				}
				return super.getText(element);
			}
		});

		titleViewer.setInput(Title.values());
		titleViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().create());
		titleViewer.setSelection(new StructuredSelection(contact.getTitle()));

		toolkit.createLabel(composite, "First name:");
		firstNameText = toolkit.createText(composite, contact.getFirstName());
		firstNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.createLabel(composite, "Last name:");
		lastNameText = toolkit.createText(composite, contact.getLastName());
		lastNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.createLabel(composite, "Date of birth:");
		date = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN);
		date.setLayoutData(GridDataFactory.fillDefaults().create());

		initDataBindingOn(firstNameText, "firstName");
		initDataBindingOn(lastNameText, "lastName");
		initDataBindingOn(date, "dateOfBirth");
		initDataBindingOn(titleViewer, "title");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOn(Text textField, String fieldName) {
		IObservableValue targetObservable = WidgetProperties.text(SWT.Modify).observe(textField);
		IObservableValue modelObservable = PojoProperties.value(EditorContact.class, fieldName).observe(contact);
		Binding binding = bindingContext.bindValue(targetObservable, modelObservable);
		binding.getTarget().addChangeListener(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOn(DateTime dateTime, String fieldName) {
		final IObservableValue targetObservable = WidgetProperties.dateTimeSelection().observe(dateTime);
		final IObservableValue modelObservable = PojoProperties.value(EditorContact.class, fieldName).observe(contact);
	
		final IConverter dateToLocalDateConverter = IConverter.create(Date.class, LocalDate.class, (o1) -> {
			if(o1 instanceof Date) {
				Date date = (Date) o1;
				return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			return LocalDate.now();
		});
		
		final IConverter localDateToDateConverter = IConverter.create(LocalDate.class, Date.class, (o1) -> {
			if(o1 instanceof LocalDate) {
				LocalDate localDate = (LocalDate) o1;
				return Date.from(localDate.atStartOfDay()
					      .atZone(ZoneId.systemDefault())
					      .toInstant());
			}
			return Date.from(LocalDate.now().atStartOfDay()
				      .atZone(ZoneId.systemDefault())
				      .toInstant());
		});
		
		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, 
				UpdateValueStrategy.create(dateToLocalDateConverter), UpdateValueStrategy.create(localDateToDateConverter));
		binding.getTarget().addChangeListener(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOn(ComboViewer comboViewer, String fieldName) {
		final ISWTObservableValue<String> targetObservable = WidgetProperties.comboSelection().observe(comboViewer.getCombo());
		final IObservableValue modelObservable = PojoProperties.value(EditorContact.class, fieldName).observe(contact);
		
		final IConverter stringToTitleConverter = IConverter.create(String.class, Title.class, (o1) -> {
			final String title = (String) o1;
			return Title.getValueFrom(title);
		});
		
		final IConverter titleToStringConverter = IConverter.create(Title.class, String.class, (o1) -> {
			if(o1 instanceof Title) {
				final Title title = (Title) o1;
				return title.getDisplayTitle();
			}
			return Title.DEFAULT.getDisplayTitle();
		});
		final UpdateValueStrategy modelToWidgetStrategy = UpdateValueStrategy.create(titleToStringConverter);
		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, 
			 	UpdateValueStrategy.create(stringToTitleConverter), modelToWidgetStrategy);
		
		binding.getTarget().addChangeListener(this);
	}

	@Override
	public void handleChange(ChangeEvent event) {
		contactFormEditor.notifyTableEditing();
	}

}
