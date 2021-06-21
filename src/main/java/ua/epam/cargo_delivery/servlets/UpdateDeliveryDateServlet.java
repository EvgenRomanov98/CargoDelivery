package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.db.DeliveryManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@WebServlet(name = "UpdateDeliveryDateServlet", urlPatterns = "/updateDeliveryDate")
public class UpdateDeliveryDateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("delivery"));
        String deliveryDate = req.getParameter("deliveryDate");
        DeliveryManager.updateDateDelivery(id, deliveryDate);
        String ajax = req.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(ajax)) {
            resp.getWriter().write("{\"status\" : 200}");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/manager");
    }
}
