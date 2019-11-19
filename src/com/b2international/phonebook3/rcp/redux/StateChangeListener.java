package com.b2international.phonebook3.rcp.redux;

public interface StateChangeListener<S> {

	void onStateChange(S oldState, S newState);
	
}
