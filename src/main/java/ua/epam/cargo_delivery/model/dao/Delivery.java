package ua.epam.cargo_delivery.model.dao;

import ua.epam.cargo_delivery.model.Util;

import java.util.Date;
import java.util.Objects;

public class Delivery extends Entity {
    private String whence;
    private String whither;
    private Date createDate;
    private Date deliveryDate;
    private Float distance;
    private Integer price;
    private Cargo cargo;
    private DeliveryStatus status;
    private User user;

    public Delivery() {
    }

    public Delivery(Long id, String whence, String whither, Date createDate, Date deliveryDate, Float distance, Integer price, DeliveryStatus status) {
        init(id, whence, whither, createDate, deliveryDate, distance, price, status);
    }

    public Delivery(String whence, String whither, Date createDate, Date deliveryDate, Float distance, Integer price, DeliveryStatus status) {
        init(null, whence, whither, createDate, deliveryDate, distance, price, status);
    }

    public Delivery(String whence, String whither) {
        init(null, whence, whither, null, null, null, null, DeliveryStatus.CREATED);
    }

    private void init(Long id, String from, String to, Date createDate, Date deliveryDate, Float distance, Integer price, DeliveryStatus status) {
        this.id = id;
        this.whence = from;
        this.whither = to;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.distance = distance;
        this.price = price;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(whence, delivery.whence) && Objects.equals(whither, delivery.whither) && Objects.equals(createDate, delivery.createDate) && Objects.equals(deliveryDate, delivery.deliveryDate) && Objects.equals(distance, delivery.distance) && Objects.equals(price, delivery.price) && Objects.equals(cargo, delivery.cargo) && status == delivery.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(whence, whither, createDate, deliveryDate, distance, price, cargo, status);
    }

    public String getWhence() {
        return whence;
    }

    public void setWhence(String whence) {
        this.whence = whence;
    }

    public String getWhither() {
        return whither;
    }

    public void setWhither(String whither) {
        this.whither = whither;
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

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    public void calculatePrice() {
        this.price = Math.round(distance * 5 +
                (cargo.getWeight() + cargo.getLength() + cargo.getWidth() + cargo.getHeight()) / 1000f);
    }
}
