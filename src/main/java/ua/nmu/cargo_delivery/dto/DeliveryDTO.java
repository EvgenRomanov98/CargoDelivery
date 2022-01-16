package ua.nmu.cargo_delivery.dto;

import lombok.Builder;
import lombok.Data;
import ua.nmu.cargo_delivery.model.Util;
import ua.nmu.cargo_delivery.model.db.Delivery;

import java.util.List;

@Data
@Builder
public class DeliveryDTO {

    private String status;
    private String price;
    private List<Float[]> lngLat;
    private Float distance;
    private List<Delivery> deliveries;

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
