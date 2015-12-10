package eu.seaclouds.paas.heroku;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heroku.api.App;
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
    public Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException {
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
    public void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException {
    	logger.info(command.name() + "({})", module.getName());
    	switch (command)
    	{
    		case START:
    			connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), 1);
    			break;
    			
    		case STOP:
    			connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), 0);
    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " method not supported");
    	}
    }


	@Override
	public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case UP:
    			connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() + 1);
    			break;
    			
    		case DOWN:
    			if (module.getRunningInstances() > 1) {
    				connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() - 1);
    			}
    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " method not supported");
    	}
	}


	@Override
	public void bindToService(Module module) throws PaasException
	{
		throw new UnsupportedOperationException("BindToService method not implemented");
	}


	@Override
	public Module getModule(String moduleName) throws PaasException
	{
		App app = connector.getHerokuAPIClient().getApp(moduleName);
		if (app == null) {
			throw new PaasException("Application " + moduleName + " is NULL");
		}
		return new eu.seaclouds.paas.heroku.Module(app);
	}
    

}
