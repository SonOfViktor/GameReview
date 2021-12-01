package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.controller.command.impl.*;
import com.fairycompany.reviewer.controller.command.impl.gotocommand.*;
import com.fairycompany.reviewer.model.entity.User;

import java.util.Set;

import static com.fairycompany.reviewer.model.entity.User.Role.*;

public enum CommandType {
    LOGIN(new LoginCommand(), GUEST),
    LOGOUT(new LogoutCommand(), USER, ADMIN),
    LOCALE(new LocalCommand(), GUEST, USER, ADMIN),
    CREATE_USER(new CreateUserCommand(), GUEST),
    UPDATE_USER(new UpdateUserCommand(), USER, ADMIN),
    UPDATE_PASSWORD(new UpdatePasswordCommand(), USER, ADMIN),
    UPDATE_PHOTO(new UpdatePhotoCommand(), USER, ADMIN),
    FINISH_REGISTRATION(new FinishRegistrationCommand(), GUEST, USER, ADMIN),
    TO_SING_UP_PAGE(new GoToCreateUserPage(), GUEST),
    TO_ADD_GAME_PAGE(new GoToCreateGamePage(), ADMIN, GUEST),                   //todo delete guest
    CREATE_GAME(new CreateGameCommand(), ADMIN, GUEST),                          // todo delete guest
    CREATE_UPDATE_GAME_RATING(new CreateGameUpdateRatingCommand(), USER, ADMIN),
    TO_EXCEPTION_PAGE(new GoToExceptionPage(), GUEST, USER, ADMIN),
    TO_MAIN_PAGE(new GoToMainPage(), GUEST, USER, ADMIN),
    TO_GAME_PAGE(new GoToGamePage(), GUEST, USER, ADMIN),
    TO_USER_PAGE(new GoToUserPage(), USER, ADMIN),
    TO_USER_EDITOR_PAGE(new GoToUserEditorPage(), USER, ADMIN);

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
