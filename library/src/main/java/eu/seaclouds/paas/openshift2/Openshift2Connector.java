package eu.seaclouds.paas.openshift2;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.ConnectionBuilder;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.OpenShiftException;


/**
 * 
 *
 * @author ATOS
 * @date 19/2/2016-16:01:46
 */
public class Openshift2Connector
{
	
	
	// logger
	private static final Logger logger = LoggerFactory.getLogger(Openshift2Connector.class);
	
	// Openshift client
	private IOpenShiftConnection _of2client;
	
	
	/**
	 * 
	 * @param login
	 * @param passwd
	 */
	public Openshift2Connector(String login, String passwd)
	{
		logger.info(">> Connecting to Openshift2 ...");
		try
		{
			_of2client = new ConnectionBuilder().credentials(login, passwd).create();
		}
		catch (OpenShiftException e)
		{
			logger.warn(">> Not connected to Openshift2: " + e.getMessage());
			throw new RuntimeException("Not connected to Openshift2: " + e.getMessage(), e);
		}
		catch (IOException e)
		{
			logger.error(">> Not connected to Openshift2: " + e.getMessage());
			throw new RuntimeException("Not connected to Openshift2: " + e.getMessage(), e);
		}
	}
		

}
