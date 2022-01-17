package com.fairycompany.reviewer.model.entity;

import java.time.LocalDate;

public class User extends Entity{
    private long userId;
    private String login;
    private String firstName;
    private String secondName;
    private LocalDate birthday;
    private int phone;
    private String photo;
    private Role userRole;
    private Status userStatus;

    public enum Status {
        NOT_CONFIRMED, BANNED, ACTIVE
    }

    public enum Role {
        ADMIN, USER, GUEST
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Status getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (phone != user.phone) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (secondName != null ? !secondName.equals(user.secondName) : user.secondName != null) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (photo != null ? !photo.equals(user.photo) : user.photo != null) return false;
        if (userRole != user.userRole) return false;
        return userStatus == user.userStatus;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + phone;
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (userStatus != null ? userStatus.hashCode() : 0);
        return result;
    }

    public User clone() {
        User newUser;
        try {
            newUser = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("This shouldn't happen, since User class is Cloneable");
        }
        return newUser;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("User { ")
                .append("userId = ").append(userId)
                .append(", login = ").append(login)
                .append(", firstName = ").append(firstName)
                .append(", secondName = ").append(secondName)
                .append(", birthday = ").append(birthday)
                .append(", phone = ").append(phone)
                .append(", photo = ").append(photo)
                .append(", userRole = ").append(userRole)
                .append(", userStatus = ").append(userStatus)
                .append(" }");
        return builder.toString();
    }

    public static class UserBuilder {
        private User user;

        public UserBuilder() {
            user = new User();
        }

        public UserBuilder setUserId(long userId) {
            user.setUserId(userId);
            return this;
        }

        public UserBuilder setLogin(String login) {
            user.setLogin(login);
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            user.setFirstName(firstName);
            return this;
        }

        public UserBuilder setSecondName(String secondName) {
            user.setSecondName(secondName);
            return this;
        }

        public UserBuilder setBirthday(LocalDate birthday) {
            user.setBirthday(birthday);
            return this;
        }

        public UserBuilder setPhone(int phone) {
            user.setPhone(phone);
            return this;
        }

        public UserBuilder setPhoto(String photo) {
            user.setPhoto(photo);
            return this;
        }

        public UserBuilder setUserRole(Role userRole) {
            user.setUserRole(userRole);
            return this;
        }

        public UserBuilder setUserStatus(Status userStatus) {
            user.setUserStatus(userStatus);
            return this;
        }

        public User createUser() {
            return user;
        }
    }
}
