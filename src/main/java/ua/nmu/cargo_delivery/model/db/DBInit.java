package ua.nmu.cargo_delivery.model.db;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.ds.PGSimpleDataSource;
import ua.nmu.cargo_delivery.exceptions.DBException;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DBInit {

    private static DataSource ds;

    public static void init(DsType type) {
        try {
            if (type == DsType.SIMPLE) {
                Class.forName("org.postgresql.Driver");
                ds = new PGSimpleDataSource();
                ((PGSimpleDataSource) ds).setUrl(System.getProperty("DB_URL"));
                ((PGSimpleDataSource) ds).setUser(System.getProperty("USER"));
                ((PGSimpleDataSource) ds).setPassword(System.getProperty("PASSWORD"));
            } else if (type == DsType.JNDI){
                if (ds != null) {
                    throw new IllegalStateException("Cannot init DataSource twice");
                }
                InitialContext cxt = new InitialContext();
                ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
                if (ds == null) {
                    throw new DBException("Data source not found!");
                }
            } else {
                throw new Exception("Connection type not found");
            }
        } catch (Exception e) {
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
