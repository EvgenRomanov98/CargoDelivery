package ua.epam.cargo_delivery.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.dao.Role;
import ua.epam.cargo_delivery.model.dao.User;
import ua.epam.cargo_delivery.model.dao.UserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(RegistrationServlet.class);

    @Override
    public void init() {
        //-*
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        User user = new User(email, password, Role.USER, true);
        log.debug(user);
        UserManager.saveUser(user);
        response.sendRedirect("index.jsp");
    }

    @Override
    public void destroy() {
        // +-
    }
}