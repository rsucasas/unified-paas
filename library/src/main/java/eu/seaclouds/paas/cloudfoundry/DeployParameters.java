package eu.seaclouds.paas.cloudfoundry;

import eu.seaclouds.paas.PaasSession;


/**
 * 
 * @author
 *
 */
public class DeployParameters implements PaasSession.DeployParameters
{
	
	
	private String path;
    private String buildpack_url;

    
    public DeployParameters(String path) {
        this.path = path;
        this.buildpack_url = "";
    }
    
    
    public DeployParameters(String path, String buildpack_url) {
        this.path = path;
        this.buildpack_url = buildpack_url;
    }
    
    
    public String getPath() {
        return path;
    }
    
    
    public String getBuildpackUrl()
    {
    	return buildpack_url;
    }
    

}
