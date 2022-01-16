package ua.nmu.cargo_delivery.servlets.commands;

import ua.nmu.cargo_delivery.dto.UserDTO;
import ua.nmu.cargo_delivery.model.db.UserManager;

public class CheckEmail implements Command {

    @Override
    public String execute(String email) {
        return UserDTO.builder().exist(
                UserManager.findEmail(email))
                .build().toString();
    }
}
