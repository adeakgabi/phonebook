package com.b2international.phonebook3.rcp.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
	
	private String country;
	private String zipCode;
	private String city;
	private String street;

	@JsonCreator
	public Address(
			@JsonProperty("country") String country,
			@JsonProperty("zipCode") String zipCode,
			@JsonProperty("city") String city,
			@JsonProperty("street") String street) {
		this.country = country;
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public boolean hasMatch(String word) {
		if(country.equalsIgnoreCase(word)) {
			return true;
		}
		
		if(zipCode.equalsIgnoreCase(word)) {
			return true;
		}
		
		if(city.equalsIgnoreCase(word)) {
			return true;
		}
		
		if(street.equalsIgnoreCase(word)) {
			return true;
		}
		return false;
	}
	
	

	@Override
	public int hashCode() {
		return Objects.hash(city, country, street, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city)
				&& Objects.equals(country, other.country)
				&& Objects.equals(zipCode, other.zipCode)
				&& Objects.equals(street, other.street);
	}

	@Override
	public String toString() {
		return city +", " + country;
	}
	
}
