package ua.nmu.cargo_delivery.servlets;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ExtendWith(MockitoExtension.class)
public class TestMockitoSettings {
    @Mock
    protected HttpServletRequest req;
    @Mock
    protected HttpServletResponse resp;
    @Mock
    protected HttpSession session;
    @Mock
    protected Connection connection;
    @Mock
    protected PreparedStatement ps;
    @Mock
    protected ResultSet rs;
    @Mock
    protected RequestDispatcher dispatcher;
}
