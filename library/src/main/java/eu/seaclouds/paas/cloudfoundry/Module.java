package eu.seaclouds.paas.cloudfoundry;

import org.cloudfoundry.client.lib.domain.CloudApplication;


/**
 * Cloud Foundry module
 * @author 
 *
 */
public class Module implements eu.seaclouds.paas.Module {

	
	private CloudApplication app;
	
	
	public Module(CloudApplication app) {
        this.app = app;
    }
	
	
	@Override
	public String getName()
	{
		return app.getName();
	}

	
	@Override
	public String getUrl()
	{
		// TODO get url
		return app.getUris().get(0);
	}
	

	@Override
	public String getAppType()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getRunningInstances()
	{
		return app.getRunningInstances();
	}
	

}
