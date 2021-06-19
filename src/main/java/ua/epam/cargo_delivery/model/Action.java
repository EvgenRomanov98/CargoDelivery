package ua.epam.cargo_delivery.model;

import ua.epam.cargo_delivery.exceptions.AppException;

public enum Action {
    ROOT("/"), AUTH("/authorization"), CALCULATE_PRICE("/calculatePrice"),
    REGIONS("/availableRegions"), CREATE_DELIVERY("/createDelivery"), MANAGEMENT("/manager"),
    PAGINATION("/paginationDelivery"), PAY("/pay"), PRIVATE_OFFICE("/privateOffice"),
    RECEIPT("/getReceipt"), REGISTRATION("/registration"), SIGN_OUT("/signOut"),
    UPDATE_DELIVERY_DATE("/updateDeliveryDate"), CHANGE_STATUS("/changeStatus"), XLS("/getReport");

    private final String endpoint;

    Action(String endpoint) {
        this.endpoint = endpoint;
    }

    public static Action findAction(String endpoint) {
        for (Action act : Action.values()) {
            if (act.endpoint.equals(endpoint)) {
                return act;
            }
        }
        throw new AppException("Endpoint " + endpoint + " doesn't registered");
    }
}
