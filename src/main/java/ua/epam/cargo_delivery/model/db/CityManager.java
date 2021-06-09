package ua.epam.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CityManager {
    private static final Logger log = LogManager.getLogger(CityManager.class);
    private static final DBManager db = DBManager.getInstance();


    public static List<City> getCities() {
        try (Connection c = db.getConnection()) {
            return db.findCities(c);
        } catch (SQLException e) {
            throw new DBException("Can't find cities");
        }
    }
}
