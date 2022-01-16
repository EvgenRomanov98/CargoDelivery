package ua.nmu.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.nmu.cargo_delivery.exceptions.AppException;
import ua.nmu.cargo_delivery.exceptions.CreateUserException;
import ua.nmu.cargo_delivery.exceptions.DBException;
import ua.nmu.cargo_delivery.exceptions.PermissionDenied;

import java.sql.Connection;
import java.sql.SQLException;

public class UserManager {
    private static final Logger log = LogManager.getLogger(UserManager.class);
    private static final DBManager db = DBManager.getInstance();

    private UserManager() {
        //hide
    }

    public static void saveUser(User user) {
        try (Connection c = db.getConnection()) {
            db.insertUser(c, user);
        } catch (SQLException e) {
            throw new CreateUserException("Create user failed", e);
        }
    }

    public static User authenticate(User user) {
        try (Connection c = db.getConnection()) {
            User savedUser = db.findUser(c, user);
            if (!savedUser.checkSame(user)) {
                throw new PermissionDenied("Login or password was wrong");
            }
            savedUser.hidePassword();
            return savedUser;
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public static boolean findEmail(String email) {
        try (Connection c = db.getConnection()) {
            db.findUser(c, User.builder().email(email).build());
            return true;
        } catch (SQLException e) {
            throw new AppException("Find user by email failed", e);
        } catch (DBException e) {
            return false;
        }
    }

    public static boolean findPhone(String phone) {
        try (Connection c = db.getConnection()) {
            db.findUserByPhone(c, User.builder().phone(phone).build());
            return true;
        } catch (SQLException e) {
            throw new AppException("Find user by phone failed", e);
        } catch (DBException e) {
            return false;
        }
    }
}
