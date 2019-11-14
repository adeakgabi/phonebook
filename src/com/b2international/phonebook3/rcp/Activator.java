package com.b2international.phonebook3.rcp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.b2international.phonebook3.rcp.util.ContactJsonHandler;

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
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		appHome = Paths.get(System.getProperty("app.home", System.getProperty("user.home") + "/.gabicontacts/"));
		ContactJsonHandler.readContactsFromFile(appHome.resolve(DB_FILE));
		plugin = this;

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ContactJsonHandler.writeContactsToFile(appHome.resolve(DB_FILE));
		plugin = null;
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

}
