package ua.nmu.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import ua.nmu.cargo_delivery.model.db.DBInit;
import ua.nmu.cargo_delivery.model.db.Role;
import ua.nmu.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class MainServletTest extends TestMockitoSettings {
    @Spy
    private MainServlet main;
    private static MockedStatic<DBInit> db = Mockito.mockStatic(DBInit.class);

    @Test
    void doGetManager() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("loggedUser")).thenReturn(User.builder().role(Role.MANAGER).build());
        when(req.getRequestDispatcher("/manager")).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(req, resp);
        main.doGet(req, resp);
        verify(req).getRequestDispatcher("/manager");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void doGet() throws ServletException, IOException, SQLException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("loggedUser")).thenReturn(User.builder().role(Role.AUTHORIZE_USER).build());
        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getLong("d_id")).thenReturn(1L);
        when(rs.getString(any()))
                .thenReturn("whence1")
                .thenReturn("whither1")
                .thenReturn("from_name1")
                .thenReturn("to_name1");
        when(rs.getObject("create_date", LocalDate.class)).thenReturn(LocalDate.now());
        when(rs.getObject("delivery_date", LocalDate.class)).thenReturn(LocalDate.now());
        when(rs.getFloat("distance")).thenReturn(12355F);
        when(rs.getInt("price")).thenReturn(1111);
        when(rs.getInt("status_id")).thenReturn(2);

        when(req.getRequestDispatcher("/WEB-INF/index.jsp")).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(req, resp);

        main.doGet(req, resp);

        verify(req).getRequestDispatcher("/WEB-INF/index.jsp");
        verify(dispatcher).forward(req, resp);
    }

    @AfterAll
    public static void close() {
        db.close();
    }
}