package eu.seaclouds.paas.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import eu.seaclouds.paas.PaasSession.ScaleCommand;
import eu.seaclouds.paas.PaasSession.ScaleUpDownCommand;
import eu.seaclouds.paas.PaasSession.StartStopCommand;
import eu.seaclouds.paas.data.Application;
import eu.seaclouds.paas.heroku.DeployParameters;


/**
 * 
 * @author 
 *
 */
public abstract class PaaSResource
{


	private static Logger log = LoggerFactory.getLogger(PaaSResource.class);
	protected PaasClient client;


	public PaaSResource(PaasClient client)
	{
		if (client == null)
		{
			throw new NullPointerException("client cannot be null");
		}
		this.client = client;
	}


	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String index()
	{
		return "index";
	}


	/*
	 * curl http://localhost:8080/heroku/applications -X POST -F
	 * file=@"SampleApp3.war" -F model='{"name":"samplewa r"}' -H"Content-Type:
	 * multipart/form-data" -H"apikey:<apikey>"
	 */
	@POST
	@Path("/applications")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
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

			file = File.createTempFile("up-tmp-file", ".tmp");

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


	@GET
	@Path("applications/{name}")
	public String getApplication(@PathParam("name") String name, @Context HttpHeaders headers)
	{
		log.info("getApplication({})", name);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);
		
		Module m = session.getModule(name);
		log.debug("application " + m.getName() + " : " + m.getUrl());
		return "get /applications/" + name;
	}


	@DELETE
	@Path("/applications/{name}")
	public String deleteApplication(@PathParam("name") String name, @Context HttpHeaders headers)
	{
		log.info("deleteApplication({})", name);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);

		session.undeploy(name);
		log.debug("application {} deleted", name);
		return "delete /applications/" + name;
	}


	@PUT
	@Path("/applications/{name}/start")
	public String startApplication(@PathParam("name") String name, @Context HttpHeaders headers)
	{
		log.info("startApplication({})", name);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);

		Module m = session.getModule(name);
	    session.startStop(m, StartStopCommand.START);
	    
	    log.debug("application {} started", name);
		return "put /applications/" + name + "/start";
	}


	@PUT
	@Path("/applications/{name}/stop")
	public String stopApplication(@PathParam("name") String name, @Context HttpHeaders headers)
	{
		log.info("stopApplication({})", name);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);

		Module m = session.getModule(name);
	    session.startStop(m, StartStopCommand.START);
	    
	    log.debug("application {} stopped", name);
		return "put /applications/" + name + "/stop";
	}
	
	
	@PUT
	@Path("/applications/{name}/scale/{updown}")
	public String scaleUpDownApplication(@PathParam("name") String name, @PathParam("updown") String updown, @Context HttpHeaders headers)
	{
		log.info("scaleUpDownApplication({}, {})", name, updown);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);

		Module m = session.getModule(name);
		if ("up".equalsIgnoreCase(updown))
			session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
		else if ("down".equalsIgnoreCase(updown))
			session.scaleUpDown(m, ScaleUpDownCommand.SCALE_DOWN_INSTANCES);
		else
			log.error("application {} not scaled", name);
	    
	    log.debug("application {} scaled {}", name, updown);
		return "put /applications/" + name + "/scale/" + updown;
	}
	
	
	@PUT
	@Path("/applications/{name}/scale/{type}/{value}")
	public String scaleApplication(@PathParam("name") String name, @PathParam("type") String type, 
			@PathParam("value") String value, @Context HttpHeaders headers)
	{
		log.info("scaleApplication({}, {}, {})", name, type, value);
		Credentials credentials = extractCredentials(headers);
		PaasSession session = client.getSession(credentials);

		Module m = session.getModule(name);
		if ("instances".equalsIgnoreCase(type))
			session.scale(m, ScaleCommand.SCALE_INSTANCES, Integer.parseInt(value));
		else if ("memory".equalsIgnoreCase(type))
			session.scale(m, ScaleCommand.SCALE_MEMORY, Integer.parseInt(value));
		else if ("disk".equalsIgnoreCase(type))
			session.scale(m, ScaleCommand.SCALE_DISK, Integer.parseInt(value));
		else
			log.error("application {} not scaled", name);
	    
	    log.debug("application {} scaled {} - {}", name, type, value);
		return "put /applications/" + name + "/scale/" + type + "/" + value;
	}
	
	
	@PUT
	@Path("/applications/{name}/bind/{service}")
	public String bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
	{
		// TODO implement method
		return "put /applications/" + name + "/bind/ + service";
	}

	
	/**
	 * 
	 * @param headers
	 * @return
	 */
	protected abstract Credentials extractCredentials(HttpHeaders headers);

	
	/**
	 * 
	 * @param is
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void saveToFile(InputStream is, File file) throws FileNotFoundException, IOException
	{
		OutputStream os = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = is.read(bytes)) != -1)
		{
			os.write(bytes, 0, read);
		}
		os.flush();
		os.close();
	}

}
