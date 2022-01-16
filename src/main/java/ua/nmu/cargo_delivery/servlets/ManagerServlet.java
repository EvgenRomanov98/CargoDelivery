package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.model.db.DeliveryManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ManagerServlet", urlPatterns = "/manager")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("managerDeliveries", DeliveryManager.findDeliveriesWithCargoes(100, 0, "status_id"));
        req.getRequestDispatcher("/WEB-INF/manager.jsp").forward(req, resp);
    }
}
