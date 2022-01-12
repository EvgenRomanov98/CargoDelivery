package ua.epam.cargo_delivery.model.db;

import lombok.extern.slf4j.Slf4j;
import ua.epam.cargo_delivery.exceptions.DBException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DBInit {

    private static DataSource ds;

    public static void init() {
        if (ds != null) {
            throw new IllegalStateException("Cannot init DataSource twice");
        }
        try {
            InitialContext cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
            if (ds == null) {
                throw new DBException("Data source not found!");
            }
        } catch (NamingException e) {
            throw new IllegalStateException("Error init DBManager", e);
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot obtain a connection", ex);
        }
        return con;
    }
}
