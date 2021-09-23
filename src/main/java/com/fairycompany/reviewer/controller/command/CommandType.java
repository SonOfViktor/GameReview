package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.controller.command.impl.LocalCommand;
import com.fairycompany.reviewer.controller.command.impl.LoginCommand;
import com.fairycompany.reviewer.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.fairycompany.reviewer.model.entity.User.Role.*;

public enum CommandType {
    LOGIN(new LoginCommand(), GUEST),
    LOCALE(new LocalCommand(), GUEST, USER, ADMIN);

    private Set<User.Role> roles;
    private Command command;

    CommandType(Command command, User.Role ... roles) {
        this.command = command;
        this.roles = Set.of(roles);
    }

    public Command getCommand() {
        return command;
    }

    public boolean isValidRole(User.Role role) {
        return roles.stream().anyMatch(r -> r == role);
    }
}
