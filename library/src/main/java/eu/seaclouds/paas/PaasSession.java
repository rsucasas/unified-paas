package eu.seaclouds.paas;

import java.util.List;

import eu.seaclouds.paas.PaasSession.DeployParameters;
import eu.seaclouds.paas.PaasSession.StartStopCommand;

public interface PaasSession {
    
    public interface DeployParameters {
        
    }

    public enum StartStopCommand {
        START,
        STOP
    }

    List<Module> list() throws PaasException;
    
    Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException;
    
    void undeploy(String moduleName) throws PaasException;
    
    void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException;
}