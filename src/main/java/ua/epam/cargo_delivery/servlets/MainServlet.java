package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.db.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainServlet", urlPatterns = "/")
public class MainServlet extends HttpServlet {
    private static final String AVAILABLE_REGIONS = "availableRegions";
    private static final String LOGGED_USER = "loggedUser";
    private static final int LIMIT = 5;
    private static final String DELIVERIES = "deliveries";
    private static final String TOTAL_NUMBER = "totalNumber";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initUser(req);
        if (((User) req.getSession().getAttribute(LOGGED_USER)).getRole() == Role.MANAGER) {
            req.getRequestDispatcher("/manager").forward(req, resp);
            return;
        }
        initDisplayedData(req);
        setUpPagination(req);
        setSupportRegions(req);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void initDisplayedData(HttpServletRequest req) {
        if (req.getSession().getAttribute(DELIVERIES) == null) {
            List<Delivery> deliveries = DeliveryManager.findDeliveries(LIMIT, 0);
            req.getSession().setAttribute(DELIVERIES, deliveries);
        }
    }

    private void initUser(HttpServletRequest req) {
        if (req.getSession().getAttribute(LOGGED_USER) == null) {
            req.getSession().setAttribute(LOGGED_USER, new User(Role.USER));
        }
    }

    private void setUpPagination(HttpServletRequest req) {
        req.getSession().setAttribute(TOTAL_NUMBER, DeliveryManager.getTotalNumber());
    }

    private void setSupportRegions(HttpServletRequest req) {
        if (req.getSession().getAttribute(AVAILABLE_REGIONS) == null) {
            req.getSession().setAttribute(AVAILABLE_REGIONS, CityManager.getCities());
        }
    }
}
