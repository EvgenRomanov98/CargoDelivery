package ua.epam.cargo_delivery.model.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.epam.cargo_delivery.model.Util;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Delivery extends Entity {
    private String whence;
    private String whither;
    private String fromName;
    private String toName;
    private LocalDate createDate;
    private LocalDate deliveryDate;
    private Float distance;
    private Integer price;
    private Cargo cargo;
    private DeliveryStatus status;
    private User user;
    private City fromRegion;
    private City toRegion;

    public Delivery() {
    }

    public Delivery(Long id, String whence, String whither, String fromName, String toName, LocalDate createDate, LocalDate deliveryDate, Float distance, Integer price, DeliveryStatus status) {
        init(id, whence, whither, fromName, toName, createDate, deliveryDate, distance, price, status);
    }

    public Delivery(String whence, String whither) {
        init(null, whence, whither, null, null, null, null, null, null, DeliveryStatus.CREATED);
    }

    private void init(Long id, String whence, String whither, String fromName, String toName, LocalDate createDate, LocalDate deliveryDate, Float distance, Integer price, DeliveryStatus status) {
        this.id = id;
        this.whence = whence;
        this.whither = whither;
        this.fromName = fromName;
        this.toName = toName;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.distance = distance;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    public void calculatePrice() {
        this.price = Math.round((distance / 1000) * 5 +
                (cargo.getWeight() + cargo.getLength() + cargo.getWidth() + cargo.getHeight()) / 10f);
    }
}
