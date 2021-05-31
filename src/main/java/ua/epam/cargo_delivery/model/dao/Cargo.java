package ua.epam.cargo_delivery.model.dao;

import ua.epam.cargo_delivery.model.Util;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return Objects.equals(description, cargo.description) && Objects.equals(weight, cargo.weight) && Objects.equals(length, cargo.length) && Objects.equals(width, cargo.width) && Objects.equals(height, cargo.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, weight, length, width, height);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
