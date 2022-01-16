package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.model.Util;
import ua.nmu.cargo_delivery.model.db.Delivery;
import ua.nmu.cargo_delivery.model.db.DeliveryManager;
import ua.nmu.cargo_delivery.model.db.DeliveryStatus;
import ua.nmu.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateDeliveryServlet", urlPatterns = "/createDelivery")
public class CreateDeliveryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Delivery delivery = Util.extractDeliveryFromReq(req);
        delivery.setStatus(DeliveryStatus.CREATED);
        delivery.setUser((User) req.getSession().getAttribute("loggedUser"));
        DeliveryManager.saveDelivery(delivery);
        resp.sendRedirect(req.getContextPath() + "/privateOffice");
    }
}
