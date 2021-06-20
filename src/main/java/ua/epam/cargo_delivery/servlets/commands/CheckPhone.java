package ua.epam.cargo_delivery.servlets.commands;

import ua.epam.cargo_delivery.dto.UserDTO;
import ua.epam.cargo_delivery.model.db.UserManager;

public class CheckPhone implements Command {
    @Override
    public String execute(String phone) {
        return UserDTO.builder().exist(
                UserManager.findPhone(phone))
                .build().toString();
    }
}
