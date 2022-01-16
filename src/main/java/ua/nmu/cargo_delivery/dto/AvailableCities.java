package ua.nmu.cargo_delivery.dto;

import lombok.Data;
import ua.nmu.cargo_delivery.model.Util;
import ua.nmu.cargo_delivery.model.db.City;

import java.util.List;

@Data
public class AvailableCities {
    private List<City> availableRegions;

    public AvailableCities(List<City> availableRegions) {
        this.availableRegions = availableRegions;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
