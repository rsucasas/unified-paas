package eu.seaclouds.paas;

import java.util.List;


public interface PaasSession {
    
    public interface DeployParameters {
    	String getPath();
    	String getBuildpackUrl();
    }

    public enum StartStopCommand {
        START,
        STOP
    }
    
    public enum ScaleUpDownCommand {
        UP,
        DOWN
    }

    List<Module> list() throws PaasException;
    
    Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException;
    
    void undeploy(String moduleName) throws PaasException;
    
    void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException;
    
    void scaleUpDown(Module module, PaasSession.ScaleUpDownCommand command) throws PaasException;
    
    void bindToService(Module module) throws PaasException;
    
    Module getModule(String moduleName) throws PaasException;
    
}