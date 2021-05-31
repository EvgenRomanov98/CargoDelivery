package ua.epam.cargo_delivery.model.dao;

public enum DeliveryStatus {

    CREATED(1), PAID(2), TRANSFER(3), DELIVERED(4);

    private int id;

    DeliveryStatus(int id) {
        this.id = id;
    }

    public static DeliveryStatus valueOf(int id){
        for (DeliveryStatus value : DeliveryStatus.values()) {
            if (value.id == id){
                return value;
            }
        }
        throw new IllegalStateException("Delivery status not found");
    }

    public int getId() {
        return id;
    }
}
