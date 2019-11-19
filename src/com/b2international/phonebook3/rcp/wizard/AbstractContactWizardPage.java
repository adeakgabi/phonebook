package com.b2international.phonebook3.rcp.wizard;

import java.util.function.Function;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.b2international.phonebook3.rcp.editor.EditorContact;

public abstract class AbstractContactWizardPage<T> extends WizardPage {
	
	protected static final String HUNGARIAN_CHARSET_REGEX = "[a-zA-zÁÉŐÚŰÓÜÖÍáéűúőóüöí]+([ '-][a-zA-Z]+)*";
	protected static final String PHONE_NUMBER_REGEX = "\\d{1}[-]?\\d{3}[-]?\\d{3}[-]?\\d{4}$";
	
	protected final EditorContact editorContact;
	protected final DataBindingContext bindingContext;

	protected AbstractContactWizardPage(EditorContact editorContact, String pageName) {
		super(pageName);
		this.editorContact = editorContact;
		bindingContext = new DataBindingContext();
		setTitle(pageName);
		setPageComplete(false);
		setDescription("Create a new contact.");
	}

	public void createControl(Composite parent) {};
	
	protected void createLabel(Composite composite, String label) {
		final Label fieldLabel = new Label(composite, SWT.NONE);
		fieldLabel.setText(label);
	}
	
	protected Text createText(Composite composite) {
		final Text text = new Text(composite, SWT.TOP);
        final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        return text;
	}
	
	protected void validatePage() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initDataBindingOn(Text textField, String fieldName, Class<T> modelClass, T objectModel,  Function<Object, IStatus> validator) {
		final IObservableValue targetObservable = WidgetProperties.text(SWT.Modify).observe(textField);
		final IObservableValue modelObservable = PojoProperties.value(modelClass, fieldName).observe(objectModel);
		
		final UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				return validator.apply(value);
			}
		});
		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, strategy, null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT);
	}
	
}
