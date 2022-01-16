package ua.nmu.cargo_delivery.servlets;

import ua.nmu.cargo_delivery.model.reports.XlsReport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet(name = "XlsReportServlet", urlPatterns = "/getReport")
public class XlsReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ByteArrayOutputStream baos = XlsReport.genXls(req.getParameter("fromRegion"),
                req.getParameter("toRegion"),
                req.getParameter("createDate"),
                req.getParameter("deliveryDate"))) {
            resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
            resp.addHeader("Content-Disposition", "attachment;filename=report.xls");
            baos.writeTo(resp.getOutputStream());
        }
    }
}
