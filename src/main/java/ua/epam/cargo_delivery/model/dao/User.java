package ua.epam.cargo_delivery.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.CreateUserException;
import ua.epam.cargo_delivery.model.EncryptUtil;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class User implements Serializable {
    private final Logger log = LogManager.getLogger(User.class);

    private long id;
    private String email;
    private String password;
    private Role role;

    public User() {
    }

    private void init(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password, Role role, boolean passwordEncrypt) {
        if (passwordEncrypt) {
            try {
                password = EncryptUtil.getSaltedHash(password);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new CreateUserException("Hashing password user failed", e);
            }
        }
        init(email, password, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }
}
