package com.b2international.phonebook3.rcp.redux;

public interface Reducer<S> {

	S reduce(S state, Action action);
}
