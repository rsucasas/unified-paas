package eu.seaclouds.paas.heroku;

import java.util.ArrayList;
import java.util.List;
import com.heroku.api.Addon;
import com.heroku.api.App;


/**
 * Heroku module
 * @author
 *
 */
public class Module implements eu.seaclouds.paas.Module {

	
    private App app;
    private List<String> lServices;
    
    
    /**
     * 
     * @param app
     */
    public Module(App app) {
        this.app = app;
        lServices = new ArrayList<String>(0);
    }
    
    
    /**
     * 
     * @param app
     * @param l
     */
    public Module(App app, List<Addon> l) {
        this.app = app;
        
        if ((l != null) && (l.size() > 0))
        {
        	lServices = new ArrayList<String>(3);
        	for (Addon ad : l)
        	{
        		lServices.add(ad.getName());
        	}
        }
        else
        {
        	lServices = new ArrayList<String>(0);
        }
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

	
	@Override
	public List<String> getServices()
	{
		return lServices;
	}
    
    
}
