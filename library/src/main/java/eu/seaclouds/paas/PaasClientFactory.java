package eu.seaclouds.paas;

import eu.seaclouds.paas.cloudfoundry.CloudFoundryClient;
import eu.seaclouds.paas.heroku.HerokuClient;


public class PaasClientFactory {

    public PaasClient getClient(String provider) {
        switch (provider) {
	        case "heroku":
	            return new HerokuClient();
	        case "cloudfoundry":
	        case "bluemix":
	        case "pivotal":
	            return new CloudFoundryClient();
	        default:
	            throw new IllegalArgumentException("Provider " + provider + " not supported");
        }
    }
    
    
}
