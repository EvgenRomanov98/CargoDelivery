package ua.epam.cargo_delivery.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoggingFilter")
public class LoggingFilter implements Filter {
    private final Logger log = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            log.info(">>> REQUEST: {}, {}", ((HttpServletRequest) request).getRequestURI(), request.getParameterMap());
//            ((HttpServletRequest) request).getSession().setAttribute("error", null);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ((HttpServletRequest) request).getSession().setAttribute("error", e.getMessage());
            ((HttpServletResponse) response).sendRedirect("error.jsp");
        }
        log.info("<<< RESPONSE: {}", ((HttpServletResponse) response).getStatus());
    }
}
