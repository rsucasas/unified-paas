package eu.seaclouds.paas.openshift2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.ServiceApp;


/**
 * 
 *
 * @author ATOS
 * @date 11/3/2016-15:54:03
 */
public class Openshift2Session implements PaasSession
{
	
	
	private static Logger logger = LoggerFactory.getLogger(Openshift2Session.class);
    // paas connector
    private Openshift2Connector connector;
    
    
    /**
     * 
     * Constructor
     * @param connector
     */
    public Openshift2Session(Openshift2Connector connector) {
        this.connector = connector;
    }

	
	@Override
	public Module deploy(String moduleName, DeployParameters params) throws PaasException
	{
		logger.info("DEPLOY({})", moduleName);
		
		return null;
	}

	
	@Override
	public void undeploy(String moduleName) throws PaasException
	{
		logger.info("UNDEPLOY({})", moduleName);
        connector.deleteApp(moduleName);
	}
	

	@Override
	public void startStop(Module module, StartStopCommand command) throws PaasException, UnsupportedOperationException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case START:

    			break;
    			
    		case STOP:

    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Cloud Foundry)");
    	}
	}

	
	@Override
	public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException, UnsupportedOperationException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_UP_INSTANCES:

    			break;
    			
    		case SCALE_DOWN_INSTANCES:

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
	public void scale(Module module, ScaleCommand command, int scale_value) throws PaasException, UnsupportedOperationException
	{
		logger.info(command.name() + "({})", module.getName());
		switch (command)
    	{
    		case SCALE_INSTANCES:

    			break;
    			
    		case SCALE_MEMORY:

    			break;
    			
    		case SCALE_DISK:

    			break;
    			
    		default:
    			throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
    	}
	}

	
	@Override
	public void bindToService(Module module, ServiceApp service) throws PaasException
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void unbindFromService(Module module, ServiceApp service) throws PaasException
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public Module getModule(String moduleName) throws PaasException
	{
		logger.debug("getModule({})", moduleName);
		
		return null;
	}
	

}
