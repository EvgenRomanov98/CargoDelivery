package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.model.db.Delivery;
import ua.nmu.cargo_delivery.model.db.DeliveryManager;
import ua.nmu.cargo_delivery.model.db.DeliveryStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PayServlet", urlPatterns = "/pay")
public class PayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Delivery> deliveriesForPay = (ArrayList<Delivery>) req.getSession().getAttribute("deliveriesForPay");
        DeliveryManager.updateStatusDelivery(deliveriesForPay, DeliveryStatus.PAID);
        resp.sendRedirect(req.getContextPath() + "/privateOffice");
    }
}
