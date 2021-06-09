package ua.epam.cargo_delivery.model.db;

import lombok.Builder;
import lombok.Data;
import ua.epam.cargo_delivery.model.Util;

@Data
public class City {

    private Integer id;
    private String name;
    private String key;

    public City() {
    }

    public City(Integer id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
