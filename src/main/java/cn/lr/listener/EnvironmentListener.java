package cn.lr.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Lifecycle Listener implementation class EnvironmentListener
 *
 */
public class EnvironmentListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public EnvironmentListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	String projectHome = System.getenv("lr_project_home");
    	System.out.println("ServletContext上下文初始化的");
    	if(projectHome == null) {
    		projectHome = ".";
    	}
    	System.setProperty("project.home", projectHome);
    }
	
}
