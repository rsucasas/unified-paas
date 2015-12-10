package eu.seaclouds.paas.cloudfoundry;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasClientFactory;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.PaasSession.StartStopCommand;


/**
 * 
 * @author 
 *
 */
public class CloudFoundryTest
{

	
	private static final String PIVOTAL = "https://api.run.pivotal.io";
	private static final String BLUEMIX = "https://api.eu-gb.bluemix.net";
	private static final String APPFOG = "";
	private static final String PRIVATE_CF = "https://api.95.211.172.243.xip.io";
	
	private static final String APP_NAME = "unified-paas-cloudfoundry-test";
	
	private PaasSession session;
    
    private String username;
    private String password;
    private String api;
    private String org;
    private String space;
    private boolean trustSelfSignedCerts = true;
    

    
    @BeforeTest
    public void initialize()
    {
        username = System.getenv("cf_user");
        password = System.getenv("cf_password");
        api = PIVOTAL;
        org = "ATOS-ModaClouds";
        space = "development";
    	
        // login / connect to PaaS
        PaasClientFactory factory = new PaasClientFactory();
        PaasClient client = factory.getClient("cloudfoundry");
        session = client.getSession(
        		new Credentials.ApiUserPasswordOrgSpaceCredentials(api, username, password, org, space, trustSelfSignedCerts));
    }
    

    @Test
    public void deploy() {
    	System.out.println("### TEST .... deploy()");

        String path = this.getClass().getResource("/SampleApp1.war").getFile();
        eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));

        assertNotNull(m);
        System.out.println(String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
    }
    
    
    @Test
    public void stop() {
    	System.out.println("### TEST .... stop()");

        eu.seaclouds.paas.Module m = session.getModule(APP_NAME);

        session.startStop(m, StartStopCommand.STOP);
    }
    
    
    @Test
    public void start() {
    	System.out.println("### TEST .... start()");

        eu.seaclouds.paas.Module m = session.getModule(APP_NAME);

        session.startStop(m, StartStopCommand.START);
    }

    
    @Test
    public void undeploy() {
    	System.out.println("### TEST .... undeploy()");

        session.undeploy(APP_NAME);
    }

    
}