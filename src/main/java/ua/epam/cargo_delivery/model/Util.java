package ua.epam.cargo_delivery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {
    private static final ObjectMapper om = new ObjectMapper();
    private static final Logger log = LogManager.getLogger(Util.class);

    static {
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
}
