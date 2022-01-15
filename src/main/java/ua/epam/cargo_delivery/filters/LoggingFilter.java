package ua.epam.cargo_delivery.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoggingFilter", urlPatterns = "/*")
public class LoggingFilter implements Filter {
    private final Logger log = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            log.info(">>> REQUEST: {}, {}", ((HttpServletRequest) request).getRequestURI(), request.getParameterMap());
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String userMessage = e.getMessage() == null ? "Something wrong! Please, try later" : e.getMessage();
            ((HttpServletRequest) request).getSession().setAttribute("error", userMessage);
            ((HttpServletResponse) response).sendRedirect("error.jsp");
        }
        log.info("<<< RESPONSE: {}", ((HttpServletResponse) response).getStatus());
    }

    @Override
    public void destroy() {

    }
}
