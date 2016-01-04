package eu.seaclouds.paas;

import eu.seaclouds.paas.PaasSession.ScaleCommand;
import eu.seaclouds.paas.PaasSession.ScaleUpDownCommand;
import eu.seaclouds.paas.PaasSession.StartStopCommand;
import eu.seaclouds.paas.heroku.DeployParameters;


/**
 * 
 * @author
 *
 */
public class Tests
{
	
	
	private static final String PIVOTAL = "https://api.run.pivotal.io";
	private static final String BLUEMIX = "https://api.eu-gb.bluemix.net";
	private static final String APPFOG = "";
	private static final String PRIVATE_CF = "https://api.95.211.172.243.xip.io";
	
	private static final String APP_NAME = "unified-paas-cloudfoundry-test";
	
	
	public static void main(String[] args)
	{
		try
		{
			//
			String heroku_apiKey = "";
		    String heroku_username = "";
		    String heroku_password = "";
		    
		    String cf_username = "";
		    String cf_password = "";
		    String cf_api = PIVOTAL;
		    String cf_org = "";
		    String cf_space = "development";
		    boolean cf_trustSelfSignedCerts = true;
		    
		    //
		    PaasClientFactory factory = new PaasClientFactory();
	        PaasClient client = factory.getClient("cloudfoundry"); // cloudfoundry   heroku
	        PaasSession session = null;
	        String path = Tests.class.getResource("/SampleApp1.war").getFile();
	        
		    // HEROKU
	        /*
	        session = client.getSession(new Credentials.ApiKeyCredentials(heroku_apiKey));
		    //eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));
		    eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
		    
		    System.out.println("### >> running instances: " + m.getRunningInstances());
		    
		    session.startStop(m, StartStopCommand.START);
		    m = session.getModule(APP_NAME);

	        System.out.println("### >> " + String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
	        System.out.println("### >> running instances: " + m.getRunningInstances());
	        */
	        
	        // CLOUD FOUNDRY
	        session = client.getSession(
	        		new Credentials.ApiUserPasswordOrgSpaceCredentials(cf_api, cf_username, cf_password, cf_org, cf_space, cf_trustSelfSignedCerts));
	       
	        /*
	        eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path)); 
	        System.out.println("### >> running instances: " + m.getRunningInstances());
	        
	        session.startStop(m, StartStopCommand.STOP);
	        Thread.sleep(5000);
	        m = session.getModule(APP_NAME);
	        System.out.println("### >> running instances: " + m.getRunningInstances());
	        
	        session.startStop(m, StartStopCommand.START);
	        Thread.sleep(5000);
	        m = session.getModule(APP_NAME);
	        System.out.println("### >> running instances: " + m.getRunningInstances());
	        
	        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
	        Thread.sleep(5000);
	        m = session.getModule(APP_NAME);
	        System.out.println("### >> running instances: " + m.getRunningInstances());
	        
	        session.undeploy(APP_NAME);
	        */
	        
	        eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
	        //session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
	        //session.scale(m, ScaleCommand.SCALE_INSTANCES, 0);
	        session.undeploy(APP_NAME);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

}
