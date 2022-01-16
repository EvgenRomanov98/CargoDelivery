package ua.nmu.cargo_delivery.model.db;

import ua.nmu.cargo_delivery.model.Action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Role {
    USER(1, Action.ROOT, Action.AUTH, Action.REGISTRATION, Action.CALCULATE_PRICE, Action.REGIONS, Action.PAGINATION, Action.VALIDATE),
    AUTHORIZE_USER(2, Action.ROOT, Action.CALCULATE_PRICE, Action.REGIONS,
            Action.PAGINATION, Action.CREATE_DELIVERY, Action.PRIVATE_OFFICE, Action.PAY, Action.RECEIPT, Action.SIGN_OUT, Action.VALIDATE),
    MANAGER(3, Action.ROOT, Action.MANAGEMENT, Action.PAGINATION, Action.SIGN_OUT, Action.UPDATE_DELIVERY_DATE, Action.CHANGE_STATUS, Action.XLS, Action.VALIDATE);

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
