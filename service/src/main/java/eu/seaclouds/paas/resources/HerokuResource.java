package eu.seaclouds.paas.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

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
import eu.seaclouds.paas.data.Application;
import eu.seaclouds.paas.heroku.DeployParameters;

@Path("/heroku")
public class HerokuResource {
    private static Logger log = LoggerFactory.getLogger(HerokuResource.class);
    private PaasClient client;
    
    public HerokuResource(PaasClient client) {
        if (client == null) {
            throw new NullPointerException("client cannot be null");
        }
        this.client = client;
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "index";
    }
    
    /*
     * curl http://localhost:8080/heroku/applications -X POST -F file=@"SampleApp3.war" 
     *   -F model='{"name":"samplewa r"}' -H"Content-Type: multipart/form-data" -H"apikey:<apikey>"
     */
    @POST
    @Path("/applications")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Application createApplication(
            @Context HttpHeaders headers, FormDataMultiPart form) {

        FormDataBodyPart filePart = form.getField("file");
        FormDataBodyPart modelPart = form.getField("model");
        
        modelPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        Application application = modelPart.getValueAs(Application.class); 

        log.info("createApplication({})", application.getName());
        
        Credentials credentials = extractCredentials(headers);
        
        if (application.getName() == null || application.getName().isEmpty()) {
            throw new WebApplicationException("application name must be specified", Response.Status.BAD_REQUEST);
        }
        PaasSession session = client.getSession(credentials);
        
        InputStream is = filePart.getEntityAs(InputStream.class);
        
        File file = null;
        Application result;
        try {
            
            file = File.createTempFile("up-heroku", ".tmp");

            saveToFile(is, file);
            
            DeployParameters params = new DeployParameters(file.getAbsolutePath());
            Module m = session.deploy(application.getName(), params);
            result = new Application(m.getName(), new URL(m.getUrl()));
            
        } catch (IOException e) {
            
            throw new WebApplicationException(e);
            
        } finally {
            
            if (file != null) {
                file.delete();
            }
        }
        log.info("Application {} created", application.getName());
        return result;
    }

    @GET
    @Path("applications/{name}")
    public String getApplication(@PathParam("name") String name, @Context HttpHeaders headers) {
        Credentials credentials = extractCredentials(headers);
        PaasSession session = client.getSession(credentials);
        return "get /applications/" + name;
    }
    
    @DELETE
    @Path("/applications/{name}")
    public String deleteApplication(@PathParam("name") String name, @Context HttpHeaders headers) {
        log.info("deleteApplication({})", name);
        Credentials credentials = extractCredentials(headers);
        PaasSession session = client.getSession(credentials);

        session.undeploy(name);
        log.debug("application {} deleted", name);
        return "delete /applications/" + name;
    }

    @PUT
    @Path("/applications/{name}/start")
    public String startApplication(@PathParam("name") String name) {
        return "put /applications/" + name + "/start";
    }
    
    @PUT
    @Path("/applications/{name}/stop")
    public String stopApplication(@PathParam("name") String name, Credentials credentials) {
        log.info("stopApplication({})", name);
        log.info("application {} stopped", name);
        return "put /applications/" + name + "/stop";
    }

    private Credentials extractCredentials(HttpHeaders headers) {
        Credentials credentials;
        
        List<String> apikeys = headers.getRequestHeader("apikey");
        if (apikeys != null && !apikeys.isEmpty()) {
            credentials = new Credentials.ApiKeyCredentials(apikeys.get(0));
        }
        else {
            throw new WebApplicationException("Credentials not found in request headers", Response.Status.BAD_REQUEST);
        }
        return credentials;
    }

    private void saveToFile(InputStream is, File file) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
    }
    
}
