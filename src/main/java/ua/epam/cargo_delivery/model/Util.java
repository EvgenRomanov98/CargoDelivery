package ua.epam.cargo_delivery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.AppException;
import ua.epam.cargo_delivery.model.db.Cargo;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.http.HttpServletRequest;

public class Util {
    private static final ObjectMapper om = new ObjectMapper();
    private static final Logger log = LogManager.getLogger(Util.class);

    static {
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.findAndRegisterModules();
    }

    private Util() {
        // hide
    }

    public static String toString(Object o) {
        try {
            return om.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Can't write toString for object ".concat(o.getClass().toString()), e);
        }
        return "Error processing toString";
    }

    public static <T> T readValue(String s, Class<T> classCast) {
        try {
            return om.readValue(s, classCast);
        } catch (JsonProcessingException e) {
            String message = "Can't parse string value to " + classCast;
            log.error(message, e);
            throw new AppException(message, e);
        }
    }

    public static Delivery extractDeliveryFromReq(HttpServletRequest req) {
        Delivery delivery = new Delivery(req.getParameter("from"), req.getParameter("to"));
        Cargo cargo = new Cargo(
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("weight")),
                Integer.parseInt(req.getParameter("length")),
                Integer.parseInt(req.getParameter("width")),
                Integer.parseInt(req.getParameter("height"))
        );
        delivery.setUser((User) req.getSession().getAttribute("loggedUser"));
        delivery.setCargo(cargo);
        return delivery;
    }
}
