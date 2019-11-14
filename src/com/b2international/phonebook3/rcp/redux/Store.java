package com.b2international.phonebook3.rcp.redux;

import java.util.Observable;

public class Store<S> extends Observable {
	
	private Reducer<S> reducer;
	private S initialState;

	public Store(S initialState, Reducer<S> reducer) {
		this.reducer = reducer;
		this.initialState = initialState;
	}

	public static <S> Store<S> createStore(S state, Reducer<S> reducer) {
		return new Store<>(state, reducer);
	}
	
	public void dispatch(Action action) {
		initialState = reducer.reduce(initialState, action);
		notifySubscribers();
	}
	
	public void notifySubscribers() {
		System.out.println(initialState.toString());
	}

}
