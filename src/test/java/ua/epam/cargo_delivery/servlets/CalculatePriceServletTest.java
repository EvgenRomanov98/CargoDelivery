package ua.epam.cargo_delivery.servlets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.epam.cargo_delivery.clients.MapBoxClient;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculatePriceServletTest extends TestMockitoSettings {
    @Spy
    private CalculatePriceServlet calculate;
    @Mock
    private HttpClient mockHttp;
    @Mock
    private HttpResponse<Object> httpResponse;
    @Captor
    private ArgumentCaptor<Integer> intCaptor;
    @Captor
    private ArgumentCaptor<Float> floatCaptor;
    private static final MockedStatic<HttpClient> http = mockStatic(HttpClient.class);

    @Test
    void doGet() throws ServletException, IOException, InterruptedException {
        when(req.getParameter(any()))
                .thenReturn("11")
                .thenReturn("12")
                .thenReturn("fromName")
                .thenReturn("toName")
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("description")
                .thenReturn("200")
                .thenReturn("200")
                .thenReturn("200")
                .thenReturn("200")
                .thenReturn("");
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(any()))
                .thenReturn(mock(User.class))
                .thenReturn(1234)
                .thenReturn(250000F);

        http.when(HttpClient::newHttpClient).thenReturn(mockHttp);
        MapBoxClient.setSecretToken("test");
        when(mockHttp.send(any(), any())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("{\"routes\":[{\"distance\":11842.187,\"geometry\":{\"coordinates\":[[35.125152,48.511114],[35.123178,48.511061]]}}]}");
        doNothing().when(session).setAttribute(eq("price"), intCaptor.capture());
        doNothing().when(session).setAttribute(eq("distance"), floatCaptor.capture());
        try (StringWriter out = new StringWriter();
             PrintWriter wrt = new PrintWriter(out)) {
            when(resp.getWriter()).thenReturn(wrt);
            calculate.doGet(req, resp);
            assertEquals("{\"price\":\"139\",\"lngLat\":[[35.125153,48.511112],[35.123177,48.511063]],\"distance\":11842.187}", out.toString());
        }
    }

    @AfterAll
    public static void closeHttp() {
        http.close();
    }
}