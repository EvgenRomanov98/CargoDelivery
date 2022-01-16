package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.model.db.User;
import ua.nmu.cargo_delivery.model.reports.PdfReceipt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet(name = "ReceiptServlet", urlPatterns = "/getReceipt")
public class ReceiptServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ByteArrayOutputStream baos = PdfReceipt.genPdf(Long.parseLong(req.getParameter("idDelivery")),
                (User) req.getSession().getAttribute("loggedUser"))) {
            resp.setContentType("application/pdf;charset=UTF-8");
            resp.addHeader("Content-Disposition", "inline; filename=receipt.pdf");
            baos.writeTo(resp.getOutputStream());
        }
    }
}
