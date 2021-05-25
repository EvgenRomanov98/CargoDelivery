package ua.epam.cargo_delivery.controller;

import ua.epam.cargo_delivery.model.dao.User;
import ua.epam.cargo_delivery.model.dao.UserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User(
                request.getParameter("email"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("surname"),
                request.getParameter("phone"),
                true);
        UserManager.saveUser(user);
        request.getSession().setAttribute("loggedUser", user);
        response.sendRedirect("index.jsp");
    }
}