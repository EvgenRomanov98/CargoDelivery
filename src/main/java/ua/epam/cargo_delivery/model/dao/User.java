package ua.epam.cargo_delivery.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.epam.cargo_delivery.exceptions.CreateUserException;
import ua.epam.cargo_delivery.model.EncryptUtil;
import ua.epam.cargo_delivery.model.Util;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class User implements Serializable {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Role role;

    public User() {
    }

    public User(String email, String password, boolean hashPassword) {
        init(null, email, password, null, null, null, Role.USER, hashPassword);
    }

    public User(String email, String password, String name, String surname, String phone, boolean hashPassword) {
        init(null, email, password, name, surname, phone, Role.USER, hashPassword);
    }

    public User(Long id, String email, String password, String name, String surname, String phone, Role role, boolean hashPassword) {
        init(id, email, password, name, surname, phone, role, hashPassword);
    }

    private String hashPassword(String password) {
        try {
            password = EncryptUtil.getSaltedHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CreateUserException("Hashing password user failed", e);
        }
        return password;
    }

    private void init(Long id, String email, String password, String name, String surname, String phone, Role role, boolean hashPassword) {
        this.id = id;
        this.email = email;
        this.password = hashPassword ? hashPassword(password) : password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Checks mail and password at user {@code u}
     *
     * @param u - verified user
     * @return true if same
     */
    public boolean checkSame(User u) {
        if (u == null) return false;
        if (this == u) return true;
        if (!password.contains("$")) {
            throw new IllegalStateException("this.password not hashing");
        }
        try {
            return email.equals(u.email) && EncryptUtil.check(u.password, password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Hashing error during compare to password", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(phone, user.phone) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, phone, role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    public void hidePassword() {
        password = "";
    }
}
