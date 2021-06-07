package ua.epam.cargo_delivery.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.dto.DeliveryDTO;
import ua.epam.cargo_delivery.dto.MapBoxResp;
import ua.epam.cargo_delivery.exceptions.HttpException;
import ua.epam.cargo_delivery.model.Util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MapBoxClient {
    private static final Logger log = LogManager.getLogger(MapBoxClient.class);
    private static String uri = "https://api.mapbox.com/directions/v5/mapbox/driving/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static String secretToken;

    private MapBoxClient() {
        //hide
    }

    public static void setSecretToken(String token) {
        if (secretToken == null) {
            MapBoxClient.secretToken = token;
        }
    }

    public static DeliveryDTO getGeoJson(String from, String to) {
        from = from.replace(" ", "");
        to = to.replace(" ", "");
        String url = uri.concat(from)
                .concat(";")
                .concat(to)
                .concat("?overview=full&geometries=geojson")
                .concat("&access_token=").concat(secretToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        try {
            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            MapBoxResp resp = Util.readValue(body, MapBoxResp.class);
            return DeliveryDTO.builder()
                    .lngLat(resp.getLngLat())
                    .distance(resp.getDistance()).build();
        } catch (IOException | InterruptedException e) {
            String message = "Can't calculate distance!";
            log.error(message, e);
            throw new HttpException(message, e);
        }
    }
}
