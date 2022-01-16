package ua.nmu.cargo_delivery.model.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.nmu.cargo_delivery.model.Util;

@Data
@EqualsAndHashCode(callSuper = true)
public class Cargo extends Entity {
    private String description;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;

    public Cargo() {
    }

    public Cargo(String description, Integer weight, Integer length, Integer width, Integer height) {
        init(null, description, weight, length, width, height);
    }

    public Cargo(Long id, String description, Integer weight, Integer length, Integer width, Integer height) {
        init(id, description, weight, length, width, height);
    }

    private void init(Long id, String description, Integer weight, Integer length, Integer width, Integer height) {
        this.description = description;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
