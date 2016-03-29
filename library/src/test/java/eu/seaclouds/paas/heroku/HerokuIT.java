package eu.seaclouds.paas.heroku;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.heroku.api.exception.RequestFailedException;
import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasClientFactory;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.ServiceApp;
import eu.seaclouds.paas.TestConfigProperties;
import eu.seaclouds.paas.PaasSession.ScaleUpDownCommand;
import eu.seaclouds.paas.PaasSession.StartStopCommand;


/**
 * 
 *
 * @author ATOS
 * @date 8/1/2016-15:50:20
 */
public class HerokuIT
{
	
	
	// Application
	private static final String APP_NAME = TestConfigProperties.getInstance().getApp_name();

	// session
    private PaasSession session;
    
    // log
 	private static Logger logger = LoggerFactory.getLogger(HerokuIT.class);

	
	@BeforeTest
    public void initialize()
    {
		logger.info("### INTEGRATION TESTS > Heroku ...");
		// login / connect to PaaS
        PaasClientFactory factory = new PaasClientFactory();
        PaasClient client = factory.getClient("heroku");
        session = client.getSession(new Credentials.ApiKeyCredentials(TestConfigProperties.getInstance().getHeroku_apiKey()));
    }
    

	/**
	 * 
	 * @param m
	 * @param exeFunc
	 * @param operation
	 * @param expectedValue
	 * @param seconds
	 * @return
	 */
	private boolean checkResult(eu.seaclouds.paas.Module m, String exeFunc, String operation, int expectedValue, int seconds)
	{
		for (int i = 0; i < 10; i++)
		{
			try
			{
				logger.info(">> " + exeFunc + " >> " + operation + " == " + expectedValue + " ?");
				Thread.sleep(seconds*1000);
				m = session.getModule(APP_NAME);

				if (("instances".equalsIgnoreCase(operation)) && (m.getRunningInstances() == expectedValue))
				{
					logger.info(">> " + operation + " = " + expectedValue);
					return true;
				}
			}
			catch (Exception e)
			{
				fail(e.getMessage());
				break;
			}
		}

		return false;
	}
	

    @Test
    public void deploy() 
    {
    	logger.info("### TEST > Heroku > deploy()");

    	String path = this.getClass().getResource("/SampleApp1.war").getFile();
        eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));

        assertNotNull(m);
        logger.info(">> " + String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
        
        if (!checkResult(m, "deploying / starting application", "instances", 1, 10))
        	fail(APP_NAME + " not started");
        else
        	assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"deploy"})
    public void stop() 
    {
    	logger.info("### TEST > Heroku > stop()");

    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.STOP);
        
        if (!checkResult(m, "stopping application", "instances", 0, 2))
        	fail(APP_NAME + " not stopped");
        else
        	assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"stop"})
    public void start() 
    {
    	logger.info("### TEST > Heroku > start()");

    	 eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
         session.startStop(m, StartStopCommand.START);
         
         if (!checkResult(m, "starting application", "instances", 1, 5))
         	fail(APP_NAME + " not started");
         else
         	assertTrue(true);
    }
    
   
    @Test (dependsOnMethods={"start"})
    public void scaleUp() 
    {
    	logger.info("### TEST > Heroku > scaleUp()");

    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 2, 5))
        	fail(APP_NAME + " not scaled up");
        else
        	assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"scaleUp"})
    public void scaleDown() 
    {
    	logger.info("### TEST > Heroku > scaleDown()");

    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_DOWN_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 1, 5))
        	fail(APP_NAME + " not scaled down");
        else
        	assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"scaleDown"}) 
    public void bindToService() 
    {
    	logger.info("### TEST > Heroku > bindToService()");
    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
    	ServiceApp service = new ServiceApp("cleardb:ignite");
    	
        session.bindToService(m, service);
        
        m = session.getModule(APP_NAME);
        assertEquals(1, m.getServices().size());
        
        logger.info("### TEST > Heroku > bindToService() > environment values...");
        for (Map.Entry<String, Object> entry : m.getmEnv().entrySet()) {
            logger.info("### TEST > Heroku > bindToService() > " + entry.getKey() + " / " + entry.getValue().toString());
        }
    }

    
    @Test (dependsOnMethods={"bindToService"})
    public void unbindFromService() 
    {
    	logger.info("### TEST > Heroku > unbindFromService()");
    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
    	ServiceApp service = new ServiceApp("cleardb:ignite");
    	
        session.unbindFromService(m, service);
        
        m = session.getModule(APP_NAME);
        assertEquals(0, m.getServices().size());
    }
    
    
    @Test (dependsOnMethods={"unbindFromService"})
    public void undeploy() 
    {
    	logger.info("### TEST > Heroku > undeploy()");

    	session.undeploy(APP_NAME);
        
        try {
        	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
        	System.out.println("### TEST > Heroku > undeploy() > " + m.getName());
        	fail(APP_NAME + " still exists");
        }
        catch (RequestFailedException | PaasException ex)
        {
        	assertTrue(true);
        }
    }

	
}
