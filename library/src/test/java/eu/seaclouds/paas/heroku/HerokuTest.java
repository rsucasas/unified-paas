package eu.seaclouds.paas.heroku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasClientFactory;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.TestConfigProperties;
import eu.seaclouds.paas.PaasSession.StartStopCommand;


/**
 * 
 *
 * @author ATOS
 * @date 23/2/2016-16:11:39
 */
public class HerokuTest 
{
	
	
	// Application
    private static final String APP_NAME = TestConfigProperties.getInstance().getApp_name();
    // session
    private PaasSession session;
    // log
 	private static Logger logger = LoggerFactory.getLogger(HerokuTest.class);
    

    @BeforeTest
    public void initialize()
    {
    	logger.info("### UNIT TESTS > Heroku ...");
        // login / connect to PaaS
        PaasClientFactory factory = new PaasClientFactory();
        PaasClient client = factory.getClient("heroku");
        session = client.getSession(new Credentials.ApiKeyCredentials(TestConfigProperties.getInstance().getHeroku_apiKey()));
    }
    
    
    @Test
    public void deploy() {
    	logger.info("### TEST > HerokuTest > deploy()");

        String path = this.getClass().getResource("/SampleApp1.war").getFile();
        eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));

        assertNotNull(m);
        System.out.println("### >> " + String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
        assertEquals("https://" + APP_NAME + ".herokuapp.com/", m.getUrl());
    }
    
    
    @Test (dependsOnMethods={"deploy"})
    public void stop() {
    	logger.info("### TEST > HerokuTest > stop()");
    	
        eu.seaclouds.paas.Module m = session.getModule(APP_NAME);

        session.startStop(m, StartStopCommand.STOP);
        
        m = session.getModule(APP_NAME);
        System.out.println("### >> running instances: " + m.getRunningInstances());
        assertEquals(0, m.getRunningInstances());
    }
    
    
    @Test (dependsOnMethods={"stop"})
    public void start() {
    	logger.info("### TEST > HerokuTest > start()");

        eu.seaclouds.paas.Module m = session.getModule(APP_NAME);

        session.startStop(m, StartStopCommand.START);
        
        m = session.getModule(APP_NAME);
        System.out.println("### >> running instances: " + m.getRunningInstances());
        assertEquals(1, m.getRunningInstances());
    }
    
    
    @Test (dependsOnMethods={"start"})
    public void undeploy() {
    	logger.info("### TEST > HerokuTest > undeploy()");

        session.undeploy(APP_NAME);
    }
    
    
    /*
    @Test
    public void bindToService() {
    	logger.info("### TEST > HerokuTest > bindToService()");

    	eu.seaclouds.paas.Module m = session.getModule(APP_NAME);
    	ServiceApp service = new ServiceApp("cleardb:ignite");
    	
        session.bindToService(m, service);
    }
    */

    
}
