package com.b2international.phonebook3.rcp.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.b2international.phonebook3.rcp.api.ContactService;
import com.b2international.phonebook3.rcp.model.Contact;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@SuppressWarnings("deprecation")
public final class ContactJsonHandler {
	
	private static final ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
		mapper.setDateFormat(new StdDateFormat());
		mapper.registerModule(new JSR310Module());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	public static List<Contact> readContactsFromFile(final Path dbFile) throws IOException {
		final ContactService contactService = ContactService.getInstance();
		final File contactsFile = dbFile.toFile();
		if (contactsFile.exists()) {
			Contact[] contacts = mapper.readValue(contactsFile, Contact[].class);
			contactService.bulkCreate(Arrays.asList(contacts));
		} else {
			contactsFile.getParentFile().mkdirs();
			contactsFile.createNewFile();
		}
		return contactService.getFlatContacts();
	}
	
	public static void writeContactsToFile(final Path dbFile) throws IOException {
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		Collection<Contact> contactsToWriteInFile = ContactService.getInstance().getContacts().values();
		writer.forType(Contact.class);
		writer.writeValue(dbFile.toFile(), contactsToWriteInFile);
	}

}
