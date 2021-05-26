package ua.epam.cargo_delivery.model.dao;

import ua.epam.cargo_delivery.model.Util;

import java.util.Date;
import java.util.Objects;

public class Delivery extends Entity {

    private String from;
    private String to;
    private Date createDate;
    private Date deliveryDate;
    private Long distance;
    private Long price;

    public Delivery() {
    }

    public Delivery(Long id, String from, String to, Date createDate, Date deliveryDate, Long distance, Long price) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.distance = distance;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(from, delivery.from) && Objects.equals(to, delivery.to) && Objects.equals(createDate, delivery.createDate) && Objects.equals(deliveryDate, delivery.deliveryDate) && Objects.equals(distance, delivery.distance) && Objects.equals(price, delivery.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, createDate, deliveryDate, distance, price);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
