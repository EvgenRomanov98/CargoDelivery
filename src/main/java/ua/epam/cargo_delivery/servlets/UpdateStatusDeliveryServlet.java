package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.db.DeliveryManager;
import ua.epam.cargo_delivery.model.db.DeliveryStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeStatusDeliveryServlet", urlPatterns = "/changeStatus")
public class UpdateStatusDeliveryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("delivery"));
        DeliveryStatus status = DeliveryStatus.valueOf(req.getParameter("status"));
        DeliveryManager.updateStatusDelivery(id, status);
        String ajax = req.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(ajax)) {
            resp.getWriter().write("{\"status\" : 200}");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/manager");
    }
}
