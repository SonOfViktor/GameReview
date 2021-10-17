package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.controller.command.impl.LocalCommand;
import com.fairycompany.reviewer.controller.command.impl.LoginCommand;
import com.fairycompany.reviewer.controller.command.impl.CreateUserCommand;
import com.fairycompany.reviewer.controller.command.impl.LogoutCommand;
import com.fairycompany.reviewer.controller.command.impl.gotocommand.GoToCreateUserPage;
import com.fairycompany.reviewer.controller.command.impl.gotocommand.GoToExceptionPage;
import com.fairycompany.reviewer.controller.command.impl.gotocommand.GoToMainPage;
import com.fairycompany.reviewer.model.entity.User;

import java.util.Set;

import static com.fairycompany.reviewer.model.entity.User.Role.*;

public enum CommandType {
    LOGIN(new LoginCommand(), GUEST),
    LOGOUT(new LogoutCommand(), USER, ADMIN),
    LOCALE(new LocalCommand(), GUEST, USER, ADMIN),
    CREATE_USER(new CreateUserCommand(), GUEST),
    TO_SING_UP_PAGE(new GoToCreateUserPage(), GUEST),
    TO_EXCEPTION_PAGE(new GoToExceptionPage(), GUEST, USER, ADMIN),
    TO_MAIN_PAGE(new GoToMainPage(), GUEST);

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
