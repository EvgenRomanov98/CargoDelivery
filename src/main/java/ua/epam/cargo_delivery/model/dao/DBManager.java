package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.DBException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private final Logger log = LogManager.getLogger(DBManager.class);
    private static DBManager instance;
    private final DataSource ds;

    private static final String INSERT_USER = "INSERT INTO users (email, password, role_id, name, surname, phone) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM users where email = ?";
    private static final String SELECT_DELIVERIES_WITH_LIMIT = "SELECT * FROM deliveries LIMIT ? OFFSET ?";


    private DBManager() {
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

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public void saveUser(Connection connection, User user) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, Role.AUTHORIZE_USER.getId());
            ps.setString(4, user.getName());
            ps.setString(5, user.getSurname());
            ps.setString(6, user.getPhone());
            ps.execute();
            ResultSet gk = ps.getGeneratedKeys();
            gk.next();
            user.setId(gk.getLong(1));
            user.setRole(Role.AUTHORIZE_USER);
            user.hidePassword();
        }
    }

    public User findUser(Connection c, User user) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_USER)) {
            ps.setString(1, user.getEmail());
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DBException("User with email " + user.getEmail() + " not found");
            }
            return new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("phone"),
                    Role.valueOf(rs.getInt("role_id")),
                    false
            );
        } finally {
            closeResource(rs);
        }
    }

    public List<Delivery> findDeliveries(Connection c, int limit, int page) throws SQLException, ParseException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_WITH_LIMIT)) {
            ps.setInt(1, limit);
            ps.setInt(2, page * limit);
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                deliveries.add(
                        new Delivery(
                                rs.getLong("id"),
                                rs.getString("from"),
                                rs.getString("to"),
                                Timestamp.valueOf(rs.getString("create_date")),
                                Timestamp.valueOf(rs.getString("delivery_date")),
                                rs.getLong("distance"),
                                rs.getLong("price")
                        )
                );
            }
            return deliveries;
        } finally {
            closeResource(rs);
        }
    }

    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                log.error("Fail close resource", e);
            }
        }
    }
}
