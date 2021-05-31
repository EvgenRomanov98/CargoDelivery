package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DeliveryManager {
    private static final Logger log = LogManager.getLogger(DeliveryManager.class);
    private static final DBManager db = DBManager.getInstance();

    private DeliveryManager() {
        //hide
    }

    public static List<Delivery> findDeliveries(int limit, int page) {
        try (Connection c = db.getConnection()) {
            return db.findDeliveries(c, limit, page);
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public static void saveDelivery(Delivery delivery) {
        Connection c = null;
        try {
            c = db.getConnection();
            c.setAutoCommit(false);
            db.insertCargo(c, delivery.getCargo());
            db.insertDelivery(c, delivery);
            c.commit();
        } catch (SQLException e) {
            db.rollbackConnection(c);
            throw new DBException("Save delivery failed", e);
        } finally {
            db.closeResource(c);
        }
    }

    public static List<Delivery> findDeliveriesForUser(int limit, int page, User user) {
        try (Connection c = db.getConnection()) {
            return db.findDeliveriesForUser(c, user, limit, page);
        } catch (SQLException e) {
            throw new DBException("Fail get deliveries for user", e);
        }
    }
}
