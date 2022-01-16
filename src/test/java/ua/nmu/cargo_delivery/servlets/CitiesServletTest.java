package ua.nmu.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.nmu.cargo_delivery.model.db.City;
import ua.nmu.cargo_delivery.model.db.DBInit;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CitiesServletTest extends TestMockitoSettings {
    private static MockedStatic<DBInit> db = Mockito.mockStatic(DBInit.class);

    @Spy
    private CitiesServlet servlet;
    @Captor
    private ArgumentCaptor<List<City>> citiesCaptor;

    @Test
    void doGetNoAjax() throws IOException, ServletException, SQLException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("availableRegions")).thenReturn(null);
        doNothing().when(session).setAttribute(any(), citiesCaptor.capture());

        db.when(DBInit::getConnection).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(rs.getString("name"))
                .thenReturn("name1")
                .thenReturn("name2");

        when(rs.getString("region"))
                .thenReturn("region1")
                .thenReturn("region2");

        when(rs.getString("locale_key"))
                .thenReturn("locale_key1")
                .thenReturn("locale_key2");

        doNothing().when(resp).sendRedirect(any());
        servlet.doGet(req, resp);
        assertEquals(2, citiesCaptor.getValue().size());
        verify(resp).sendRedirect(any());
    }

    @Test
    void doGetAjax() throws IOException, ServletException, SQLException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("availableRegions")).thenReturn(Arrays.asList(
                City.builder().id(1L).name("name1").region("region1").localeKey("localeKey1").build(),
                City.builder().id(2L).name("name2").region("region2").localeKey("localeKey2").build())
        );
        when(req.getHeader("x-requested-with")).thenReturn("XMLHttpRequest");
        try (StringWriter out = new StringWriter();
             PrintWriter wrt = new PrintWriter(out)) {
            when(resp.getWriter()).thenReturn(wrt);
            servlet.doGet(req, resp);
            verify(resp).setCharacterEncoding("UTF-8");
            assertEquals("{\"availableRegions\":[{\"id\":1,\"name\":\"name1\",\"region\":\"region1\",\"localeKey\":\"localeKey1\"},{\"id\":2,\"name\":\"name2\",\"region\":\"region2\",\"localeKey\":\"localeKey2\"}]}", out.toString());
        }
    }

    @AfterAll
    public static void close() {
        db.close();
    }
}