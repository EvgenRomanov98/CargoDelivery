package ua.epam.cargo_delivery.model.db;

import ua.epam.cargo_delivery.model.Action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ua.epam.cargo_delivery.model.Action.*;

public enum Role {
    USER(1, ROOT, AUTH, REGISTRATION, CALCULATE_PRICE, REGIONS, PAGINATION),
    AUTHORIZE_USER(2, ROOT, CALCULATE_PRICE, REGIONS,
            PAGINATION, CREATE_DELIVERY, PRIVATE_OFFICE, PAY, RECEIPT, SIGN_OUT),
    MANAGER(3, ROOT, MANAGEMENT, PAGINATION, SIGN_OUT, UPDATE_DELIVERY_DATE, CHANGE_STATUS, XLS);

    private final int id;
    private final Set<Action> actions = new HashSet<>();

    Role(int id, Action... actions) {
        this.id = id;
        this.actions.addAll(Arrays.asList(actions));
    }

    public int getId() {
        return id;
    }

    public static Role valueOf(int i) {
        for (Role role : Role.values()) {
            if (role.id == i) {
                return role;
            }
        }
        throw new IllegalStateException("User role not found");
    }

    public boolean checkPermission(Action action) {
        return this.actions.contains(action);
    }
}
