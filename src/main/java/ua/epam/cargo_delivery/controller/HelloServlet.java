package ua.epam.cargo_delivery.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", value = "/")
public class HelloServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(HelloServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
