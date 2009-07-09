package de.mn.felixembedsample.hostapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;



import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.ServiceTracker;

import de.mn.felixembedsample.hostapp.services.consumed.command.Command;

public class HostApplication
{
    private HostActivator m_hostActivator = null;
    private Felix m_felix = null;
    // Überwacht, ob neue Services (deren Service Interfaces dieser HostApp bekannt sind)
    // durch Bundles implementiert werden, welche wiederum in der eingebetteen Felix Instanz isntalliert sind und laufen
    private ServiceTracker m_servicetracker = null;

    
    // für den angeboteen lookupservice
    private Map<String,String> m_lookupMap = new HashMap<String,String>();
    
    
    
    
    

    /**
     * WENN diese HostApplication, Services den im eingebetteten Felix laufenden Bundles ANBIETEN
     * will, muss sie:
     * 1. die/das Service Interface (klassendefinition des services) kennen (buildpath oder lokal)
     * 2. Das Verwendende Bundle muss dieses selbe Service Interface kennen und nutzen
     * 3. Die HostAppl. kann dieses Interface NICHT vom Bundle importieren (deshalb siehe 1.)
     * 4. Diew HostAppl. muss das Package mit dem ServiceInterface drinne über das System-Bundle, 
     * (=Framework Bundle), exportieren ->  Bundles die im engebett. Framework installiert sind, können das dann nutzen
     * 5. Nummer 4. wird durch Nutzung von org.osgi.framework.system.packages.extra in der Start-Config (config.properties) 
     * des FrameWorks erreicht
     * 
     */
    public HostApplication()
    {
        // Create a configuration property map.(nutzt sonst die Standardsettings)
    	// diese funktion sucht erstmal nach der config file.
    	// man kann  Main.CONFIG_PROPERTIES_PROP  (= "felix.config.properties") als schlüssel in den system properties setzen
    	// die dort angegebene (zu setzende) location der config.propterties datei wird dann verwendet
    	// ist da nix, versucht er das Verz. der felix.jar als root zu verwenden
    	System.setProperty("felix.config.properties", "file:config/config.properties");
        Properties configProps = Main.loadConfigProperties();
        if (configProps == null)
        {
            System.err.println("No config file found.");
            configProps = new Properties();
        }
   	
    	// Init fuellung für den von der hostapp angebotenen lookupservice
        // Initialize the map for the property lookup service.
        m_lookupMap.put("name1", "value1");
        m_lookupMap.put("name2", "value2");
        m_lookupMap.put("name3", "value3");
        m_lookupMap.put("name4", "value4");        
        
        // Create host activator; => nun mit Lookupmap die oben initialisiert wurde
        m_hostActivator = new HostActivator(m_lookupMap);
        List<BundleActivator> list = new ArrayList<BundleActivator>();
        list.add(m_hostActivator);
        // der hostActivator dieser HostApplication wird dann (als einziger) zu der Liste
        // der Activators hinzugefügt, und dann beim start des (eingebetteten) Felix 
        // mitgestartet
        // über diesen mitgestarteten Activator kann die HostApplication dann kontakt
        // zum framework aufbauen. -> denn dieser acivator wird ja gestartet wenn framework gestartet wird
        // und gestoppt wenn Framework gestoppt wird
        
        
        // create AutoActivaor from Felix MAIN Package.
        // this takes the felix.auto.start.# Property in config.properties
        // and starts the bundles.
        // for doing so on startup, this AutoActivator is added to the listet of activatores
        // which are startet with the system bundle
        // Its Constructor takes the configProps, when BundleActivators start() is executed, it processes the configProps
        list.add(new AutoActivator(configProps));

        // Add List of Autostart Activators to configProps (can't be done in config.propterties file so easy)
        configProps.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);

        try
        {
            // Now create an instance of the framework with our configuration properties.
            m_felix = new Felix(configProps);
            // Now start Felix instance.
            m_felix.start();
        }
        catch (Exception ex)
        {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
        }
        
        // init servicetracker
        m_servicetracker = new ServiceTracker
        		(
        				m_hostActivator.getContext(), 
        				Command.class.getName(), 
        				null
        		);
        
        m_servicetracker.open();        
        
    }
    
    
    
    

 


    public Bundle[] getInstalledBundles()
    {
        // Use the system bundle activator to gain external
        // access to the set of installed bundles.
        return m_hostActivator.getBundles();
    }

    public void shutdownApplication()
    {
        // Shut down the felix framework when stopping the
        // host application.
        try {
			m_felix.stop();
		} catch (BundleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cannot stop HostApplication");
		}
        
		// timeout time? in msec?
        long waittime = 10000;
        
        try {
			m_felix.waitForStop(waittime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Thread has waited and was then interrupted");
			e.printStackTrace();
		}
    }
    
    
    /**
     * Beispiel für die Nutzung eines von einem Bundle zur Verfügung gestellten Services
     * The above example is overly simplistic with respect to concurrency issues and error
     * conditions, but it demonstrates the overall approach for using bundle-provided 
     * services from the host application.
     * @param name
     * @param commandline
     * @return
     */
    public boolean execute(String name, String commandline)
    {
        // See if any of the currently tracked command services
        // match the specified command name, if so then execute it.
    	// holt liste installierter Bundles, die der ServiceTracker kennt
        Object[] services = m_servicetracker.getServices();
        
        
        // besser mit for each
        for (Object service : services) {
        	
            try
            {
                if (((Command) service).getName().equals(name))
                {
                    return ((Command) service).execute(commandline);
                }
            }
            catch (Exception ex)
            {
                // Since the services returned by the tracker could become
                // invalid at any moment, we will catch all exceptions, log
                // a message, and then ignore faulty services.
                System.err.println(ex);
            }
        	
        } 
        
        //else
        return false;
    }
    
    
}
