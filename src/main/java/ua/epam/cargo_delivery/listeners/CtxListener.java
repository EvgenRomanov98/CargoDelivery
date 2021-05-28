package ua.epam.cargo_delivery.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.dao.DBManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class CtxListener implements ServletContextListener {
    private final Logger log = LogManager.getLogger(CtxListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            checkConnectToDB();
        } catch (Exception e) {
            IllegalStateException ex = new IllegalStateException("Fail context initialize", e);
            log.error(ex.getMessage(), e);
            throw ex;
        }
    }

    private void checkConnectToDB() throws SQLException {
        DBManager instance = DBManager.getInstance();
        log.info("Successful init DBManager and get connection {}", instance.getConnection());
    }
}
