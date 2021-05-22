package ua.epam.cargo_delivery.model.dao;

public enum Role {
    USER(1), AUTHORIZE_USER(2), MANAGER(3);

    private int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
