package ua.epam.cargo_delivery.controller;

import ua.epam.cargo_delivery.model.dao.User;
import ua.epam.cargo_delivery.model.dao.UserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AuthorizationServlet", value = "/authorization")
public class AuthorizationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User(req.getParameter("email"),
                req.getParameter("password"),
                false);
        req.getSession().setAttribute("loggedUser", UserManager.authenticate(user));
        resp.sendRedirect("index.jsp");
    }
}
