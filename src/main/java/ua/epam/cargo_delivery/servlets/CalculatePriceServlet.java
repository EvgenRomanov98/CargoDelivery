package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.Util;
import ua.epam.cargo_delivery.model.db.Delivery;

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
        req.getSession().setAttribute("price", delivery.getPrice());
        resp.getWriter().write("{\"price\": " + delivery.getPrice() + "}");
    }
}
