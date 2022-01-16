package ua.nmu.cargo_delivery.model.db;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.nmu.cargo_delivery.model.Util;

@Data
@EqualsAndHashCode(callSuper = true)
public class City extends Entity {

    private String name;
    private String region;
    private String localeKey;

    @Builder
    public City(Long id, String name, String region, String localeKey) {
        super(id);
        this.name = name;
        this.region = region;
        this.localeKey = localeKey;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
