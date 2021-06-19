package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.dto.AvailableCities;
import ua.epam.cargo_delivery.model.db.City;
import ua.epam.cargo_delivery.model.db.CityManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CitiesServlet", urlPatterns = "/availableRegions")
public class CitiesServlet extends HttpServlet {
    private static final String AVAILABLE_REGIONS = "availableRegions";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute(AVAILABLE_REGIONS) == null) {
            req.getSession().setAttribute(AVAILABLE_REGIONS, CityManager.getCities());
            System.out.println("set in session availableRegions");
        }
        String ajax = req.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(ajax)) {
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new AvailableCities(
                    (List<City>) req.getSession().getAttribute(AVAILABLE_REGIONS)
            ).toString());
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
