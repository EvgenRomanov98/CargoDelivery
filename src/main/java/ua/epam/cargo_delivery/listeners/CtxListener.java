package ua.epam.cargo_delivery.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.clients.MapBoxClient;
import ua.epam.cargo_delivery.exceptions.AppException;
import ua.epam.cargo_delivery.model.db.DBManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class CtxListener implements ServletContextListener {
    private final Logger log = LogManager.getLogger(CtxListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            checkConnectToDB();
        } catch (Exception e) {
            AppException ex = new AppException("Fail context initialize", e);
            log.error(ex.getMessage(), e);
            throw ex;
        }

        Properties property = new Properties();
        try (InputStream is = event.getServletContext().getResourceAsStream("/WEB-INF/classes/app.properties")) {
            property.load(is);
            MapBoxClient.setSecretToken(property.getProperty("mapBox.token"));
        } catch (IOException e) {
            String message = "File not found! MapBoxClient doesn't setup";
            log.error(message, e);
            throw new AppException(message, e);
        }
    }

    private void checkConnectToDB() throws SQLException {
        DBManager instance = DBManager.getInstance();
        log.info("Successful init DBManager and get connection {}", instance.getConnection());
    }
}
