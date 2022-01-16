package ua.nmu.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.nmu.cargo_delivery.exceptions.DBException;
import ua.nmu.cargo_delivery.model.Util;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DBManager {

    private final Logger log = LogManager.getLogger(DBManager.class);
    private static DBManager instance;
    private static final Pattern COLUMN_PATTERN = Pattern.compile("^(price|distance|whence|whither|c.description|d.from_name|" +
            "d.to_name|d.create_date|d.delivery_date|d.distance|d.price|c.weight|c.length|c.width|c.height|d.status)$");

    private static final String INSERT_USER = "INSERT INTO cargo_users (email, password, role_id, name, surname, phone) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER = "SELECT id AS u_id, email, password, name, surname, phone, role_id " +
            "FROM cargo_users where email = ?";
    private static final String SELECT_USER_BY_PHONE = "SELECT id AS u_id, * from cargo_users where phone = ?";
    private static final String INSERT_CARGO = "INSERT INTO cargo_cargoes (description, weight, length, width, height) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_DELIVERY = "INSERT INTO cargo_deliveries (whence, whither, from_name, to_name, distance, price, cargo_id, status_id, user_id, from_region_id, to_region_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_DELIVERIES_WITH_LIMIT = "SELECT d.id AS d_id, c.id as c_id, * " +
            "FROM cargo_deliveries d JOIN cargo_cargoes c on c.id = d.cargo_id WHERE d.status_id != 6 AND d.from_name LIKE ? AND d.to_name LIKE ? ORDER BY d.id LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERIES_AND_CARGO_WITH_LIMIT_FOR_USER = "SELECT d.id AS d_id, " +
            "d.whence, d.whither, d.from_name, d.to_name, d.create_date, d.delivery_date, d.distance, d.price, d.status_id, " +
            "c.id AS c_id, c.description, c.weight, c.width, c.length, c.height " +
            "FROM cargo_deliveries d JOIN cargo_cargoes c on c.id = d.cargo_id WHERE user_id = ? AND status_id != 6 ORDER BY status_id LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERY_AND_CARGO_FOR_USER = "SELECT *, d.id as d_id, c.id as c_id from cargo_deliveries d " +
            "JOIN cargo_cargoes c ON c.id = d.cargo_id WHERE d.id = ? AND d.user_id = ?";
    private static final String SELECT_DELIVERIES_EAGER = "SELECT *, d.id AS d_id, c.id AS c_id, u.id AS u_id FROM cargo_deliveries d " +
            "JOIN cargo_cargoes c ON c.id = d.cargo_id " +
            "JOIN cargo_users u ON u.id = d.user_id WHERE status_id != 6 ORDER BY ? LIMIT ? OFFSET ?";
    private static final String SELECT_DELIVERIES_FOR_USER_IN_STATUS = "SELECT id AS d_id, * FROM cargo_deliveries WHERE user_id = ? AND status_id = ?";
    private static final String SELECT_NUMBER_OF_DELIVERIES = "SELECT count(*) FROM cargo_deliveries";
    private static final String SELECT_NUMBER_OF_DELIVERIES_FOR_USER = "SELECT count(*) FROM cargo_deliveries WHERE user_id = ?";
    private static final String SELECT_DELIVERIES_REPORT = "SELECT *, d.id as d_id, u.id as u_id, c.id as c_id " +
            "FROM cargo_deliveries d " +
            "         join cargo_cargoes c on c.id = d.cargo_id " +
            "         join cargo_users u on u.id = d.user_id " +
            "WHERE (? IS NULL OR d.from_region_id = ?) " +
            "  AND (? IS NULL OR d.to_region_id = ?) " +
            "  AND (? IS NULL OR d.create_date = ?) " +
            "  AND (? IS NULL OR d.delivery_date = ?)";
    private static final String UPDATE_STATUS_DELIVERY = "UPDATE cargo_deliveries SET status_id = ? WHERE id = ?";
    private static final String UPDATE_DATE_DELIVERY = "UPDATE cargo_deliveries SET delivery_date = to_date(?, 'YYYY-MM-DD') WHERE id = ?";
    private static final String SELECT_CITIES = "SELECT * FROM cargo_cities";

    public Connection getConnection() throws SQLException {
        return DBInit.getConnection();
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
            Util.closeResource(gk);
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
            Util.closeResource(rs);
        }
    }

    public User findUserByPhone(Connection c, User user) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_USER_BY_PHONE)) {
            ps.setString(1, user.getPhone());
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DBException("User with phone " + user.getPhone() + " not found");
            }
            return extractUser(rs);
        } finally {
            Util.closeResource(rs);
        }
    }

    public List<Delivery> findDeliveries(Connection c, int limit, int page, String orderBy, boolean asc,
                                         String filterFrom, String filterTo, Long userId) throws SQLException {
        ResultSet rs = null;
        page = page == 0 ? page : --page;
        String ascStr = asc ? "" : "DESC";
        String sql;
        if (COLUMN_PATTERN.matcher(orderBy).matches()) {
            sql = SELECT_DELIVERIES_WITH_LIMIT.replace("ORDER BY d.id", "ORDER BY " + orderBy + " " + ascStr);
        } else {
            sql = asc ? SELECT_DELIVERIES_WITH_LIMIT : SELECT_DELIVERIES_WITH_LIMIT.replace("ORDER BY d.id", "ORDER BY d.id DESC");
        }
        if (userId != null) {
            sql = sql.replace("WHERE", "WHERE d.user_id = ? AND ");
        }
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            if (userId != null) {
                ps.setLong(i++, userId);
            }
            ps.setString(i++, "%" + filterFrom + "%");
            ps.setString(i++, "%" + filterTo + "%");
            ps.setInt(i++, limit);
            ps.setInt(i, page * limit);
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery d = extractDelivery(rs);
                d.setCargo(extractCargo(rs));
                deliveries.add(d);
            }
            return deliveries;
        } finally {
            Util.closeResource(rs);
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
            Util.closeResource(gk);
        }
    }

    public void insertDelivery(Connection c, Delivery delivery) throws SQLException {
        ResultSet gk = null;
        try (PreparedStatement ps = c.prepareStatement(INSERT_DELIVERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, delivery.getWhence());
            ps.setString(2, delivery.getWhither());
            ps.setString(3, delivery.getFromName());
            ps.setString(4, delivery.getToName());
            ps.setDouble(5, delivery.getDistance());
            ps.setInt(6, delivery.getPrice());
            ps.setLong(7, delivery.getCargo().getId());
            ps.setInt(8, delivery.getStatus().getId());
            ps.setLong(9, delivery.getUser().getId());
            ps.setLong(10, delivery.getFromRegion().getId());
            ps.setLong(11, delivery.getToRegion().getId());
            ps.execute();
            gk = ps.getGeneratedKeys();
            gk.next();
            delivery.setId(gk.getLong(1));
        } finally {
            Util.closeResource(gk);
        }
    }

    public List<Delivery> findDeliveriesForUser(Connection c, User user, int limit, int page) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_AND_CARGO_WITH_LIMIT_FOR_USER)) {
            ps.setLong(1, user.getId());
            ps.setInt(2, limit);
            ps.setInt(3, page * limit);
            rs = ps.executeQuery();
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery d = extractDelivery(rs);
                d.setCargo(extractCargo(rs));
                deliveries.add(d);
            }
            return deliveries;
        } finally {
            Util.closeResource(rs);
        }
    }

    public Delivery findDeliveryForUser(Connection c, long idDelivery, Long userId) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERY_AND_CARGO_FOR_USER)) {
            ps.setLong(1, idDelivery);
            ps.setLong(2, userId);
            rs = ps.executeQuery();
            rs.next();
            Delivery d = extractDelivery(rs);
            d.setCargo(extractCargo(rs));
            return d;
        } finally {
            Util.closeResource(rs);
        }
    }

    public List<Delivery> findDeliveriesEager(Connection c, int limit, int page, String orderByColumn) throws SQLException {
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
            Util.closeResource(rs);
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

    public List<Delivery> findDeliveriesWithStatus(Connection c, DeliveryStatus status, User user) throws SQLException {
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
            Util.closeResource(rs);
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

    public Integer numberOfDeliveries(Connection c) throws SQLException {
        try (ResultSet rs = c.prepareStatement(SELECT_NUMBER_OF_DELIVERIES).executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public Integer numberOfDeliveriesForUser(Connection c, Long id) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_NUMBER_OF_DELIVERIES_FOR_USER)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            Util.closeResource(rs);
        }
    }

    public List<Delivery> findDeliveriesReport(Connection c, Long fromRegion, Long toRegion, LocalDate createDate, LocalDate deliveryDate)
            throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_DELIVERIES_REPORT)) {
            if (fromRegion != null) {
                ps.setLong(1, fromRegion);
                ps.setLong(2, fromRegion);
            } else {
                ps.setNull(1, JDBCType.BIGINT.getVendorTypeNumber());
                ps.setNull(2, JDBCType.BIGINT.getVendorTypeNumber());
            }
            if (toRegion != null) {
                ps.setLong(3, toRegion);
                ps.setLong(4, toRegion);
            } else {
                ps.setNull(3, JDBCType.BIGINT.getVendorTypeNumber());
                ps.setNull(4, JDBCType.BIGINT.getVendorTypeNumber());
            }
            if (createDate != null) {
                ps.setObject(5, createDate);
                ps.setObject(6, createDate);
            } else {
                ps.setNull(5, JDBCType.DATE.getVendorTypeNumber());
                ps.setNull(6, JDBCType.DATE.getVendorTypeNumber());
            }
            if (deliveryDate != null) {
                ps.setObject(7, deliveryDate);
                ps.setObject(8, deliveryDate);
            } else {
                ps.setNull(7, JDBCType.DATE.getVendorTypeNumber());
                ps.setNull(8, JDBCType.DATE.getVendorTypeNumber());
            }
            rs = ps.executeQuery();
            ArrayList<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery d = extractDelivery(rs);
                d.setCargo(extractCargo(rs));
                d.setUser(extractUser(rs));
                deliveries.add(d);
            }
            if (deliveries.isEmpty()) {
                throw new DBException("Deliveries not found");
            }
            return deliveries;
        } finally {
            Util.closeResource(rs);
        }
    }

    public List<City> findCities(Connection c) throws SQLException {
        try (ResultSet rs = c.prepareStatement(SELECT_CITIES).executeQuery()) {
            ArrayList<City> cities = new ArrayList<>();
            while (rs.next()) {
                cities.add(extractCity(rs));
            }
            return cities;
        }
    }

    private City extractCity(ResultSet rs) throws SQLException {
        return new City(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("region"),
                rs.getString("locale_key")
        );
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

    private Delivery extractDelivery(ResultSet rs) throws SQLException {
        return new Delivery(
                rs.getLong("d_id"),
                rs.getString("whence"),
                rs.getString("whither"),
                rs.getString("from_name"),
                rs.getString("to_name"),
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
