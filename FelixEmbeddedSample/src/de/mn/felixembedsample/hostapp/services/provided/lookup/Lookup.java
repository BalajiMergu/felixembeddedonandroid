package de.mn.felixembedsample.hostapp.services.provided.lookup;


/**
 * Das Service Interface, das von der HostApp angeboten wird, um von den installierten Bundles genutzt zu werden
 * @author matthiasneubert
 *
 */
public interface Lookup {

	public Object lookup(String name);

	
}
