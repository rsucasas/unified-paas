package eu.seaclouds.paas.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.data.Application;
import eu.seaclouds.paas.heroku.DeployParameters;


@Path("/heroku")
public class HerokuResource extends PaaSResource
{


	private static Logger log = LoggerFactory.getLogger(HerokuResource.class);


	/**
	 * 
	 * @param client
	 */
	public HerokuResource(PaasClient client)
	{
		super(client);
	}


	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String index()
	{
		return "index - Heroku resource";
	}


	/*
	 * curl http://localhost:8080/heroku/applications -X POST -F
	 * file=@"SampleApp3.war" -F model='{"name":"samplewar"}' -H"Content-Type:
	 * multipart/form-data" -H"apikey:<apikey>"
	 */
	@POST
	@Path("/applications")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Application createApplication(@Context HttpHeaders headers, FormDataMultiPart form)
	{
		FormDataBodyPart filePart = form.getField("file");
		FormDataBodyPart modelPart = form.getField("model");

		modelPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		Application application = modelPart.getValueAs(Application.class);

		log.info("createApplication({})", application.getName());

		Credentials credentials = extractCredentials(headers);

		if (application.getName() == null || application.getName().isEmpty())
		{
			throw new WebApplicationException("application name must be specified", Response.Status.BAD_REQUEST);
		}
		PaasSession session = client.getSession(credentials);

		InputStream is = filePart.getEntityAs(InputStream.class);

		File file = null;
		Application result;
		try
		{
			file = File.createTempFile("up-heroku", ".tmp");

			saveToFile(is, file);

			DeployParameters params = new DeployParameters(file.getAbsolutePath());
			Module m = session.deploy(application.getName(), params);
			result = new Application(m.getName(), new URL(m.getUrl()));

		}
		catch (IOException e)
		{

			throw new WebApplicationException(e);

		}
		finally
		{

			if (file != null)
			{
				file.delete();
			}
		}
		log.info("Application {} created", application.getName());
		return result;
	}


	@Override
	protected Credentials extractCredentials(HttpHeaders headers)
	{
		Credentials credentials;

		List<String> apikeys = headers.getRequestHeader("apikey");
		if (apikeys != null && !apikeys.isEmpty())
		{
			credentials = new Credentials.ApiKeyCredentials(apikeys.get(0));
		}
		else
		{
			throw new WebApplicationException("Credentials not found in request headers", Response.Status.BAD_REQUEST);
		}
		return credentials;
	}

	
}
