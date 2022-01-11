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
import ua.epam.cargo_delivery.model.db.City;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

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
        } catch (IOException e) {
            String message = "Can't parse string value to " + classCast;
            log.error(message, e);
            throw new AppException(message, e);
        }
    }

    public static Delivery extractDeliveryFromReq(HttpServletRequest req) {
        Delivery delivery = new Delivery(req.getParameter("from"), req.getParameter("to"));
        delivery.setFromName(req.getParameter("fromName"));
        delivery.setToName(req.getParameter("toName"));
        Optional.ofNullable(req.getParameter("fromRegionId")).filter(s -> !s.isBlank()).ifPresent(id -> delivery.setFromRegion(City.builder()
                .id(Long.parseLong(id))
                .build()));
        Optional.ofNullable(req.getParameter("toRegionId")).filter(s -> !s.isBlank()).ifPresent(id -> delivery.setToRegion(City.builder()
                .id(Long.parseLong(id))
                .build()));
        Cargo cargo = new Cargo(
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("weight")),
                Integer.parseInt(req.getParameter("length")),
                Integer.parseInt(req.getParameter("width")),
                Integer.parseInt(req.getParameter("height"))
        );
        delivery.setUser((User) req.getSession().getAttribute("loggedUser"));
        delivery.setCargo(cargo);
        delivery.setPrice((Integer) req.getSession().getAttribute("price"));
        delivery.setDistance((Float) req.getSession().getAttribute("distance"));
        return delivery;
    }

    public static void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                log.error("Fail close resource", e);
            }
        }
    }
}
