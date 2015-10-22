package eu.seaclouds.paas.heroku;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heroku.api.App;

import seaclouds.paas.adapter.heroku_deploy.HerokuConnector;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;

public class HerokuSession implements PaasSession {
    private static Logger logger = LoggerFactory.getLogger(HerokuSession.class);
    
    private HerokuConnector connector;
    
    public HerokuSession(HerokuConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<Module> list() throws PaasException {
        
        throw new UnsupportedOperationException("List method not implemented");
    }

    @Override
    public Module deploy(String moduleName, PaasSession.DeployParameters params)
            throws PaasException {
        eu.seaclouds.paas.heroku.DeployParameters _params = (eu.seaclouds.paas.heroku.DeployParameters) params;
        
        logger.info("deploy({})" + moduleName);
        App app = connector.createJavaWebApp(moduleName);
        
        Module module = new eu.seaclouds.paas.heroku.Module(app);
        boolean deployed = connector.deployJavaWebApp(module.getName(), _params.getPath());
        
        if (!deployed) {
            throw new PaasException("Application not deployed");
        }
        return module;
    }

    @Override
    public void undeploy(String moduleName) throws PaasException {
        logger.info("undeploy({})", moduleName);
        connector.deleteApp(moduleName);
    }

    @Override
    public void startStop(Module module, PaasSession.StartStopCommand command)
            throws PaasException {
        throw new UnsupportedOperationException("StartStop method not supported");
    }

}
