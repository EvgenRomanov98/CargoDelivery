package ua.epam.cargo_delivery.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.PermissionDenied;
import ua.epam.cargo_delivery.model.Action;
import ua.epam.cargo_delivery.model.db.Role;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(filterName = "SecurityFilter")
public class SecurityFilter implements Filter {
    private static final Logger log = LogManager.getLogger(SecurityFilter.class);
    private final Pattern p = Pattern.compile(".*(\\.css|\\.js|\\.svg|\\.html|\\.jsp|\\.map|\\.ico)$");
    private static final String LOGGED_USER = "loggedUser";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (p.matcher(req.getServletPath()).matches()) {
            chain.doFilter(request, response);
            return;
        }
        User user = (User) req.getSession().getAttribute(LOGGED_USER);
        try {
            if (user == null) {
                user = initUser(req);
            }
            if (!user.getRole().checkPermission(Action.findAction(req.getServletPath()))) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                throw new PermissionDenied("Resource not available");
            }
        } catch (Exception e) {
            log.error("Security handler, path = {}, user = {}", req.getServletPath(), user,  e);
            req.getSession().setAttribute("error", e.getMessage());
            resp.sendRedirect("error.jsp");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private User initUser(HttpServletRequest req) {
        User user = new User(Role.USER);
        req.getSession().setAttribute(LOGGED_USER, user);
        return user;
    }
}
