package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.dto.Response;
import ua.nmu.cargo_delivery.exceptions.AppException;
import ua.nmu.cargo_delivery.servlets.commands.CheckEmail;
import ua.nmu.cargo_delivery.servlets.commands.CheckPhone;
import ua.nmu.cargo_delivery.servlets.commands.Command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "ValidationServlet", urlPatterns = "/validate")
public class ValidationServlet extends HttpServlet {
    private final HashMap<String, Command> commands = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commands.put("phone", new CheckPhone());
        commands.put("email", new CheckEmail());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String ajax = req.getHeader("x-requested-with");
            if (!"XMLHttpRequest".equals(ajax)) {
                throw new AppException("Only ajax support");
            }

            String command = Optional.ofNullable(req.getParameter("check")).filter(s -> !s.isBlank()).orElse(null);
            Command c;
            if (command == null || (c = commands.get(command)) == null) {
                throw new AppException("Command not found");
            }
            String param = Optional.ofNullable(req.getParameter("param")).filter(s -> !s.isBlank()).orElse(null);
            if (param == null) {
                throw new AppException("Empty search parameter");
            }
            resp.getWriter().write(c.execute(param));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(Response.builder().message(e.getMessage()).build().toString());
        }
    }
}
