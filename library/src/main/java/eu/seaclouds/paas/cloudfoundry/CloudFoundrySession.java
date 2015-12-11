package eu.seaclouds.paas.cloudfoundry;

import java.util.List;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;


/**
 * 
 * @author 
 *
 */
public class CloudFoundrySession implements PaasSession {

	
	private static Logger logger = LoggerFactory.getLogger(CloudFoundrySession.class);
	
	private CloudFoundryConnector connector;
	
	
	/**
	 * 
	 * @param connector
	 */
	public CloudFoundrySession(CloudFoundryConnector connector) {
        this.connector = connector;
    }
	
	
	@Override
	public List<Module> list() throws PaasException
	{
		throw new UnsupportedOperationException("List method not implemented");
	}

	
	@Override
	public Module deploy(String moduleName, DeployParameters params) throws PaasException
	{
		logger.info("deploy({})" + moduleName);
		if (!connector.deployApp(moduleName, params.getPath(), params.getBuildpackUrl()))
		{
			throw new PaasException("Application not deployed");
		}

		return getModule(moduleName);
	}

	
	@Override
	public void undeploy(String moduleName) throws PaasException
	{
		logger.info("undeploy({})", moduleName);
        connector.deleteApp(moduleName);
	}

	
	@Override
	public void startStop(Module module, StartStopCommand command) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case START:
    			connector.getConnectedClient().startApplication(module.getName());
    			break;
    			
    		case STOP:
    			connector.getConnectedClient().stopApplication(module.getName());
    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Cloud Foundry)");
    	}
	}


	@Override
	public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_UP_INSTANCES:
    			connector.getConnectedClient().updateApplicationInstances(module.getName(), module.getRunningInstances() + 1);
    			break;
    			
    		case SCALE_DOWN_INSTANCES:
    			if (module.getRunningInstances() > 1) {
    				connector.getConnectedClient().updateApplicationInstances(module.getName(), module.getRunningInstances() - 1);
    			}
    			break;
    			
    		case SCALE_UP_MEMORY:
    			break;
    			
    		case SCALE_DOWN_MEMORY:
    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Cloud Foundry)");
    	}
	}
	
	
	@Override
	public void scale(Module module, ScaleCommand command, int scale_value) throws PaasException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_INSTANCES:
    			connector.getConnectedClient().updateApplicationInstances(module.getName(), scale_value);
    			break;
    			
    		case SCALE_MEMORY:
    			connector.getConnectedClient().updateApplicationMemory(module.getName(), scale_value);
    			break;
    			
    		case SCALE_DISK:
    			connector.getConnectedClient().updateApplicationDiskQuota(module.getName(), scale_value);
    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
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
		logger.info("getModule({})", moduleName);
		CloudApplication app = connector.getConnectedClient().getApplication(moduleName);
		if (app == null) {
			throw new PaasException("Application " + moduleName + " is NULL");
		}
		return new eu.seaclouds.paas.cloudfoundry.Module(app);
	}
	

}
