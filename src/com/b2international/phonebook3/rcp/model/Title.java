package com.b2international.phonebook3.rcp.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Title {
	MR("Mr"),
	MRS("Mrs"),
	MS("Ms"),
	DEFAULT("");
	
	@JsonValue
	private final String displayTitle;
	
	Title(String displayTitle){
		this.displayTitle = displayTitle;
	}
	
	public String getDisplayTitle() {
		return displayTitle;
	}
	
	public static Title getValueFrom(String title) {
		final Title[] titles = values();
		for (Title candidate : titles) {
			if (candidate.displayTitle.equals(title)) {
				return candidate;
			}
		}
		return DEFAULT;
	}

}
