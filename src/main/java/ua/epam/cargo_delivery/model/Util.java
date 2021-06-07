package ua.epam.cargo_delivery.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.dto.DeliveryDTO;
import ua.epam.cargo_delivery.dto.MapBoxResp;
import ua.epam.cargo_delivery.exceptions.HttpException;
import ua.epam.cargo_delivery.model.db.Cargo;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.User;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class Util {
    private static final ObjectMapper om = new ObjectMapper();
    private static final Logger log = LogManager.getLogger(Util.class);
    private static final HttpClient client;
    private static String token;
    private static String uri = "https://api.mapbox.com/directions/v5/mapbox/driving/";

    static {
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.findAndRegisterModules();

        client = HttpClient.newHttpClient();
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/app.properties");) {
            property.load(fis);
            token = property.getProperty("mapBox.token");
        } catch (IOException e) {
            System.err.println("File not found!");
        }
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
        delivery.calculatePrice();
        return delivery;
    }

    //    https://api.mapbox.com/directions/v5/mapbox/driving/-122.39636,37.79129;-122.39732,37.79283?overview=full&geometries=geojson&access_token=pk.eyJ1IjoiZXZyb205OCIsImEiOiJja3BqbWJ2dGUweWNxMnZvOG56bTg3OWcyIn0.dzdxhoOvGG6pBQz0uiDFmQ
    public static DeliveryDTO getGeoJson(String from, String to) {
        from = from.replaceAll(" ", "");
        to = to.replaceAll(" ", "");
        String url = uri.concat(from)
                .concat(";")
                .concat(to)
                .concat("?overview=full&geometries=geojson")
                .concat("&access_token=").concat(token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        try {
            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            MapBoxResp resp = om.readValue(body, MapBoxResp.class);
            return DeliveryDTO.builder()
                    .lngLat(resp.getLngLat())
                    .distance(resp.getDistance()).build();
        } catch (IOException | InterruptedException e) {
            String message = "Can't calculate distance!";
            log.error(message);
            throw new HttpException(message, e);
        }
    }
}
