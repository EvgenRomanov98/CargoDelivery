package ua.nmu.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import ua.nmu.cargo_delivery.exceptions.DBException;
import ua.nmu.cargo_delivery.model.db.DBInit;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateStatusDeliveryServletTest extends TestMockitoSettings {
    @Spy
    private UpdateStatusDeliveryServlet servlet;
    private static MockedStatic<DBInit> db = Mockito.mockStatic(DBInit.class);

    @Test
    void doGetFail() throws ServletException, IOException, SQLException {
        when(req.getParameter("delivery")).thenReturn("1");
        when(req.getParameter("status")).thenReturn("PAID");
        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(0);
        assertThrows(DBException.class, () -> servlet.doGet(req, resp));
    }

    @Test
    void doGetAjax() throws ServletException, IOException, SQLException {
        when(req.getParameter("delivery")).thenReturn("1");
        when(req.getParameter("status")).thenReturn("PAID");
        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        when(req.getHeader("x-requested-with")).thenReturn("XMLHttpRequest");
        try (StringWriter out = new StringWriter();
             PrintWriter wrt = new PrintWriter(out)) {
            when(resp.getWriter()).thenReturn(wrt);
            servlet.doGet(req, resp);
            assertEquals("{\"status\" : 200}", out.toString());
        }
    }

    @Test
    void doGet() throws ServletException, IOException, SQLException {
        when(req.getParameter("delivery")).thenReturn("1");
        when(req.getParameter("status")).thenReturn("PAID");
        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        when(req.getHeader("x-requested-with")).thenReturn("");
        servlet.doGet(req, resp);
        verify(resp).sendRedirect(any());
    }

    @AfterAll
    public static void close() {
        db.close();
    }

}