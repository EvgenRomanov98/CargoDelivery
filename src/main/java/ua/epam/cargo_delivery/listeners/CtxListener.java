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
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBManager instance = DBManager.getInstance();
            log.info("Successful init DBManager and get connection {}", instance.getConnection());
        } catch (SQLException e) {
            log.error(e);
            throw new IllegalStateException("Fail init DBManager", e);
        }
    }
}
