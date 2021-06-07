package ua.epam.cargo_delivery.dto;

import lombok.Builder;
import lombok.Data;
import ua.epam.cargo_delivery.model.Util;

import java.util.List;

@Data
@Builder
public class DeliveryDTO {

    private String status;
    private String price;
    private List<Float[]> lngLat;
    private Float distance;

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
