package ua.nmu.cargo_delivery.model.db;

public enum DeliveryStatus {

    CREATED(1), APPROVED(2), PAID(3), TRANSFER(4), DELIVERED(5), DELETED(6);

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
