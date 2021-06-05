package ua.epam.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.DBException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private final Logger log = LogManager.getLogger(DBManager.class);
    private static DBManager instance;
    private final DataSource ds;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

    private static final String INSERT_USER = "INSERT INTO users (email, password, role_id, name, surname, phone) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER = "SELECT id AS u_id, email, password, name, surname, phone, role_id " +
            "FROM users where email = ?";
    private static final String INSERT_CARGO = "INSERT INTO cargoes (description, weight, length, width, height) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_DELIVERY = "INSERT INTO deliveries (whence, whither, distance, price, cargo_id, status_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_DELIVERIES_WITH_LIMIT = "SELECT id AS d_id, " +
            "whence, whither, create_date, delivery_date, distance,price,status_id " +
            "FROM deliveries WHERE status_id != 6 ORDER BY ? LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERIES_AND_CARGO_WITH_LIMIT_FOR_USER = "SELECT d.id AS d_id, " +
            "d.whence, d.whither, d.create_date, d.delivery_date, d.distance, d.price, d.status_id, " +
            "c.id AS c_id, c.description, c.weight, c.width, c.length, c.height " +
            "FROM deliveries d JOIN cargoes c on c.id = d.cargo_id WHERE user_id = ? AND status_id != 6 ORDER BY status_id LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERIES_EAGER = "SELECT *, d.id AS d_id, c.id AS c_id, u.id AS u_id FROM deliveries d " +
            "JOIN cargoes c ON c.id = d.cargo_id " +
            "JOIN users u ON u.id = d.user_id WHERE status_id != 6 ORDER BY ? LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERIES_FOR_USER_IN_STATUS = "SELECT id AS d_id, * FROM deliveries WHERE user_id = ? AND status_id = ?";
    private static final String UPDATE_STATUS_DELIVERY = "UPDATE deliveries SET status_id = ? WHERE id = ?";
    private static final String UPDATE_DATE_DELIVERY = "UPDATE deliveries SET delivery_date = to_date(?, 'YYYY-MM-DD') WHERE id = ?";

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

    public void insertUser(Connection connection, User user) throws SQLException {
        ResultSet gk = null;
        try (PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, Role.AUTHORIZE_USER.getId());
            ps.setString(4, user.getName());
            ps.setString(5, user.getSurname());
            ps.setString(6, user.getPhone());
            ps.execute();
            gk = ps.getGeneratedKeys();
            gk.next();
            user.setId(gk.getLong(1));
            user.setRole(Role.AUTHORIZE_USER);
            user.hidePassword();
        } finally {
            closeResource(gk);
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
            return extractUser(rs);
        } finally {
            closeResource(rs);
        }
    }

    public List<Delivery> findDeliveries(Connection c, int limit, int page, String orderBy) throws SQLException, ParseException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_WITH_LIMIT)) {
            ps.setString(1, orderBy);
            ps.setInt(2, limit);
            ps.setInt(3, page * limit);
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                deliveries.add(extractDelivery(rs));
            }
            return deliveries;
        } finally {
            closeResource(rs);
        }
    }

    public void insertCargo(Connection c, Cargo cargo) throws SQLException {
        ResultSet gk = null;
        try (PreparedStatement ps = c.prepareStatement(INSERT_CARGO, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cargo.getDescription());
            ps.setInt(2, cargo.getWeight());
            ps.setInt(3, cargo.getLength());
            ps.setInt(4, cargo.getWidth());
            ps.setInt(5, cargo.getHeight());
            ps.execute();
            gk = ps.getGeneratedKeys();
            gk.next();
            cargo.setId(gk.getLong(1));
        } finally {
            closeResource(gk);
        }
    }

    public void insertDelivery(Connection c, Delivery delivery) throws SQLException {
        ResultSet gk = null;
        try (PreparedStatement ps = c.prepareStatement(INSERT_DELIVERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, delivery.getWhence());
            ps.setString(2, delivery.getWhither());
            ps.setDouble(3, delivery.getDistance());
            ps.setInt(4, delivery.getPrice());
            ps.setLong(5, delivery.getCargo().getId());
            ps.setInt(6, delivery.getStatus().getId());
            ps.setLong(7, delivery.getUser().getId());
            ps.execute();
            gk = ps.getGeneratedKeys();
            gk.next();
            delivery.setId(gk.getLong(1));
        } finally {
            closeResource(gk);
        }
    }

    public List<Delivery> findDeliveriesForUser(Connection c, User user, int limit, int page) throws SQLException, ParseException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_AND_CARGO_WITH_LIMIT_FOR_USER)) {
            ps.setLong(1, user.getId());
            ps.setInt(2, limit);
            ps.setInt(3, page * limit);
            rs = ps.executeQuery();
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery d = extractDelivery(rs);
                d.setCargo(extractCargo(rs));
                deliveries.add(d);
            }
            return deliveries;
        } finally {
            closeResource(rs);
        }
    }

    // TODO: 31.05.21 rename findDeliveriesFullEager?
    public List<Delivery> findDeliveriesEager(Connection c, int limit, int page, String orderByColumn) throws SQLException, ParseException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_EAGER)) {
            ps.setString(1, orderByColumn);
            ps.setInt(2, limit);
            ps.setInt(3, page * limit);
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery d = extractDelivery(rs);
                d.setCargo(extractCargo(rs));
                d.setUser(extractUser(rs));
                d.getUser().hidePassword();
                deliveries.add(d);
            }
            return deliveries;
        } finally {
            closeResource(rs);
        }
    }

    public void updateStatus(Connection c, Long id, DeliveryStatus status) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(UPDATE_STATUS_DELIVERY)) {
            ps.setInt(1, status.getId());
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) {
                throw new DBException("Empty result of update a delivery id = " + id + " to status = " + status);
            }
        }
    }

    public List<Delivery> findDeliveriesWithStatus(Connection c, DeliveryStatus status, User user) throws SQLException, ParseException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_FOR_USER_IN_STATUS)) {
            ps.setLong(1, user.getId());
            ps.setInt(2, status.getId());
            rs = ps.executeQuery();
            ArrayList<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                deliveries.add(extractDelivery(rs));
            }
            return deliveries;
        } finally {
            closeResource(rs);
        }
    }

    public void updateDateDelivery(Connection c, Long id, String date) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(UPDATE_DATE_DELIVERY)) {
            ps.setString(1, date);
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) {
                throw new DBException("Empty result of update a delivery date. Id = " + id + " delivery date = " + date);
            }
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("u_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("phone"),
                Role.valueOf(rs.getInt("role_id")),
                false
        );
    }

    private Delivery extractDelivery(ResultSet rs) throws SQLException, ParseException {
        return new Delivery(
                rs.getLong("d_id"),
                rs.getString("whence"),
                rs.getString("whither"),
                rs.getObject("create_date", LocalDate.class),
                rs.getObject("delivery_date", LocalDate.class),
                rs.getFloat("distance"),
                rs.getInt("price"),
                DeliveryStatus.valueOf(rs.getInt("status_id"))
        );
    }

    private Cargo extractCargo(ResultSet rs) throws SQLException {
        return new Cargo(
                rs.getLong("c_id"),
                rs.getString("description"),
                rs.getInt("weight"),
                rs.getInt("length"),
                rs.getInt("width"),
                rs.getInt("height")
        );
    }

    public void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                log.error("Fail close resource", e);
            }
        }
    }

    public void rollbackConnection(Connection c) {
        if (c != null) {
            try {
                c.rollback();
            } catch (Exception e) {
                log.error("Fail rollback connection", e);
            }
        }
    }
}
