package ua.epam.cargo_delivery.model.db;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.epam.cargo_delivery.model.Util;

@Data
@EqualsAndHashCode(callSuper = true)
public class City extends Entity {

    private String name;
    private String region;

    @Builder
    public City(Long id, String name, String region) {
        super(id);
        this.name = name;
        this.region = region;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
