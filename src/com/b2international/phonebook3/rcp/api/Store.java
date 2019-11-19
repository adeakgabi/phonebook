package com.b2international.phonebook3.rcp.api;

import java.util.ArrayList;
import java.util.List;

import com.b2international.phonebook3.rcp.redux.Action;
import com.b2international.phonebook3.rcp.redux.Reducer;
import com.b2international.phonebook3.rcp.redux.StateChangeListener;

public class Store<S> {
	
	private final Reducer<S> reducer;
	private S state;
	private final List<StateChangeListener<S>> listeners = new ArrayList<>();
	
	public Store(S initialState, Reducer<S> reducer) {
		this.reducer = reducer;
		this.state = initialState;
	}
	
	public void dispatch(Action action) {
		S oldState = state;
		S newState = reducer.reduce(oldState, action);
		if (oldState != newState) {
			this.state = newState;
			notifySubscribers(oldState, newState);
		}
	}

	public S getState() {
		return state;
	}
	
	public void addStateChangeListener(StateChangeListener<S> listener) {
		listeners.add(listener);	
	}
	
	public void removeStateChangeListener(StateChangeListener<S> listener) {
		listeners.remove(listener);	
	}
	
	public void notifySubscribers(S oldState, S newState) {
		System.out.println(state.toString());
		listeners.forEach(listener -> {
			listener.onStateChange(oldState, newState);
		});
	}
	
//	public void saveContact(Contact contact) {
//		contactsMap.put(contact.getId(), contact);
//		updateContactTree();
//	}
	
}
