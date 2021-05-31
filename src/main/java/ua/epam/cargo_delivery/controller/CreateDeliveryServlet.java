package ua.epam.cargo_delivery.controller;

import ua.epam.cargo_delivery.model.Util;
import ua.epam.cargo_delivery.model.dao.Delivery;
import ua.epam.cargo_delivery.model.dao.DeliveryManager;
import ua.epam.cargo_delivery.model.dao.DeliveryStatus;

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
        Delivery delivery = Util.deliveryParseRequest(req);
        delivery.setStatus(DeliveryStatus.CREATED);
        DeliveryManager.saveDelivery(delivery);
        resp.sendRedirect("/privateOffice");
    }
}
