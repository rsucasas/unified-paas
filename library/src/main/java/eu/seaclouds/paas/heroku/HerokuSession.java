package eu.seaclouds.paas.heroku;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heroku.api.AddonChange;
import com.heroku.api.App;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.ServiceApp;


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
        logger.info("deploy({})" + moduleName);
        App app = connector.createJavaWebApp(moduleName);
        
        Module module = new eu.seaclouds.paas.heroku.Module(app);
        boolean deployed = connector.deployJavaWebApp(module.getName(), params.getPath());
        
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
    			throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
    	}
    }


	@Override
	public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_UP_INSTANCES:
    			connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() + 1);
    			break;
    			
    		case SCALE_DOWN_INSTANCES:
    			if (module.getRunningInstances() > 1) {
    				connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() - 1);
    			}
    			break;
    			
    		case SCALE_UP_MEMORY:
    		case SCALE_DOWN_MEMORY:
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
    	}
	}
	
	
	@Override
	public void scale(Module module, ScaleCommand command, int scale_value) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_INSTANCES:
    			connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), scale_value);
    			break;
    			
    		case SCALE_MEMORY:
    		case SCALE_DISK:
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
    	}
	}


	@Override
	public void bindToService(Module module, ServiceApp service) throws PaasException
	{
		AddonChange change = connector.getHerokuAPIClient().addAddon(module.getName(), service.getServiceName());
		
	}


	@Override
	public void unbindFromService(Module module, ServiceApp service) throws PaasException
	{
		AddonChange change = connector.getHerokuAPIClient().removeAddon(module.getName(), service.getServiceName());
		
	}
	

	@Override
	public Module getModule(String moduleName) throws PaasException
	{
		logger.info("getModule({})", moduleName);
		App app = connector.getHerokuAPIClient().getApp(moduleName);
		if (app == null) {
			throw new PaasException("Application " + moduleName + " is NULL");
		}
		return new eu.seaclouds.paas.heroku.Module(app);
	}
    

}
