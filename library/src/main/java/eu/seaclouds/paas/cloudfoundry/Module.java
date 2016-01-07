package eu.seaclouds.paas.cloudfoundry;

import java.net.MalformedURLException;
import java.net.URL;
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
		try
		{
			return new URL(app.getUris().get(0)).toString();
		}
		catch (MalformedURLException ex)
		{
			return "http://" + app.getUris().get(0);
		}
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
		return app.getInstances(); //.getRunningInstances();
	}
	

}
