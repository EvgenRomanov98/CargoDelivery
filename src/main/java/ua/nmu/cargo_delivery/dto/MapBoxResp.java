package ua.nmu.cargo_delivery.dto;

import lombok.Data;
import ua.nmu.cargo_delivery.model.Util;

import java.util.List;

@Data
public class MapBoxResp {

    private List<Route> routes;

    @Data
    private static class Route {
        private Float distance;
        private Geometry geometry;

        @Data
        private static class Geometry {
            private List<Float[]> coordinates;
        }
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    public Float getDistance() {
        return this.routes.get(0).distance;
    }

    public List<Float[]> getLngLat() {
        return this.routes.get(0).geometry.coordinates;
    }


}
