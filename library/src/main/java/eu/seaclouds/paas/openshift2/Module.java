package eu.seaclouds.paas.openshift2;

import java.util.List;
import java.util.Map;
import com.openshift.client.IApplication;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:05:37
 */
public class Module implements eu.seaclouds.paas.Module
{

	
	private IApplication app;
	
	
	/**
	 * 
	 * Constructor
	 * @param app
	 */
	public Module(IApplication appl)
	{
		this.app = appl;
	}
	
	
	@Override
	public String getName()
	{
		return this.app.getName();
	}

	
	@Override
	public String getUrl()
	{
		return this.app.getApplicationUrl();
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
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public List<String> getServices()
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Map<String, Object> getmEnv()
	{
		// TODO Auto-generated method stub
		return null;
	}
	

}
