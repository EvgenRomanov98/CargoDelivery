package ua.epam.cargo_delivery.model.db;

import java.io.Serializable;

public class Entity implements Serializable {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
