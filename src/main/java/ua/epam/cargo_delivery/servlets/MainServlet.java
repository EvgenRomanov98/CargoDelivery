package ua.epam.cargo_delivery.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.DeliveryManager;
import ua.epam.cargo_delivery.model.db.Role;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainServlet", urlPatterns = "/")
public class MainServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(MainServlet.class);
    private final String loggedUser = "loggedUser";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MainServlet " + req.getRequestURI());
        initDisplayedData(req);
        initUser(req);
        if (((User) req.getSession().getAttribute(loggedUser)).getRole() == Role.MANAGER) {
            req.getRequestDispatcher("/manager").forward(req, resp);
        } else {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    private void initUser(HttpServletRequest req) {
        if (req.getSession().getAttribute(loggedUser) == null) {
            req.getSession().setAttribute(loggedUser, new User(Role.USER));
        }
    }

    private void initDisplayedData(HttpServletRequest req) {
        if (req.getSession().getAttribute("deliveries") == null) {
            System.out.println("req.getSession().getAttribute(deliveries) = null");
            List<Delivery> deliveries = DeliveryManager.findDeliveries(5, 0);
            req.getSession().setAttribute("deliveries", deliveries);
        }
        System.out.println("result = " + req.getSession().getAttribute("deliveries"));
    }
}
