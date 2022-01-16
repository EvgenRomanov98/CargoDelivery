package ua.nmu.cargo_delivery.servlets.commands;

import ua.nmu.cargo_delivery.dto.UserDTO;
import ua.nmu.cargo_delivery.model.db.UserManager;

public class CheckPhone implements Command {
    @Override
    public String execute(String phone) {
        return UserDTO.builder().exist(
                UserManager.findPhone(phone))
                .build().toString();
    }
}
