package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.db.User;
import ua.epam.cargo_delivery.model.db.UserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User(
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("surname"),
                req.getParameter("phone"),
                true);
        UserManager.saveUser(user);
        req.getSession().setAttribute("loggedUser", user);
        resp.sendRedirect(req.getContextPath() + "/privateOffice");
    }
}