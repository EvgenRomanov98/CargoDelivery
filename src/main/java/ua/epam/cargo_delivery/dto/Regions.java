package ua.epam.cargo_delivery.dto;

import lombok.Data;
import ua.epam.cargo_delivery.model.Util;
import ua.epam.cargo_delivery.model.db.City;

import java.util.List;

@Data
public class Regions {
    private List<City> availableRegions;

    public Regions(List<City> availableRegions) {
        this.availableRegions = availableRegions;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
