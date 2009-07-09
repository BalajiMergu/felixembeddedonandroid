package de.mn.felixembedsample.hostapp;

import org.osgi.framework.Bundle;

public class HostStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HostApplication ha = new HostApplication();
		Bundle[] list = ha.getInstalledBundles();
		
		for (Bundle b : list) {
		
			System.out.println("TESTBUNDLE: "+b.getSymbolicName());
		}
		
	}

}
