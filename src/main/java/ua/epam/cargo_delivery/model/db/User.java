package ua.epam.cargo_delivery.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.epam.cargo_delivery.exceptions.CreateUserException;
import ua.epam.cargo_delivery.model.EncryptUtil;
import ua.epam.cargo_delivery.model.Util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Data
@Builder()
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends Entity {
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Role role;

    public User() {
    }

    public User(Role role) {
        init(null, null, null, null, null, null, role, false);
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
    public String toString() {
        return Util.toString(this);
    }

    public void hidePassword() {
        password = "";
    }
}
