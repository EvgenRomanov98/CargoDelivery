package ua.epam.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import ua.epam.cargo_delivery.model.db.DBInit;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class PrivateOfficeServletTest extends TestMockitoSettings {
    @Spy
    private PrivateOfficeServlet servlet;
    private static MockedStatic<DBInit> db = Mockito.mockStatic(DBInit.class);

    @Test
    void doGet() throws ServletException, IOException, SQLException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("loggedUser")).thenReturn(User.builder().id(1L).build());
        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(any())).thenReturn(1235).thenReturn(2);
        when(req.getRequestDispatcher("/WEB-INF/privateOffice.jsp")).thenReturn(dispatcher);
        servlet.doGet(req, resp);
        verify(dispatcher).forward(req, resp);
    }

    @AfterAll
    public static void close() {
        db.close();
    }
}