package epam.gymcrm.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public void init(FilterConfig filterConfig) {
        logger.info("Initializing Logging Filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Generate unique transaction ID
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId); // Store in logging context

        // Log request details
        logger.info("Transaction ID: {} | Method: {} | URI: {} | Params: {}",
                transactionId, httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getParameterMap());

        try {
            chain.doFilter(request, response);
            // Log response status
            logger.info("Transaction ID: {} | Response Status: {}", transactionId, httpResponse.getStatus());
        } catch (Exception e) {
            logger.error("Transaction ID: {} | Error: {}", transactionId, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        logger.info("Destroying Logging Filter");
    }
}
