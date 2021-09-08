package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.controller.command.impl.LoginCommand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum CommandType {
    LOGIN(new LoginCommand());

//    private List<String> roles;
    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

//    CommandType(String... roles) {
//        this.roles = List.of(roles);
//    }

//    public static Optional<CommandType> fromString(String type) {
//        return Stream.of(CommandType.values())
//                .filter(e -> e.name().equalsIgnoreCase(type))
//                .findFirst();
//    }

//    public boolean isValidRole(String role) {
//        return roles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
//    }
}
