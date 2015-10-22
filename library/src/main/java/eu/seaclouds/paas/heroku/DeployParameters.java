package eu.seaclouds.paas.heroku;

import eu.seaclouds.paas.PaasSession;

public class DeployParameters implements PaasSession.DeployParameters {

    private String path;

    public DeployParameters(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
