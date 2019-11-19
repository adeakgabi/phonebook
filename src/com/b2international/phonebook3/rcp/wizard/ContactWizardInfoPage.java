package com.b2international.phonebook3.rcp.wizard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

import com.b2international.phonebook3.rcp.editor.EditorContact;
import com.b2international.phonebook3.rcp.model.Title;

public final class ContactWizardInfoPage extends AbstractContactWizardPage<EditorContact> {

	private static final String DATE_ERROR_KEY = "dateErrorMsg";
	private static final String LAST_NAME_ERROR_KEY = "lastNameErrorMsg";
	private static final String FIRST_NAME_ERROR_KEY = "firstNameErrorMsg";
	private Text firstNameText;
	private Text lastNameText;
	private boolean isFirstNameValid = false;
	private boolean isLastNameValid = false;
	private boolean isDateValid = false;
	private final Map<String, String> errorMessageMap = new HashMap<>();

	public ContactWizardInfoPage(EditorContact editorContact) {
		super(editorContact, "Contact Info");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		composite.setSize(500, 500);

		createLabel(composite, "Title:");
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
		initDataBindingOn(titleViewer, "title");

		createLabel(composite, "First name:");
		firstNameText = createText(composite);
		initDataBindingOn(firstNameText, "firstName", EditorContact.class, editorContact, (firstName) -> {
			if (firstName instanceof String) {
				final String firstNameText = (String) firstName;
				if (firstNameText.isEmpty()) {
					isFirstNameValid = false;
					final String errorMsg = "First name cannot be empty.";
					errorMessageMap.put(FIRST_NAME_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				} else if (!firstNameText.matches(HUNGARIAN_CHARSET_REGEX)) {
					isFirstNameValid = false;
					final String errorMsg = "Incorrect first name.";
					errorMessageMap.put(FIRST_NAME_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.error(errorMsg);
				}
				isFirstNameValid = true;
				errorMessageMap.remove(FIRST_NAME_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		createLabel(composite, "Last name:");
		lastNameText = createText(composite);
		initDataBindingOn(lastNameText, "lastName", EditorContact.class, editorContact, (value) -> {
			if (value instanceof String) {
				final String stringValue = (String) value;
				if (stringValue.isEmpty()) {
					isLastNameValid = false;
					final String errorMsg = "Last name cannot be empty.";
					errorMessageMap.put(LAST_NAME_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				} else if (!((String) value).matches(HUNGARIAN_CHARSET_REGEX)) {
					isLastNameValid = false;
					final String errorMsg = "Incorrect last name.";
					errorMessageMap.put(LAST_NAME_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.error(errorMsg);
				}
				isLastNameValid = true;
				errorMessageMap.remove(LAST_NAME_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		createLabel(composite, "Date of birth:");
		final DateTime date = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN);
		date.setLayoutData(GridDataFactory.fillDefaults().create());
		initDataBindingOn(date, "dateOfBirth", (value) -> {
			final LocalDate today = LocalDate.now();
			final LocalDate userSelectionLocalDateFormat = LocalDate.of(date.getYear(), (date.getMonth()+1), date.getDay());
			if (userSelectionLocalDateFormat.compareTo(today) > 0) {
				isDateValid = false;
				final String errorMsg = "Invalid date of birth.";
				errorMessageMap.put(DATE_ERROR_KEY, errorMsg);
				validatePage();
				return ValidationStatus.error(errorMsg);
			}
			isDateValid = true;
			errorMessageMap.remove(DATE_ERROR_KEY);
			validatePage();
			return ValidationStatus.ok();
		});

		setControl(composite);
		setErrorMessage(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOn(ComboViewer comboViewer, String fieldName) {
		final ISWTObservableValue<String> targetObservable = WidgetProperties.comboSelection().observe(comboViewer.getCombo());
		final IObservableValue modelObservable = PojoProperties.value(EditorContact.class, fieldName).observe(editorContact);
		
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
		bindingContext.bindValue(targetObservable, modelObservable, 
				UpdateValueStrategy.create(stringToTitleConverter), modelToWidgetStrategy);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOn(DateTime dateTime, String fieldName, Function<Object, IStatus> validator) {
		final IObservableValue targetObservable = WidgetProperties.dateTimeSelection().observe(dateTime);
		final IObservableValue modelObservable = PojoProperties.value(EditorContact.class, fieldName).observe(editorContact);
		
		final IConverter dateToLocalDateConverter = IConverter.create(Date.class, LocalDate.class, (o1) -> {
			if(o1 instanceof Date) {
				final Date date = (Date) o1;
				return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			return LocalDate.now();
		});
		
		final IConverter localDateToDateConverter = IConverter.create(LocalDate.class, Date.class, (o1) -> {
			if(o1 instanceof LocalDate) {
				final LocalDate localDate = (LocalDate) o1;
				return Date.from(localDate.atStartOfDay()
					      .atZone(ZoneId.systemDefault())
					      .toInstant());
			}
			return Date.from(LocalDate.now().atStartOfDay()
				      .atZone(ZoneId.systemDefault())
				      .toInstant());
		});
		final UpdateValueStrategy modelToWidgetStrategy = UpdateValueStrategy.create(dateToLocalDateConverter);
		modelToWidgetStrategy.setBeforeSetValidator(value -> validator.apply(value));
		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, 
				modelToWidgetStrategy, UpdateValueStrategy.create(localDateToDateConverter));
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT);
	}

	protected void validatePage() {
		if (isFirstNameValid && isLastNameValid && isDateValid) {
			setPageComplete(true);
			setErrorMessage(null);
		} else {
			if (!isDateValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(DATE_ERROR_KEY));
			}
			if (!isLastNameValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(LAST_NAME_ERROR_KEY));
			}
			if (!isFirstNameValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(FIRST_NAME_ERROR_KEY));
			}
		}
	}

}
