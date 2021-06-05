package ua.epam.cargo_delivery.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class DBManagerTest {
    private static final Logger log = LogManager.getLogger(DBManagerTest.class);

    @Test
    public void testFetDeliveries() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(format.parse("2021-05-24 08:02:56.347715"));
        Timestamp timestamp = Timestamp.valueOf("2021-05-24 08:02:56.000000");
        log.info(timestamp);
    }

}