package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class DeliveryManager {
    private static final Logger log = LogManager.getLogger(DeliveryManager.class);
    private static final DBManager db = DBManager.getInstance();

    private DeliveryManager() {
        //hide
    }

    public static List<Delivery> findDeliveries(int limit, int page) {
        try (Connection c = db.getConnection()) {
            List<Delivery> deliveries = db.findDeliveries(c, limit, page);
            log.trace("Find deliveries = {}", deliveries);
            return deliveries;
        } catch (SQLException | ParseException e) {
            throw new DBException(e.getMessage(), e);
        }
    }
}
