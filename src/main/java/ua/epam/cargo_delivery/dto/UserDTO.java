package ua.epam.cargo_delivery.dto;

import lombok.Builder;
import lombok.Data;
import ua.epam.cargo_delivery.model.Util;

@Data
@Builder
public class UserDTO {
    private boolean exist;

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
