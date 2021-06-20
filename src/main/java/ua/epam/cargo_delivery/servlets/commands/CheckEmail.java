package ua.epam.cargo_delivery.servlets.commands;

import ua.epam.cargo_delivery.dto.UserDTO;
import ua.epam.cargo_delivery.model.db.UserManager;

public class CheckEmail implements Command {

    @Override
    public String execute(String email) {
        return UserDTO.builder().exist(
                UserManager.findEmail(email))
                .build().toString();
    }
}
