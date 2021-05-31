package ua.epam.cargo_delivery.controller;

import ua.epam.cargo_delivery.model.Util;
import ua.epam.cargo_delivery.model.dao.Cargo;
import ua.epam.cargo_delivery.model.dao.Delivery;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PriceCalculateServlet", urlPatterns = "/calculatePrice")
public class PriceCalculateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Delivery delivery = Util.deliveryParseRequest(req);
        req.setAttribute("price", delivery.getPrice());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
