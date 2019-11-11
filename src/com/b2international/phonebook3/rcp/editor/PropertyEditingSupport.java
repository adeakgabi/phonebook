package com.b2international.phonebook3.rcp.editor;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public class PropertyEditingSupport<T> extends EditingSupport {
	
    private final TableViewer viewer;
	private final BiConsumer<T, Object> onUpdate;
	private Function<T, Object> initialValue;

	public PropertyEditingSupport(TableViewer viewer, 
			final Function<T, Object> initialValue, 
			final BiConsumer<T, Object> onUpdate) {
		super(viewer);
		this.viewer = viewer;
		this.initialValue = initialValue;
		this.onUpdate = onUpdate;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object userInputValue) {
		onUpdate.accept((T) element, userInputValue);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(Object element) {
		return initialValue.apply((T) element);
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}
	
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

}
