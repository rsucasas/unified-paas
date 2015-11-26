package eu.seaclouds.paas;

import javax.inject.Inject;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.seaclouds.paas.resources.HerokuResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServiceApplication extends Application<ServiceConfiguration> {
    private static Logger log = LoggerFactory.getLogger(ServiceApplication.class);

    private PaasClientFactory paasClientFactory;
    
    public static void main(String[] args) throws Exception {
        PaasClientFactory factory = new PaasClientFactory();
        
        new ServiceApplication(factory).run(args);
    }
    
    public ServiceApplication(PaasClientFactory paasClientFactory) {
        this.paasClientFactory = paasClientFactory;
    }
    
    @Override
    public String getName() {
        return "unified-paas-service";
    }
    
    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception {
        
        final HerokuResource heroku = new HerokuResource(paasClientFactory.getClient("heroku"));
        
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(heroku);
    }

}
