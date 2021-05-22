package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.CreateUserException;

import java.sql.SQLException;

public class UserManager {
    private static final Logger log = LogManager.getLogger(UserManager.class);
    private static final DBManager db = DBManager.getInstance();

    private UserManager() {
        //hide
    }

    public static User saveUser(User user) {
        try {
            db.saveUser(db.getConnection(), user);
        } catch (SQLException e) {
            throw new CreateUserException("Save user in database failed", e);
        }
        return user;
    }
}
