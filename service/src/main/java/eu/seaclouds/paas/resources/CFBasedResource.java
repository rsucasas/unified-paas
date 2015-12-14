package eu.seaclouds.paas.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;


public abstract class CFBasedResource extends PaaSResource
{
	
	
	private static Logger log = LoggerFactory.getLogger(CFBasedResource.class);
	

	/**
	 * 
	 * @param client
	 */
	public CFBasedResource(PaasClient client)
	{
		super(client);
	}
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String index()
	{
		return "index - CloudFoundry based resource";
	}


	@Override
	protected Credentials extractCredentials(HttpHeaders headers)
	{
		Credentials credentials = null;
		
		log.debug("Checking credentials [CloudFoundry] ...");

		List<String> crs = headers.getRequestHeader("credentials");
		if (crs != null && !crs.isEmpty())
		{
			credentials = new Credentials.ApiUserPasswordOrgSpaceCredentials(
					crs.get(0), crs.get(1), crs.get(2), crs.get(3), crs.get(5), Boolean.valueOf(crs.get(6)));
		}
		else
		{
			throw new WebApplicationException("Credentials not found in request headers", Response.Status.BAD_REQUEST);
		}
		return credentials;
	}
	

}
