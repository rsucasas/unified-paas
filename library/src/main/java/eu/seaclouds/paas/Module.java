package eu.seaclouds.paas;

import java.util.List;

public interface Module {
	
    String getName();
    
    String getUrl();
    
    String getAppType();
    
    int getRunningInstances();
    
    List<String> getServices();
    
}