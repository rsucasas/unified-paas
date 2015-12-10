package eu.seaclouds.paas.heroku;

import com.heroku.api.App;


/**
 * Heroku module
 * @author
 *
 */
public class Module implements eu.seaclouds.paas.Module {

	
    private App app;
    
    
    public Module(App app) {
        this.app = app;
    }

    
    @Override
    public String getName() {
        return app.getName();
    }
    
    
    @Override
    public String getUrl() {
        return app.getWebUrl();
    }
    
    
    @Override
    public String getAppType() {
        return "web";
    }


	@Override
	public int getRunningInstances()
	{
		return app.getDynos();
	}
    
    
}
