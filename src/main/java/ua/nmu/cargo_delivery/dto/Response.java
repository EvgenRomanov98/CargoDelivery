package ua.nmu.cargo_delivery.dto;

import lombok.Builder;
import lombok.Data;
import ua.nmu.cargo_delivery.model.Util;

@Data
@Builder
public class Response {
    private String message;

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
