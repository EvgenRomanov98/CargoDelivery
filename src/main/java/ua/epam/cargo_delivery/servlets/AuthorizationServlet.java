package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.exceptions.AppException;
import ua.epam.cargo_delivery.model.db.User;
import ua.epam.cargo_delivery.model.db.UserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthorizationServlet", value = "/authorization")
public class AuthorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = Optional.ofNullable(req.getParameter("email")).filter(s -> !s.isBlank()).orElse(null);
        String password = Optional.ofNullable(req.getParameter("password")).filter(s -> !s.isBlank()).orElse(null);
        if (email == null || password == null) {
            throw new AppException("Invalid input");
        }
        User user = new User(email, password, false);
        req.getSession().setAttribute("loggedUser", UserManager.authenticate(user));
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
