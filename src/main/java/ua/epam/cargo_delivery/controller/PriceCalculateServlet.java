package ua.epam.cargo_delivery.controller;

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
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        int weight = Integer.parseInt(req.getParameter("weight"));
        int length = Integer.parseInt(req.getParameter("length"));
        int width = Integer.parseInt(req.getParameter("width"));
        int height = Integer.parseInt(req.getParameter("height"));

        int distance = getDistance(from, to);

        req.setAttribute("price",
                distance * 5 + (weight + length + width + height) / 1000);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    private int getDistance(String from, String to) {
        return 200;
    }
}
