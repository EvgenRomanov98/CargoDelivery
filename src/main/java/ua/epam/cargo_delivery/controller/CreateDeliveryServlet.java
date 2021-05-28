package ua.epam.cargo_delivery.controller;

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
        System.out.println("CreateDeliveryServlet = here");
        resp.sendRedirect(req.getContextPath() + "index.jsp");
    }
}
