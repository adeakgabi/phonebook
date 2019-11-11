package com.b2international.phonebook3.rcp.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.b2international.phonebook3.rcp.model.Contact;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ContactSerializer extends JsonSerializer<Contact> {

	@Override
	public void serialize(Contact contact, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {

        jsonGenerator.writeStartObject();
       	jsonGenerator.writeStringField("title", contact.getTitle().getDisplayTitle());
        jsonGenerator.writeStringField("firstName", contact.getFirstName());
        jsonGenerator.writeStringField("lastName", contact.getLastName());
        jsonGenerator.writeStringField("dateOfBirth", formatLocalDateToString(contact.getDateOfBirth()));
    	jsonGenerator.writeFieldName("phoneNumber");
        	jsonGenerator.writeStartArray();
        		for(int i = 0; i < contact.getPhoneNumbers().size(); i++) {
        			jsonGenerator.writeString(contact.getPhoneNumbers().get(i));
        		}
        	jsonGenerator.writeEndArray();
        	
        	jsonGenerator.writeFieldName("address");
        	jsonGenerator.writeStartArray();
        	for(int i = 0; i < contact.getAddresses().size(); i++) {
    			jsonGenerator.writeStartObject();
    			jsonGenerator.writeStringField("country", contact.getAddresses().get(i).getCountry());
    			jsonGenerator.writeStringField("zipCode", contact.getAddresses().get(i).getZipCode());
    			jsonGenerator.writeStringField("city", contact.getAddresses().get(i).getCity());
    			jsonGenerator.writeStringField("street", contact.getAddresses().get(i).getStreet());
    			jsonGenerator.writeEndObject();
    		}
        	jsonGenerator.writeEndArray();
        	
        jsonGenerator.writeEndObject();
	}
	
	private String formatLocalDateToString(LocalDate localDate) {
		return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

}
