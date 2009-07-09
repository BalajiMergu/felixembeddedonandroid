package de.mn.felixembedsample.hostapp.services.consumed.command;

/** 
 * Service Interface -> Implementation will be provided by an bundle installed in the embedded framework
 * this interface has to be known to host and to implementing bundle
 * @author matthiasneubert
 *
 */
public interface Command {

	public String getName();
    public String getDescription();
    public boolean execute(String commandline);

    
}
