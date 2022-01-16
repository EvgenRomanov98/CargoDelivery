package ua.nmu.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.nmu.cargo_delivery.exceptions.AppException;
import ua.nmu.cargo_delivery.exceptions.DBException;
import ua.nmu.cargo_delivery.model.db.DBInit;
import ua.nmu.cargo_delivery.model.db.User;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizationServletTest extends TestMockitoSettings {
    @Spy
    private AuthorizationServlet auth;
    @Captor
    protected ArgumentCaptor<User> userCaptor;

    private static MockedStatic<DBInit> db = Mockito.mockStatic(DBInit.class);

    @Test
    void doPostNullParams() throws IOException {
        assertThrows(AppException.class, () -> auth.doPost(req, resp));
    }

    @Test
    void doPostUserNotFound() throws IOException, SQLException {
        when(req.getParameter("email")).thenReturn("m@m.com");
        when(req.getParameter("password")).thenReturn("111");

        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        assertThrows(DBException.class, () -> auth.doPost(req, resp));

        verify(ps).setString(1, "m@m.com");
    }

    @Test
    void doPostOk() throws IOException, SQLException {
        when(req.getParameter("email")).thenReturn("m@m.com");
        when(req.getParameter("password")).thenReturn("1");

        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        when(rs.getLong("u_id")).thenReturn(1L);
        when(rs.getString(any()))
                .thenReturn("m@m.com")
                .thenReturn("y6U62nu/lmtZ0h4x/wzLztBnuxPU3hS2BK7KTK01VJg=$9pOICvsoR+klL6HNFq9ruBE1C0axVQPeTYdwAnAcrqg=")
                .thenReturn("name")
                .thenReturn("surname")
                .thenReturn("0963333333331");
        when(rs.getInt("role_id")).thenReturn(2);

        when(req.getSession()).thenReturn(session);
        doNothing().when(session).setAttribute(any(), userCaptor.capture());
        assertDoesNotThrow(() -> auth.doPost(req, resp));

        User value = userCaptor.getValue();
        assertEquals("m@m.com", value.getEmail());
        assertEquals("", value.getPassword());
        verify(ps).setString(1, "m@m.com");
    }

    @AfterAll
    public static void close() {
        db.close();
    }
}