package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.clients.MapBoxClient;
import ua.nmu.cargo_delivery.dto.DeliveryDTO;
import ua.nmu.cargo_delivery.model.Util;
import ua.nmu.cargo_delivery.model.db.Delivery;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CalculatePriceServlet", urlPatterns = "/calculatePrice")
public class CalculatePriceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Delivery delivery = Util.extractDeliveryFromReq(req);
        DeliveryDTO dto = MapBoxClient.getGeoJson(delivery.getWhence(), delivery.getWhither());
        delivery.setDistance(dto.getDistance());
        delivery.calculatePrice();
        req.getSession().setAttribute("price", delivery.getPrice());
        req.getSession().setAttribute("distance", delivery.getDistance());
        dto.setPrice(delivery.getPrice().toString());
        resp.getWriter().write(dto.toString());
    }
}
