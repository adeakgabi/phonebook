package com.b2international.phonebook3.rcp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.b2international.phonebook3.rcp.api.Store;
import com.b2international.phonebook3.rcp.model.Contact;
import com.b2international.phonebook3.rcp.redux.State;
import com.b2international.phonebook3.rcp.redux.StateReducer;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.common.collect.Maps;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	
	private static final String DB_FILE = "data.json";

	// The plug-in ID
	public static final String PLUGIN_ID = "com.b2international.phonebook3.rcp"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Path appHome;
	private ObjectMapper mapper;
	
	private static Store<State> store;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		mapper = new ObjectMapper();
		mapper.setDateFormat(new StdDateFormat());
		mapper.registerModule(new JSR310Module());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		appHome = Paths.get(System.getProperty("app.home", System.getProperty("user.home") + "/.gabicontacts/"));
		List<Contact> contacts = readContactsFromFile(appHome.resolve(DB_FILE));
		
		plugin = this;
		
		StateReducer reducer = new StateReducer();
		Set<FormEditor> editorState = new HashSet<>();
		State state = new State(Maps.uniqueIndex(contacts, Contact::getId), editorState);
		store = new Store<>(state, reducer);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		writeContactsToFile(appHome.resolve(DB_FILE), getStore().getState().getContactsMap().values());
		plugin = null;
		mapper = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static ImageDescriptor getImageDescriptor(String iconPath) {
		ImageRegistry imageRegistry = getDefault().getImageRegistry();
		if (imageRegistry.getDescriptor(iconPath) == null) {
			Optional<ImageDescriptor> descriptor = ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, iconPath);
			if (descriptor.isPresent()) {
				imageRegistry.put(iconPath, descriptor.get());
			}
		}
		return imageRegistry.getDescriptor(iconPath);
	}

	public static Image getImage(String iconPath) {
		ImageRegistry imageRegistry = getDefault().getImageRegistry();
		if (imageRegistry.get(iconPath) == null) {
			// this will load the image and its descriptor
			getImageDescriptor(iconPath);
		}
		return imageRegistry.get(iconPath);
	}
	
	public static Store<State> getStore() {
		return store;
	}
	
	public List<Contact> readContactsFromFile(final Path dbFile) throws IOException {
		final File contactsFile = dbFile.toFile();
		if (contactsFile.exists()) {
			return Arrays.asList(mapper.readValue(contactsFile, Contact[].class));
		} else {
			contactsFile.getParentFile().mkdirs();
			contactsFile.createNewFile();
			return new ArrayList<Contact>();
		}
	}
	
	public void writeContactsToFile(final Path dbFile, final Iterable<Contact> contacts) throws IOException {
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.forType(Contact.class);
		writer.writeValue(dbFile.toFile(), contacts);
	}

}
