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

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = Optional.ofNullable(req.getParameter("email")).filter(s -> !s.isBlank()).orElse(null);
        String password = Optional.ofNullable(req.getParameter("password")).filter(s -> !s.isBlank()).orElse(null);
        String name = Optional.ofNullable(req.getParameter("name")).filter(s -> !s.isBlank()).orElse(null);
        String surname = Optional.ofNullable(req.getParameter("surname")).filter(s -> !s.isBlank()).orElse(null);
        String phone = Optional.ofNullable(req.getParameter("phone")).filter(s -> !s.isBlank()).orElse(null);

        if (email == null || password == null || name == null || surname == null || phone == null) {
            throw new AppException("Invalid input data");
        }

        User user = new User(email, password, name, surname, phone, true);
        UserManager.saveUser(user);
        req.getSession().setAttribute("loggedUser", user);
        resp.sendRedirect(req.getContextPath() + "/privateOffice");
    }
}