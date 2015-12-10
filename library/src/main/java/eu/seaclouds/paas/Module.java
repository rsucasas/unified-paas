package eu.seaclouds.paas;

public interface Module {
	
    String getName();
    
    String getUrl();
    
    String getAppType();
    
    int getRunningInstances();
    
}