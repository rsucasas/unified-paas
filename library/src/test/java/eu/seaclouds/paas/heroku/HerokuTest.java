package eu.seaclouds.paas.heroku;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasClientFactory;
import eu.seaclouds.paas.PaasSession;

public class HerokuTest {
    private static final String APP_NAME = "unified-paas-heroku-test";
    private PaasClientFactory factory;
    private String apiKey;
    
    public HerokuTest() {
        
        factory = new PaasClientFactory();
        apiKey = System.getenv("API_KEY");
    }
    
    @Test
    public void deploy() {
        PaasClient client = factory.getClient("heroku");
        PaasSession session = client.getSession(new Credentials.ApiKeyCredentials(apiKey));
        
        String path = this.getClass().getResource("/SampleApp1.war").getFile();
        eu.seaclouds.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));

        assertNotNull(m);
        System.out.println(String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
        assertEquals("https://" + APP_NAME + ".herokuapp.com/", m.getUrl());
        
    }
    
    @Test
    public void undeploy() {
        PaasClient client = factory.getClient("heroku");
        PaasSession session = client.getSession(new Credentials.ApiKeyCredentials(apiKey));

        session.undeploy(APP_NAME);
    }
}
