package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class DBManager {
    private final Logger log = LogManager.getLogger(DBManager.class);
    private static DBManager instance;
    private final DataSource ds;

    private static final String INSERT_USER = "INSERT INTO users (email, password, role_id) VALUES (?, ?, ?)";

    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/TestDB");
        } catch (NamingException e) {
            throw new IllegalStateException("Error init DBManager", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public User saveUser(Connection connection, User user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, Role.AUTHORIZE_USER.getId());
            ps.execute();
            ResultSet gk = ps.getGeneratedKeys();
            gk.next();
            user.setId(gk.getInt(1));
            return user;
        }
    }
}
