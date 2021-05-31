package ua.epam.cargo_delivery.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.dao.Delivery;
import ua.epam.cargo_delivery.model.dao.DeliveryManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "MainServlet", urlPatterns = "/")
public class MainServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("HelloServlet " + req.getRequestURI());
        try {
            initDisplayedData(req);
            req.getRequestDispatcher(req.getContextPath() + "/index.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
