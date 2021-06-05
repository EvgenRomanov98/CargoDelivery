package ua.epam.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.CreateUserException;
import ua.epam.cargo_delivery.exceptions.DBException;
import ua.epam.cargo_delivery.exceptions.PermissionDenied;

import java.sql.Connection;
import java.sql.SQLException;

public class UserManager {
    private static final Logger log = LogManager.getLogger(UserManager.class);
    private static final DBManager db = DBManager.getInstance();

    private UserManager() {
        //hide
    }

    public static void saveUser(User user) {
        Connection c = null;
        try {
            c = db.getConnection();
            log.trace("User for save = {}", user);
            db.insertUser(c, user);
            log.trace("Stored user = {}", user);
        } catch (SQLException e) {
            db.rollbackConnection(c);
            throw new CreateUserException("Save user in database failed", e);
        } finally {
            db.closeResource(c);
        }
    }

    public static User authenticate(User user) {
        log.trace("User for authenticate {}", user);
        Connection c = null;
        try {
            c = db.getConnection();
            User savedUser = db.findUser(c, user);
            log.trace("Saved user = {}", savedUser);
            if (!savedUser.checkSame(user)) {
                throw new PermissionDenied("Login or password was wrong");
            }
            savedUser.hidePassword();
            return savedUser;
        } catch (SQLException e) {
            db.rollbackConnection(c);
            throw new DBException(e.getMessage(), e);
        } finally {
            db.closeResource(c);
        }
    }
}
