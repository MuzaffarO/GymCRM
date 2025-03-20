package epam.gymcrm.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;

public class FilterConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Register Logging Filter
        FilterRegistration.Dynamic loggingFilter = servletContext.addFilter("LoggingFilter", LoggingFilter.class);
        loggingFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
