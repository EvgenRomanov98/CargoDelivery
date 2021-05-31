package ua.epam.cargo_delivery.controller;

import ua.epam.cargo_delivery.model.dao.DeliveryManager;
import ua.epam.cargo_delivery.model.dao.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PrivateOfficeServlet", urlPatterns = "/privateOffice")
public class PrivateOfficeServlet extends HttpServlet {
    private final int limit = 5;
    private final int page = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("PrivateOfficeServlet");
        if (req.getSession().getAttribute("userDeliveries") == null) {
            System.out.println("PrivateOfficeServlet set userDeliveries");
            User user = (User) req.getSession().getAttribute("loggedUser");
            req.getSession().setAttribute("userDeliveries",
                    DeliveryManager.findDeliveriesForUser(limit, page, user));
        }
        req.getRequestDispatcher("privateOffice.jsp").forward(req, resp);
    }
}
