package epam.gymcrm.config;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null; // No separate servlet configuration
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Map everything to DispatcherServlet
    }
}
