package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.DeliveryManager;
import ua.epam.cargo_delivery.model.db.DeliveryStatus;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PrivateOfficeServlet", urlPatterns = "/privateOffice")
public class PrivateOfficeServlet extends HttpServlet {
    private final int limit = 5;
    private final int page = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedUser");
        req.setAttribute("userDeliveries",
                DeliveryManager.findDeliveriesForUser(limit, page, user));
        List<Delivery> deliveriesForPay = DeliveryManager.findDeliveriesWithStatus(DeliveryStatus.APPROVED, user);
        req.getSession().setAttribute("deliveriesForPay", deliveriesForPay);
        req.setAttribute("commonPrice", deliveriesForPay.stream().map(Delivery::getPrice)
                .reduce(Integer::sum).orElse(null));
        req.getSession().setAttribute("totalNumberForUser", DeliveryManager.getTotalNumberForUser((User)req.getSession().getAttribute("loggedUser")));
        req.getRequestDispatcher("/WEB-INF/privateOffice.jsp").forward(req, resp);
    }
}
