package ua.nmu.cargo_delivery.dto;

import lombok.Builder;
import lombok.Data;
import ua.nmu.cargo_delivery.model.Util;

@Data
@Builder
public class UserDTO {
    private boolean exist;

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
