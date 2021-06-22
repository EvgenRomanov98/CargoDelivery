package ua.epam.cargo_delivery.servlets;

import ua.epam.cargo_delivery.dto.DeliveryDTO;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.DeliveryManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "PaginationDeliveryServlet", urlPatterns = "/paginationDelivery")
public class PaginationDeliveryServlet extends HttpServlet {
    private static final Integer DEFAULT_LIMIT = 5;
    private static final Integer DEFAULT_PAGE = 0;
    private static final String DEFAULT_ORDER_BY = "id";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer limit = Optional.ofNullable(req.getParameter("pageSize")).filter(s -> !s.isBlank()).map(Integer::parseInt).orElse(DEFAULT_LIMIT);
        Integer page = Optional.ofNullable(req.getParameter("pageNumber")).filter(s -> !s.isBlank()).map(Integer::parseInt).orElse(DEFAULT_PAGE);
        String orderBy = Optional.ofNullable(req.getParameter("orderBy")).filter(s -> !s.isBlank()).orElse(DEFAULT_ORDER_BY);
        boolean asc = Optional.ofNullable(req.getParameter("ascending")).filter(s -> !s.isBlank()).map(Boolean::parseBoolean).orElse(true);
        String filterFrom = Optional.ofNullable(req.getParameter("filter[fromName]")).orElse("");
        String filterTo = Optional.ofNullable(req.getParameter("filter[toName]")).orElse("");
        Long userId = Optional.ofNullable(req.getParameter("userId")).filter(s -> !s.isBlank()).map(Long::parseLong).orElse(null);
        List<Delivery> deliveries = DeliveryManager.findDeliveries(limit, page, orderBy, asc, filterFrom, filterTo, userId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(DeliveryDTO.builder().deliveries(deliveries).build().toString());
    }
}
