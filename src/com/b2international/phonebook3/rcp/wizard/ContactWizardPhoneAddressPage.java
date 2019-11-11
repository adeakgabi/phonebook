package com.b2international.phonebook3.rcp.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.b2international.phonebook3.rcp.model.Address;
import com.b2international.phonebook3.rcp.model.Contact;
import com.google.common.collect.Lists;

public final class ContactWizardPhoneAddressPage extends AbstractContactWizardPage<Address> {

	private static final String PHONE_NUMBER_ERROR_KEY = "phoneNumberErrorKey";
	private static final String STREET_ERROR_KEY = "streetErrorKey";
	private static final String CITY_ERROR_KEY = "cityErrorKey";
	private static final String ZIP_CODE_ERROR_KEY = "zipCodeErrorKey";
	private static final String COUNTRY_ERROR_KEY = "countryErrorKey";
	private Address address = new Address("", "", "", "");
	private String phoneNumber = "";
	private boolean isPhoneNumberValid = false;
	private boolean isCountryValid = false;
	private boolean isCityValid = false;
	private boolean isZipCodeValid = false;
	private boolean isStreetValid = false;
	private Map<String, String> errorMessageMap = new HashMap<>();

	public ContactWizardPhoneAddressPage(Contact contact) {
		super(contact, "Address, Phone number");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		
		final Group addressGroup = new Group(composite, SWT.NONE);
		addressGroup.setLayout(new GridLayout());
		addressGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addressGroup.setText("Address");
		
		createLabel(addressGroup, "Country:");
		final Text country = createText(addressGroup);
		initDataBindingOn(country, "country", Address.class, address, (value) -> {
			if (value instanceof String) {
				String userInput = (String) value;
				if (userInput.isEmpty()) {
					isCountryValid = false;
					final String errorMsg = "Country cannot be empty.";
					errorMessageMap.put(COUNTRY_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				} else if (!userInput.matches(HUNGARIAN_CHARSET_REGEX)) {
					isCountryValid = false;
					final String errorMsg = "Incorrect country name.";
					errorMessageMap.put(COUNTRY_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.error(errorMsg);
				}
				isCountryValid = true;
				address.setCountry((String) value);
				errorMessageMap.remove(COUNTRY_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		createLabel(addressGroup, "Zip Code:");
		final Text zipCode = createText(addressGroup);
		initDataBindingOn(zipCode, "zipCode", Address.class, address, (value) -> {
			if (value instanceof String) {
				final String userInput = (String) value;
				if (userInput.isEmpty()) {
					isZipCodeValid = false;
					final String errorMsg = "Zipcode field cannot be empty.";
					errorMessageMap.put(ZIP_CODE_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				} else if (!userInput.matches("\\d{1,6}")) {
					isZipCodeValid = false;
					final String errorMsg = "Incorrect zipcode.";
					errorMessageMap.put(ZIP_CODE_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.error(errorMsg);
				}
				isZipCodeValid = true;
				address.setZipCode((String) value);
				errorMessageMap.remove(ZIP_CODE_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		createLabel(addressGroup, "City:");
		final Text city = createText(addressGroup);
		initDataBindingOn(city, "city", Address.class, address, (value) -> {
			if (value instanceof String) {
				final String userInput = (String) value;
				if (userInput.isEmpty()) {
					isCityValid = false;
					final String errorMsg = "City field cannot be empty.";
					errorMessageMap.put(CITY_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				} else if (!userInput.matches(HUNGARIAN_CHARSET_REGEX)) {
					isCityValid = false;
					final String errorMsg = "Incorrect city name.";
					errorMessageMap.put(CITY_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.error(errorMsg);
				}
				isCityValid = true;
				address.setCity((String) value);
				errorMessageMap.remove(CITY_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		createLabel(addressGroup, "Street:");
		final Text street = createText(addressGroup);
		initDataBindingOn(street, "street", Address.class, address, (value) -> {
			if (value instanceof String) {
				final String userInput = (String) value;
				if (userInput.isEmpty()) {
					isStreetValid = false;
					final String errorMsg = "Street cannot be empty.";
					errorMessageMap.put(STREET_ERROR_KEY, errorMsg);
					validatePage();
					return ValidationStatus.warning(errorMsg);
				}
				isStreetValid = true;
				address.setStreet((String) value);
				errorMessageMap.remove(STREET_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});
		
		final Group phoneNumberGroup = new Group(composite, SWT.TOP);
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = SWT.TOP;
		phoneNumberGroup.setLayout(gridLayout);
		phoneNumberGroup.setText("Phone number");
		final GridData gridData2 = GridDataFactory.fillDefaults().create();
		phoneNumberGroup.setLayoutData(gridData2);
		
		createLabel(phoneNumberGroup, "Phone:");
		final Text phoneNumberTextField = createText(phoneNumberGroup);
		phoneNumberTextField.setText("");
		phoneNumberTextField.setToolTipText("Expected format: 1-234-567-8910");
		initDataBindingOnPhoneNumber(phoneNumberTextField, "phoneNumbers", (value) -> {
			if (value instanceof String) {
				final String userInput = (String) value;
				if (userInput.isEmpty()) {
					isPhoneNumberValid = false;
					final String message = "Phone number cannot be empty.";
					errorMessageMap.put(PHONE_NUMBER_ERROR_KEY, message);
					validatePage();
					return ValidationStatus.warning(message);
				} 
				if (!userInput.matches(PHONE_NUMBER_REGEX)) {
					isPhoneNumberValid = false;
					final String message = "Phone number must be in the given format: 1-123-456-7891.";
					errorMessageMap.put(PHONE_NUMBER_ERROR_KEY, message);
					validatePage();
					return ValidationStatus.error(message);
				}
				isPhoneNumberValid = true;
				phoneNumber = userInput;
				errorMessageMap.remove(PHONE_NUMBER_ERROR_KEY);
				validatePage();
				return ValidationStatus.ok();
			}
			return ValidationStatus.error("Incorrect input found.");
		});

		setErrorMessage(null);
		setControl(composite);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initDataBindingOnPhoneNumber(Text textField, String fieldName, Function<Object, IStatus> validator) {
		final IObservableValue targetObservable = WidgetProperties.text(SWT.Modify).observe(textField);
		final IObservableValue modelObservable = BeanProperties.value(fieldName).observe(contact.getPhoneNumbers());

		final IConverter convertListToString = IConverter.create(List.class, String.class, (o1) -> ((String) o1).split(" "));
		final UpdateValueStrategy strategy = new UpdateValueStrategy();
		UpdateValueStrategy.create(convertListToString);

		strategy.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				return validator.apply(value);
			}

		});
		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, strategy, null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT);
		
	}

	@Override
	protected void validatePage() {
		if (isCountryValid && isZipCodeValid && isCityValid && isStreetValid && isPhoneNumberValid) {
			setPageComplete(true);
			setErrorMessage(null);
		} else {
			if(!isPhoneNumberValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(PHONE_NUMBER_ERROR_KEY));
			}
			if(!isStreetValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(STREET_ERROR_KEY));
			}
			if(!isCityValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(CITY_ERROR_KEY));
			}
			if(!isZipCodeValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(ZIP_CODE_ERROR_KEY));
			}
			if(!isCountryValid) {
				setPageComplete(false);
				setErrorMessage(errorMessageMap.get(COUNTRY_ERROR_KEY));
			}
		}
	}

	public void updatePhoneAndAddress() {
		contact.setAddresses(Lists.newArrayList(address));
		contact.setPhoneNumbers(Lists.newArrayList(phoneNumber));
	}

}
