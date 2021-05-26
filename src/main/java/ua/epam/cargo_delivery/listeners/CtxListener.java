package ua.epam.cargo_delivery.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.dao.DBManager;
import ua.epam.cargo_delivery.model.dao.Delivery;
import ua.epam.cargo_delivery.model.dao.DeliveryManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.List;

@WebListener
public class CtxListener implements ServletContextListener {
    private final Logger log = LogManager.getLogger(CtxListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            checkConnectToDB();
            setDisplayedDataForUsers(event);
        } catch (Exception e) {
            IllegalStateException ex = new IllegalStateException("Fail context initialize", e);
            log.error(ex.getMessage(), e);
            throw ex;
        }
    }

    private void setDisplayedDataForUsers(ServletContextEvent e) {
        List<Delivery> deliveries = DeliveryManager.findDeliveries(5, 0);
        e.getServletContext().setAttribute("deliveries", deliveries);
    }

    private void checkConnectToDB() throws SQLException {
        DBManager instance = DBManager.getInstance();
        log.info("Successful init DBManager and get connection {}", instance.getConnection());
    }
}
